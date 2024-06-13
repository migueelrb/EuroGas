package com.example.eurogas.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eurogas.R
import com.example.eurogas.data.getUserPreferences
import com.example.eurogas.databinding.FragmentGasStationListBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GasStationList : Fragment() {

    private var _binding: FragmentGasStationListBinding? = null
    private val binding get() = _binding!!

    private val gasStationVM by viewModels<GasStationVM> { GasStationVM.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGasStationListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        lifecycleScope.launch {
            requireContext().getUserPreferences().collect { userPreferences ->
                val postalCode = userPreferences.codPostal
                gasStationVM.fetchGasStations(postalCode)
            }
        }

        lifecycleScope.launchWhenStarted {
            gasStationVM.uiState.collect { uiState ->
                binding.progressBar.visibility = if (uiState.loading) View.VISIBLE else View.GONE
                if (uiState.gasStations.isNotEmpty()) {
                    Log.d(
                        "GasStationList",
                        "Updating adapter with new data: ${uiState.gasStations}"
                    )
                    (binding.rvGasStation.adapter as GasStationAdapter).updateGasStations(uiState.gasStations)
                }
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                gasStationVM.filterGasStationsByName(newText ?: "")
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {
        binding.rvGasStation.layoutManager = LinearLayoutManager(requireContext())
        binding.rvGasStation.adapter = GasStationAdapter(listOf())
    }
}
