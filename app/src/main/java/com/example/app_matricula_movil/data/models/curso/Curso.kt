package com.example.app_matricula_movil.data.models.curso

import com.google.gson.annotations.SerializedName

data class Curso(
    @SerializedName("codigo_curso") var codigo_curso: String = "",
    @SerializedName("codigo_carrera") var codigo_carrera: String = "",
    @SerializedName("nombre") var nombre: String = "",
    @SerializedName("creditos") var creditos: Int = 0,
    @SerializedName("horas_semanales") var horas_semanales: Int = 0,
    @SerializedName("num_semestre_a_llevar") var num_semestre_a_llevar: Int = 0,
    @SerializedName("anyo_a_llevar") var anyo_a_llevar: String = "",
) : java.io.Serializable
