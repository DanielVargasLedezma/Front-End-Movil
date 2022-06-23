package com.example.app_matricula_movil.data.models.matricula

import com.google.gson.annotations.SerializedName

data class Matricula(
    @SerializedName("cedula_alumno") var cedula_alumno: String = "",
    @SerializedName("numero_grupo") var numero_grupo: String = "",
    @SerializedName("nota") var nota: Int = 0,
    @SerializedName("numero_matricula") var numero_matricula: Int = 0,
): java.io.Serializable
