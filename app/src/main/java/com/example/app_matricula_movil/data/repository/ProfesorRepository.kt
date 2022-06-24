package com.example.app_matricula_movil.data.repository

import com.example.app_matricula_movil.data.network.service.ProfesorService
import com.example.app_matricula_movil.data.responses.ProfesorResponseLogin
import com.example.app_matricula_movil.data.responses.profesor.GetProfesoresResponse

class ProfesorRepository {
    private val profesorService = ProfesorService()

    suspend fun login(cedula: String, password: String, token: String): ProfesorResponseLogin? =
        profesorService.login(cedula, password, token)

    suspend fun getProfesores(token: String): GetProfesoresResponse? = profesorService.getProfesores(token)
}