package com.example.app_matricula_movil.data.models.grupo

import com.google.gson.annotations.SerializedName

data class Grupo(
    @SerializedName("numero_grupo") var numero_grupo: String = "",
    @SerializedName("codigo_curso") var codigo_curso: String = "",
    @SerializedName("cedula_profesor") var cedula_profesor: String = "",
    @SerializedName("id_ciclo") var id_ciclo: Int = 0,
    @SerializedName("horario") var horario: String = "",
    @SerializedName("dia_uno") var dia_uno: String = "",
    @SerializedName("dia_dos") var dia_dos: String = "",
) : java.io.Serializable
