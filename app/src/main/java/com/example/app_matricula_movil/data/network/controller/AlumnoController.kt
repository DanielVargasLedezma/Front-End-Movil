package com.example.app_matricula_movil.data.network.controller

import com.example.app_matricula_movil.data.models.alumno.Alumno
import com.example.app_matricula_movil.data.responses.AlumnoResponseLogin
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface AlumnoController {
    @POST("login/alumno")
    suspend fun login(@Body alumno: Alumno): Response<AlumnoResponseLogin>
}