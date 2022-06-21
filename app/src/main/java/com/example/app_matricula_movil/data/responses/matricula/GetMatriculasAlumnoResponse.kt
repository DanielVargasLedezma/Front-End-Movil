package com.example.app_matricula_movil.data.responses.matricula

import com.example.app_matricula_movil.data.models.matricula.MatriculaCompleja
import com.google.gson.annotations.SerializedName

data class GetMatriculasAlumnoResponse(@SerializedName("data") val matriculas: ArrayList<MatriculaCompleja>)
