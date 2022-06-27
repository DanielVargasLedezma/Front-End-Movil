package com.example.app_matricula_movil.data.responses.alumno


import com.example.app_matricula_movil.data.models.alumno.AlumnoComplejo
import com.google.gson.annotations.SerializedName

data class GetAlumnosResponse(@SerializedName("data") val alumnos: ArrayList<AlumnoComplejo>)