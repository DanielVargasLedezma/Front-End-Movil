package com.example.app_matricula_movil.data.repository

import com.example.app_matricula_movil.data.models.ciclo.Ciclo
import com.example.app_matricula_movil.data.network.service.CicloService
import com.example.app_matricula_movil.data.responses.ciclo.GetCiclosResponse

class CicloRepository {
    private val cicloService = CicloService()

    suspend fun getCiclos(token: String): GetCiclosResponse? = cicloService.getCiclos(token)

    suspend fun insertarCiclo(ciclo: Ciclo, token: String): Boolean =
        cicloService.insertarCiclo(ciclo, token)

    suspend fun editarCiclo(ciclo: Ciclo, token: String): Boolean =
        cicloService.editarCiclo(ciclo, token)

    suspend fun eliminarCiclo(id_ciclo: Int, token: String): Boolean =
        cicloService.eliminarCiclo(id_ciclo, token)
}