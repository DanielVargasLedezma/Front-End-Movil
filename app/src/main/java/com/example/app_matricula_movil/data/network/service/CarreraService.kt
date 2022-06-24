package com.example.app_matricula_movil.data.network.service

import com.example.app_matricula_movil.core.RetrofitHelper
import com.example.app_matricula_movil.data.models.carrera.Carrera
import com.example.app_matricula_movil.data.network.controller.CarreraController
import com.example.app_matricula_movil.data.responses.GetCarrerasResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CarreraService {
    suspend fun getCarreras(token: String): GetCarrerasResponse? {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(CarreraController::class.java)
                .getCarreras()

            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }
    }

    suspend fun insertarCarrera(carrera: Carrera, token: String): Boolean {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(CarreraController::class.java)
                .insertarCarrera(carrera)

            response.isSuccessful
        }
    }

    suspend fun editarCarrera(carrera: Carrera, token: String): Boolean {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(CarreraController::class.java)
                .editarCarrera(carrera, carrera.codigo_carrera)

            response.isSuccessful
        }
    }

    suspend fun eliminarCarrera(codigo_carrera: String, token: String): Boolean {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(CarreraController::class.java)
                .eliminarCarrera(codigo_carrera)

            response.isSuccessful
        }
    }
}