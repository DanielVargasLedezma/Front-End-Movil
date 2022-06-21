package com.example.app_matricula_movil.data.repository

import com.example.app_matricula_movil.data.models.carrera.Carrera
import com.example.app_matricula_movil.data.network.service.CarreraService
import com.example.app_matricula_movil.data.responses.GetCarrerasResponse

class CarreraRepository {
    private val carreraService = CarreraService()

    suspend fun getCarreras(token: String): GetCarrerasResponse? = carreraService.getCarreras(token)

    suspend fun insertarCarrera(carrera: Carrera, token: String): Boolean =
        carreraService.insertarCarrera(carrera, token)

    suspend fun editarCarrera(carrera: Carrera, token: String): Boolean = carreraService.editarCarrera(carrera, token)

    suspend fun eliminarCarrera(codigo_carrera: String, token: String): Boolean =
        carreraService.eliminarCarrera(codigo_carrera, token)
}