package com.example.app_matricula_movil.data.responses.profesor

import com.example.app_matricula_movil.data.models.profesor.Profesor
import com.google.gson.annotations.SerializedName

data class GetProfesoresResponse(@SerializedName("data") val profesores: ArrayList<Profesor>)
