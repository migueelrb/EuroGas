package com.example.eurogas.repositories

import GasStationsResponse
import android.util.Log
import com.example.eurogas.api.ApiService
import retrofit2.Response

class GasStationRepository(
    private val gasStationApi: ApiService
) {

    /**
     * Esta función se encarga de obtener todas las gasolineras.
     * @return Response<GasStationsResponse> Respuesta de la petición
     */
    suspend fun getAllGasStations(): Response<GasStationsResponse> {
        return gasStationApi.getGasStations()
    }

    /**
     * Esta función se encarga de obtener las gasolineras por código postal.
     * @param postalCode Código postal
     * @return Response<GasStationsResponse> Respuesta de la petición
     */
    suspend fun getGasStationsByPostalCode(postalCode: String): Response<GasStationsResponse> {
        val response = gasStationApi.getGasStations()
        if (response.isSuccessful) {
            val gasStations = response.body()?.gasStations?.filter { it.postalCode == postalCode }
            return Response.success(GasStationsResponse(gasStations ?: emptyList(), "", "", ""))
        }
        return response
    }
}
