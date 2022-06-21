package com.example.app_matricula_movil.data.models.matricula

import com.example.app_matricula_movil.data.models.alumno.Alumno
import com.example.app_matricula_movil.data.models.grupo.GrupoComplejo
import com.example.app_matricula_movil.data.models.profesor.Profesor
import com.google.gson.annotations.SerializedName

data class MatriculaCompleja(
    @SerializedName("numero_matricula") var numero_matricula: Int,
    @SerializedName("alumno") var alumno: Alumno,
    @SerializedName("grupo") var grupo: GrupoComplejo,
    @SerializedName("nota") var nota: Int,
) : java.io.Serializable
