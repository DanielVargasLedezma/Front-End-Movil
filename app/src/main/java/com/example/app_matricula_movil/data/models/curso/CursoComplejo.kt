package com.example.app_matricula_movil.data.models.curso

import com.example.app_matricula_movil.data.models.carrera.Carrera
import com.google.gson.annotations.SerializedName

data class CursoComplejo(
    @SerializedName("codigo_curso") var codigo_curso: String,
    @SerializedName("carrera") var carrera: Carrera,
    @SerializedName("nombre") var nombre: String,
    @SerializedName("creditos") var creditos: Int,
    @SerializedName("horas_semanales") var horas_semanales: Int,
    @SerializedName("num_semestre_a_llevar") var num_semestre_a_llevar: Int,
    @SerializedName("anyo_a_llevar") var anyo_a_llevar: String,
) : java.io.Serializable