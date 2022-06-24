package com.example.app_matricula_movil.data.network.service

import android.util.Log
import com.example.app_matricula_movil.core.RetrofitHelper
import com.example.app_matricula_movil.data.models.curso.Curso
import com.example.app_matricula_movil.data.network.controller.CursoController
import com.example.app_matricula_movil.data.responses.curso.GetCursosResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CursoService {
    suspend fun getCursos(token: String): GetCursosResponse? {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(CursoController::class.java)
                .getCursos()

            if (response.isSuccessful) {
                response.body()
            } else {
                Log.v("MainActivity", "$response")
                null
            }
        }
    }

    suspend fun insertarCurso(curso: Curso, token: String): Boolean {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(CursoController::class.java)
                .insertarCurso(curso)

            response.isSuccessful
        }
    }

    suspend fun editarCurso(curso: Curso, token: String): Boolean {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(CursoController::class.java)
                .editarCurso(curso, curso.codigo_curso)

            response.isSuccessful
        }
    }

    suspend fun eliminarCurso(codigo_curso: String, token: String): Boolean {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(CursoController::class.java)
                .eliminarCurso(codigo_curso)

            response.isSuccessful
        }
    }
}