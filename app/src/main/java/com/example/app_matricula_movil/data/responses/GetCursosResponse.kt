package com.example.app_matricula_movil.data.responses

import com.example.app_matricula_movil.data.models.curso.CursoComplejo
import com.google.gson.annotations.SerializedName

data class GetCursosResponse(@SerializedName("data") val cursos: ArrayList<CursoComplejo>)