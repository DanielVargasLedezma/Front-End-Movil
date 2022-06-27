package com.example.app_matricula_movil.data.repository

import com.example.app_matricula_movil.data.models.alumno.Alumno
import com.example.app_matricula_movil.data.network.service.AlumnoService
import com.example.app_matricula_movil.data.responses.AlumnoResponseLogin
import com.example.app_matricula_movil.data.responses.alumno.GetAlumnosResponse

class AlumnoRepository {
    private val alumnoService = AlumnoService()

    suspend fun login(cedula: String, password: String, token: String): AlumnoResponseLogin? =
        alumnoService.login(cedula, password, token)

    suspend fun getAlumnos(token: String): GetAlumnosResponse? = alumnoService.getAlumnos(token)

    suspend fun registrarAlumno(alumno: Alumno, token: String): Boolean = alumnoService.registrarAlumno(alumno, token)

    suspend fun editarAlumno(alumno: Alumno, token: String): Boolean = alumnoService.editarAlumno(alumno,token)

    suspend fun eliminarAlumno(cedula_alumno: String, token: String): Boolean = alumnoService.eliminarAlumno(cedula_alumno, token)
}