package com.example.eurogas.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.eurogas.R
import com.example.eurogas.data.UserPreferences
import com.example.eurogas.data.getUserPreferences
import com.example.eurogas.data.saveUserPreferences
import com.example.eurogas.databinding.FragmentUpdatePostalCodeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class UpdatePostalCodeFragment : Fragment() {

    private var _binding: FragmentUpdatePostalCodeBinding? = null
    private val binding get() = _binding!!

    private val gasStationVM by viewModels<GasStationVM> { GasStationVM.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdatePostalCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            requireContext().getUserPreferences().collect { userPreferences ->
                binding.editTextPostalCode.setText(userPreferences.codPostal)
            }
        }

        binding.buttonSave.setOnClickListener {
            val postalCode = binding.editTextPostalCode.text.toString()
            if (postalCode.isBlank()) {
                showAlertDialog("Error", "El código postal no puede estar vacío")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val userPreferences = UserPreferences(codPostal = postalCode)
                    requireContext().saveUserPreferences(userPreferences)
                    gasStationVM.refreshGasStations(postalCode)
                    showAlertDialog("Éxito", "Código postal guardado correctamente") {
                        findNavController().navigate(R.id.gasStationList)
                    }
                } catch (e: Exception) {
                    showAlertDialog("Error", "Error al guardar el código postal")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showAlertDialog(title: String, message: String, onDismiss: (() -> Unit)? = null) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                onDismiss?.invoke()
            }
            .show()
    }
}
