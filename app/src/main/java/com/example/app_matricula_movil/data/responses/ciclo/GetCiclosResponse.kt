package com.example.app_matricula_movil.data.responses.ciclo

import com.example.app_matricula_movil.data.models.ciclo.Ciclo
import com.google.gson.annotations.SerializedName

data class GetCiclosResponse(@SerializedName("data") val ciclos: ArrayList<Ciclo>)
