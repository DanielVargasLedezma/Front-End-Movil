package com.example.app_matricula_movil.data.repository

import com.example.app_matricula_movil.data.network.service.AlumnoService
import com.example.app_matricula_movil.data.responses.AlumnoResponseLogin

class AlumnoRepository {
    private val alumnoService = AlumnoService()

    suspend fun login(cedula: String, password: String, token: String): AlumnoResponseLogin? =
        alumnoService.login(cedula, password, token)
}