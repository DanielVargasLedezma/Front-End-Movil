package com.example.app_matricula_movil.data.models.ciclo

import com.google.gson.annotations.SerializedName

data class Ciclo(
    @SerializedName("id_ciclo") var id_ciclo: Int = 0,
    @SerializedName("numero_ciclo") var numero_ciclo: Int = 0,
    @SerializedName("year") var year: Int = 0,
    @SerializedName("fecha_inicio") var fecha_inicio: String = "",
    @SerializedName("fecha_finalizacion") var fecha_finalizacion: String = "",
    @SerializedName("ciclo_activo") var ciclo_activo: Int = 0,
): java.io.Serializable
