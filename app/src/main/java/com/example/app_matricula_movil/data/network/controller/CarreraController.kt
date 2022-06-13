package com.example.app_matricula_movil.data.network.controller

import com.example.app_matricula_movil.data.responses.GetCarrerasResponse
import retrofit2.Response
import retrofit2.http.GET

interface CarreraController {
    @GET("carreras")
    suspend fun getCarreras(): Response<GetCarrerasResponse>
}