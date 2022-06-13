package com.example.app_matricula_movil.data.responses

import com.example.app_matricula_movil.data.models.profesor.Profesor
import com.google.gson.annotations.SerializedName

data class ProfesorResponseLogin(
    @SerializedName("profesor") val profesor: Profesor,
    @SerializedName("token") val token: String
)