package com.example.app_matricula_movil.data.models

import com.google.gson.annotations.SerializedName

/*
 * Otro data class porque solo se pretende almacenar datos, no tener métodos complejos. También usa SerializedName para
 * matchear las keys con la respuesta JSON en el GSONConverter. Es serializable por ambos métodos para pasarlo por los
 * intent.
 */
@kotlinx.serialization.Serializable
data class Usuario(
    @SerializedName("cedula_usuario") val cedula_usuario: String,
    @SerializedName("clave") var clave: String? = "",
    @SerializedName("tipo_usuario") val tipo_usuario: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("estado") val estado: Int,
    @SerializedName("correoE") val correo: String
) : java.io.Serializable
