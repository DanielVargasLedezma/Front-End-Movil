package com.example.app_matricula_movil.data.repository

import com.example.app_matricula_movil.data.network.service.MatriculaService
import com.example.app_matricula_movil.data.responses.matricula.GetMatriculasAlumnoResponse

class MatriculaRepository {
    private val matriculaService = MatriculaService()

    suspend fun getMatriculasAlumno(cedula_alumno: String, token: String): GetMatriculasAlumnoResponse? =
        matriculaService.getMatriculasAlumno(cedula_alumno, token)
}