package com.example.app_matricula_movil.data.models.profesor

import com.google.gson.annotations.SerializedName

data class Profesor(
    @SerializedName("cedula_profesor") var cedula_profesor: String = "",
    @SerializedName("clave") var clave: String = "",
    @SerializedName("nombre") var nombre: String = "",
    @SerializedName("telefono") var telefono: Int = 0,
    @SerializedName("correo") var correo: String = "",
) : java.io.Serializable
