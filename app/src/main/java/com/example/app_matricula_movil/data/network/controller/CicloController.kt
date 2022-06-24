package com.example.app_matricula_movil.data.network.controller

import com.example.app_matricula_movil.data.responses.ciclo.GetCiclosResponse
import retrofit2.Response
import retrofit2.http.GET

interface CicloController {
    @GET("ciclos")
    suspend fun getCiclos(): Response<GetCiclosResponse>
}