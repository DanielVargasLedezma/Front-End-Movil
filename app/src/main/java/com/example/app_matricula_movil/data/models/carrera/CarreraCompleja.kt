package com.example.app_matricula_movil.data.models.carrera

import com.example.app_matricula_movil.data.models.curso.CursoComplejo
import com.google.gson.annotations.SerializedName

data class CarreraCompleja(
    @SerializedName("codigo_carrera") var codigo_carrera: String,
    @SerializedName("nombre") var nombre: String,
    @SerializedName("titulo") var titulo: String,
    @SerializedName("cursos") var cursos: ArrayList<CursoComplejo>,
) : java.io.Serializable