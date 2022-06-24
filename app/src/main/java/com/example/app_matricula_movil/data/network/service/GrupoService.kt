package com.example.app_matricula_movil.data.network.service

import android.util.Log
import com.example.app_matricula_movil.core.RetrofitHelper
import com.example.app_matricula_movil.data.models.grupo.Grupo
import com.example.app_matricula_movil.data.network.controller.GrupoController
import com.example.app_matricula_movil.data.responses.grupo.GetGruposDeCursoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GrupoService {
    suspend fun getGruposDeCurso(id_curso: String, token: String): GetGruposDeCursoResponse? {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(GrupoController::class.java)
                .getGruposDeCurso(id_curso)

            if (response.isSuccessful) response.body()
            else null
        }
    }

    suspend fun getGruposDeProfesor(cedula_profesor: String, token: String): GetGruposDeCursoResponse? {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(GrupoController::class.java)
                .getGruposDeProfesor(cedula_profesor)

            if (response.isSuccessful) response.body()
            else null
        }
    }

    suspend fun getGruposMatriculadosDeAlumno(cedula_alumno: String, token: String): GetGruposDeCursoResponse? {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(GrupoController::class.java)
                .getGruposMatriculadosDeAlumno(cedula_alumno)

            if (response.isSuccessful) response.body()
            else null
        }
    }

    suspend fun getGruposDeCarrera(codigo_carrera: String, token: String): GetGruposDeCursoResponse? {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(GrupoController::class.java)
                .getGruposDeCarrera(codigo_carrera)

            if (response.isSuccessful) response.body()
            else null
        }
    }

    suspend fun insertarGrupo(grupo: Grupo, token: String): Boolean {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(GrupoController::class.java)
                .insertarGrupo(grupo)

            Log.v("A", "$response")
            response.isSuccessful
        }
    }

    suspend fun editarGrupo(grupo: Grupo, token: String): Boolean {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(GrupoController::class.java)
                .editarGrupo(grupo, grupo.numero_grupo)

            response.isSuccessful
        }
    }

    suspend fun eliminarGrupo(numero_grupo: String, token: String): Boolean {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(GrupoController::class.java)
                .eliminarGrupo(numero_grupo)

            response.isSuccessful
        }
    }
}