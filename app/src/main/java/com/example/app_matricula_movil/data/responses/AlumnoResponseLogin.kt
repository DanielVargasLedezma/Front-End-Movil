package com.example.app_matricula_movil.data.responses

import com.example.app_matricula_movil.data.models.alumno.Alumno
import com.google.gson.annotations.SerializedName

data class AlumnoResponseLogin(@SerializedName("alumno") val alumno: Alumno, @SerializedName("token") val token: String)