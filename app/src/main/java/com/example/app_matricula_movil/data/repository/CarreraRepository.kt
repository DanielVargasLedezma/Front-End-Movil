package com.example.app_matricula_movil.data.repository

import com.example.app_matricula_movil.data.network.service.CarreraService
import com.example.app_matricula_movil.data.responses.GetCarrerasResponse

class CarreraRepository {
    private val carreraService = CarreraService()

    suspend fun getCarreras(token: String) : GetCarrerasResponse? = carreraService.getCarreras(token)
}