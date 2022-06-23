package com.example.app_matricula_movil.data.models.alumno

import com.example.app_matricula_movil.data.models.carrera.Carrera
import com.google.gson.annotations.SerializedName

data class AlumnoComplejo(
    @SerializedName("cedula_alumno") var cedula_alumno: String = "",
    @SerializedName("clave") var clave: String = "",
    @SerializedName("nombre") var nombre: String = "",
    @SerializedName("telefono") var telefono: Int = 0,
    @SerializedName("correoe") var correo: String = "",
    @SerializedName("carrera") var carrera: Carrera,
    @SerializedName("fecha_nacimiento") var fecha_nacimiento: String = "",
) : java.io.Serializable
