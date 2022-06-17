package com.example.app_matricula_movil.data.network.service

import com.example.app_matricula_movil.core.RetrofitHelper
import com.example.app_matricula_movil.data.network.controller.CicloController
import com.example.app_matricula_movil.data.responses.ciclo.GetCiclosResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CicloService {
    suspend fun getCiclos(token: String): GetCiclosResponse? {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(CicloController::class.java)
                .getCiclos()

            if (response.isSuccessful) response.body()
            else null
        }
    }
}