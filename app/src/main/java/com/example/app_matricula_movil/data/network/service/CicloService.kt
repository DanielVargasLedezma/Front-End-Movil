package com.example.app_matricula_movil.data.network.service

import android.util.Log
import com.example.app_matricula_movil.core.RetrofitHelper
import com.example.app_matricula_movil.data.models.ciclo.Ciclo
import com.example.app_matricula_movil.data.models.profesor.Profesor
import com.example.app_matricula_movil.data.network.controller.CicloController
import com.example.app_matricula_movil.data.network.controller.ProfesorController
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

    suspend fun insertarCiclo(ciclo: Ciclo, token: String): Boolean{
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(CicloController::class.java)
                .insertarCiclo(ciclo)

            if (response.isSuccessful) {
                true
            } else {
                Log.v("MainActivity", "$response")
                false
            }
        }

    }
    suspend fun editarCiclo(ciclo: Ciclo, token: String): Boolean{
        return withContext(Dispatchers.IO){
            val response = RetrofitHelper.getRetrofit(token)
                .create(CicloController::class.java)
                .editarCiclo(ciclo, ciclo.id_ciclo)

            if (response.isSuccessful) {
                true
            } else {
                Log.v("MainActivity", "$response")
                false
            }

        }

    }
    suspend fun eliminarCiclo(id_ciclo: Int, token: String): Boolean{
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(CicloController::class.java)
                .eliminarCiclo(id_ciclo)

            if (response.isSuccessful) {
                true
            } else {
                Log.v("MainActivity", "$response")
                false
            }
        }
    }

}