package com.example.app_matricula_movil.data.models.carrera

import com.google.gson.annotations.SerializedName

data class Carrera(
    @SerializedName("codigo_carrera") var codigo_carrera: String,
    @SerializedName("nombre") var nombre: String,
    @SerializedName("titulo") var creditos: String,
) : java.io.Serializable
