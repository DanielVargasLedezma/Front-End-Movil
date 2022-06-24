package com.example.app_matricula_movil.data.repository

import com.example.app_matricula_movil.data.network.service.CicloService
import com.example.app_matricula_movil.data.responses.ciclo.GetCiclosResponse

class CicloRepository {
    private val cicloService = CicloService()

    suspend fun getCiclos(token: String): GetCiclosResponse? = cicloService.getCiclos(token)
}