package com.example.app_matricula_movil.data.network.controller

import com.example.app_matricula_movil.data.models.profesor.Profesor
import com.example.app_matricula_movil.data.responses.ProfesorResponseLogin
import com.example.app_matricula_movil.data.responses.profesor.GetProfesoresResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProfesorController {
    @POST("login/profesor")
    suspend fun login(@Body profesor: Profesor): Response<ProfesorResponseLogin>

    @GET("profesores")
    suspend fun getProfesores(): Response<GetProfesoresResponse>

    @POST("crear-profesor")
    suspend fun registrarProfesor(@Body profesor: Profesor): Response<Void>

    @POST("profesor/editar/{profesor}")
    suspend fun editarProfesor(@Body profesor: Profesor, @Path("profesor")cedula_profesor: String):Response<Void>

    @DELETE("profesor/eliminar/{profesor}")
    suspend fun eliminarProfesor(@Path("profesor")cedula_profesor: String): Response<Void>

}