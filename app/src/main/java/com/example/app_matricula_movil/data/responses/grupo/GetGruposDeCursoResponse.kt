package com.example.app_matricula_movil.data.responses.grupo

import com.example.app_matricula_movil.data.models.grupo.GrupoComplejo
import com.google.gson.annotations.SerializedName

data class GetGruposDeCursoResponse(@SerializedName("data") val grupos: ArrayList<GrupoComplejo>)
