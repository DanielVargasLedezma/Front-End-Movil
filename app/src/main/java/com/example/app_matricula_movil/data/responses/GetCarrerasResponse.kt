package com.example.app_matricula_movil.data.responses

import com.example.app_matricula_movil.data.models.carrera.CarreraCompleja
import com.google.gson.annotations.SerializedName

data class GetCarrerasResponse(@SerializedName("data") val carreras: ArrayList<CarreraCompleja>)
