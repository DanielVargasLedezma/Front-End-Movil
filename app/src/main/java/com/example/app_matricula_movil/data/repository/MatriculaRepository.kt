package com.example.app_matricula_movil.data.repository

import com.example.app_matricula_movil.data.models.matricula.Matricula
import com.example.app_matricula_movil.data.network.service.MatriculaService
import com.example.app_matricula_movil.data.responses.matricula.GetMatricula
import com.example.app_matricula_movil.data.responses.matricula.GetMatriculasAlumnoResponse

class MatriculaRepository {
    private val matriculaService = MatriculaService()

    suspend fun getMatriculasAlumno(cedula_alumno: String, token: String): GetMatriculasAlumnoResponse? =
        matriculaService.getMatriculasAlumno(cedula_alumno, token)

    suspend fun getMatriculaAlumno(cedula_alumno: String, numero_grupo: String, token: String): GetMatricula? =
        matriculaService.getMatriculaAlumno(cedula_alumno, numero_grupo, token)

    suspend fun crearMatricula(matricula: Matricula, token: String): Boolean =
        matriculaService.crearMatricula(matricula, token)

    suspend fun registrarNota(matricula: Matricula, token: String): Boolean =
        matriculaService.registrarNota(matricula, token)

    suspend fun desmatricularGrupo(cedula_alumno: String, numero_grupo: String, token: String): Boolean =
        matriculaService.desmatricularGrupo(cedula_alumno, numero_grupo, token)
}