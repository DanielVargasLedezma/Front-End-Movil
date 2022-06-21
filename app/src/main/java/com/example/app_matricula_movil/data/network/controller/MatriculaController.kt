package com.example.app_matricula_movil.data.network.controller

import com.example.app_matricula_movil.data.responses.matricula.GetMatriculasAlumnoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MatriculaController {
    @GET("alumno/matriculas/{alumno}")
    suspend fun getMatriculasAlumno(@Path("alumno") cedula_alumno: String): Response<GetMatriculasAlumnoResponse>
}