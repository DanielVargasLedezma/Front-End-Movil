package com.example.app_matricula_movil.data.network.service

import com.example.app_matricula_movil.core.RetrofitHelper
import com.example.app_matricula_movil.data.models.matricula.Matricula
import com.example.app_matricula_movil.data.network.controller.MatriculaController
import com.example.app_matricula_movil.data.responses.matricula.GetMatricula
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

    suspend fun getMatriculaAlumno(cedula_alumno: String, numero_grupo: String, token: String): GetMatricula? {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(MatriculaController::class.java)
                .getMatriculaAlumno(cedula_alumno, numero_grupo)

            if (response.isSuccessful) response.body()
            else null
        }
    }

    suspend fun crearMatricula(matricula: Matricula, token: String): Boolean {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(MatriculaController::class.java)
                .crearMatricula(matricula)

            response.isSuccessful
        }
    }

    suspend fun registrarNota(matricula: Matricula, token: String): Boolean {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(MatriculaController::class.java)
                .registrarNota(matricula, matricula.numero_matricula)

            response.isSuccessful
        }
    }

    suspend fun desmatricularGrupo(cedula_alumno: String, numero_grupo: String, token: String): Boolean {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(MatriculaController::class.java)
                .desmatricularGrupo(cedula_alumno, numero_grupo)

            response.isSuccessful
        }
    }
}