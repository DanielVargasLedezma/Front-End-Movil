package com.example.app_matricula_movil.data.responses

import com.example.app_matricula_movil.data.models.Usuario
import com.google.gson.annotations.SerializedName

data class GetUsuariosResponse(@SerializedName("data") val usuarios: ArrayList<Usuario>)
