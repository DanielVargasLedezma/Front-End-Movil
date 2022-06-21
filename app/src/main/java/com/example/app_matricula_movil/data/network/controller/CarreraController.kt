package com.example.app_matricula_movil.data.network.controller

import com.example.app_matricula_movil.data.models.carrera.Carrera
import com.example.app_matricula_movil.data.responses.GetCarrerasResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CarreraController {
    @GET("carreras")
    suspend fun getCarreras(): Response<GetCarrerasResponse>

    @POST("crear-carrera")
    suspend fun insertarCarrera(@Body carrera: Carrera): Response<Void>

    @POST("carrera/editar/{carrera}")
    suspend fun editarCarrera(@Body carrera: Carrera, @Path("carrera") codigo_carrera: String): Response<Void>

    @DELETE("carrera/eliminar/{carrera}")
    suspend fun eliminarCarrera(@Path("carrera") codigo_carrera: String): Response<Void>
}