package com.example.app_matricula_movil.data.models.matricula

import com.google.gson.annotations.SerializedName

data class Matricula(
    @SerializedName("cedula_alumno") var cedula_alumno: String = "",
    @SerializedName("numero_grupo") var numero_grupo: String = ""
): java.io.Serializable
