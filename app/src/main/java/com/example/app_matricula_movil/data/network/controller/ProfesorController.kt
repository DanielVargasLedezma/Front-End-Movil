package com.example.app_matricula_movil.data.network.controller

import com.example.app_matricula_movil.data.models.profesor.Profesor
import com.example.app_matricula_movil.data.responses.GetProfesoresResponse
import com.example.app_matricula_movil.data.responses.ProfesorResponseLogin
import com.example.app_matricula_movil.data.responses.profesor.GetProfesoresResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProfesorController {
    @POST("login/profesor")
    suspend fun login(@Body profesor: Profesor): Response<ProfesorResponseLogin>

    @GET("profesores")
    suspend fun getProfesores(): Response<GetProfesoresResponse>


}