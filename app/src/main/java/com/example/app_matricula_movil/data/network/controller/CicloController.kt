package com.example.app_matricula_movil.data.network.controller

import com.example.app_matricula_movil.data.models.ciclo.Ciclo
import com.example.app_matricula_movil.data.responses.ciclo.GetCiclosResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CicloController {
    @GET("ciclos")
    suspend fun getCiclos(): Response<GetCiclosResponse>

    @POST("crear-ciclo")
    suspend fun insertarCiclo(@Body ciclo: Ciclo): Response<Void>

    @POST("ciclo/editar/{ciclo}")
    suspend fun editarCiclo(@Body ciclo: Ciclo, @Path("ciclo") id_ciclo: Int): Response<Void>

    @DELETE("ciclo/eliminar/{ciclo}")
    suspend fun eliminarCiclo(@Path("ciclo") id_ciclo: Int): Response<Void>


}