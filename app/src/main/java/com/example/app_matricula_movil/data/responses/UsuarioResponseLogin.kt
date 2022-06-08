package com.example.app_matricula_movil.data.responses

import com.example.app_matricula_movil.data.models.Usuario
import com.google.gson.annotations.SerializedName

/*
 * Data class es un tipo de clase que busca almacenar datos. Se usa el SerializedName para darle el valor con el que va
 * a matchear en una respuesta JSON. Nótese que hasta el objeto complejo Usuario tiene uno de estos indicadores.
 * Este tipo de respuesta se pone así porque así es como responde el API a esta petición. Lo que implica que el cómo se
 * va a ver una respuesta custom varía y no es 100% reusable.
 */
data class UsuarioResponseLogin(
    @SerializedName("token") val token: String,
    @SerializedName("usuario") val usuario: Usuario
)
