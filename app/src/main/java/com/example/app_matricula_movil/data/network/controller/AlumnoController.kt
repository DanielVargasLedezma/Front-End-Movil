package com.example.app_matricula_movil.data.network.controller

import com.example.app_matricula_movil.data.models.alumno.Alumno
import com.example.app_matricula_movil.data.responses.AlumnoResponseLogin
import com.example.app_matricula_movil.data.responses.GetAlumnosResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AlumnoController {
    @POST("login/alumno")
    suspend fun login(@Body alumno: Alumno): Response<AlumnoResponseLogin>

    @GET("alumnos")
    suspend fun getAlumnos(): Response<GetAlumnosResponse>

    @POST("crear-alumno")
    suspend fun registrarAlumno(@Body alumno: Alumno): Response<Void>

    @POST("alumno/editar/{alumno}")
    suspend fun editarAlumno(@Body alumno: Alumno, @Path("alumno") cedula_alumno: String): Response<Void>

    @DELETE("alumno/eliminar/{alumno}")
    suspend fun eliminarAlumno(@Path("alumno")cedula_alumno: String): Response<Void>

}