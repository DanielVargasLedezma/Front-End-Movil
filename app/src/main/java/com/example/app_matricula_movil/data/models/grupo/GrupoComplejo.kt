package com.example.app_matricula_movil.data.models.grupo

import com.example.app_matricula_movil.data.models.ciclo.Ciclo
import com.example.app_matricula_movil.data.models.curso.Curso
import com.example.app_matricula_movil.data.models.curso.CursoComplejo
import com.example.app_matricula_movil.data.models.profesor.Profesor
import com.google.gson.annotations.SerializedName

data class GrupoComplejo(
    @SerializedName("numero_grupo") var numero_grupo: String = "",
    @SerializedName("curso") var curso: CursoComplejo,
    @SerializedName("profesor") var profesor: Profesor,
    @SerializedName("ciclo") var ciclo: Ciclo,
    @SerializedName("horario") var horario: String = "",
    @SerializedName("dia_uno") var dia_uno: String = "",
    @SerializedName("dia_dos") var dia_dos: String = "",
): java.io.Serializable
