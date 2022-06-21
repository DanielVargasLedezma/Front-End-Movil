package com.example.app_matricula_movil.data.network.service

import com.example.app_matricula_movil.core.RetrofitHelper
import com.example.app_matricula_movil.data.network.controller.MatriculaController
import com.example.app_matricula_movil.data.responses.matricula.GetMatriculasAlumnoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MatriculaService {
    suspend fun getMatriculasAlumno(cedula_alumno: String, token: String): GetMatriculasAlumnoResponse? {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(MatriculaController::class.java)
                .getMatriculasAlumno(cedula_alumno)

            if (response.isSuccessful) response.body()
            else null
        }
    }
}