package com.example.app_matricula_movil.data.repository

import com.example.app_matricula_movil.data.models.profesor.Profesor
import com.example.app_matricula_movil.data.network.service.ProfesorService
import com.example.app_matricula_movil.data.responses.ProfesorResponseLogin
import com.example.app_matricula_movil.data.responses.profesor.GetProfesoresResponse

class ProfesorRepository {
    private val profesorService = ProfesorService()

    suspend fun login(cedula: String, password: String, token: String): ProfesorResponseLogin? =
        profesorService.login(cedula, password, token)

    suspend fun getProfesores(token: String): GetProfesoresResponse? = profesorService.getProfesores(token)

    suspend fun registrarProfesor(profesor: Profesor, token: String): Boolean = profesorService.registrarProfesor(profesor, token)

    suspend fun editarProfesor(profesor: Profesor, token: String): Boolean = profesorService.editarProfesor(profesor, token)

    suspend fun eliminarProfesor(cedula_profesor: String, token: String): Boolean = profesorService.eliminarProfesor(cedula_profesor, token)

}