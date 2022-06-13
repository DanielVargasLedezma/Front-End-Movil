package com.example.app_matricula_movil.data.models

import com.google.gson.annotations.SerializedName

/*
 * Otro data class porque solo se pretende almacenar datos, no tener métodos complejos. También usa SerializedName para
 * matchear las keys con la respuesta JSON en el GSONConverter. Es serializable por ambos métodos para pasarlo por los
 * intent.
 */
@kotlinx.serialization.Serializable
data class Usuario(
    @SerializedName("cedula_usuario") var cedula_usuario: String = "",
    @SerializedName("clave") var clave: String? = "",
    @SerializedName("tipo_usuario") var tipo_usuario: Int = 0,
    @SerializedName("nombre") var nombre: String = "",
    @SerializedName("estado") var estado: Int = 0,
    @SerializedName("correoE") var correo: String = ""
) : java.io.Serializable
