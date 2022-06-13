package com.example.app_matricula_movil.data.network.service

import com.example.app_matricula_movil.core.RetrofitHelper
import com.example.app_matricula_movil.data.network.controller.CarreraController
import com.example.app_matricula_movil.data.responses.GetCarrerasResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CarreraService {
    suspend fun getCarreras(token: String) : GetCarrerasResponse? {
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
}