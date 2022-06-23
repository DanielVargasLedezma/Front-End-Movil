package com.example.app_matricula_movil.data.network.controller

import com.example.app_matricula_movil.data.models.matricula.Matricula
import com.example.app_matricula_movil.data.responses.matricula.GetMatricula
import com.example.app_matricula_movil.data.responses.matricula.GetMatriculasAlumnoResponse
import retrofit2.Response
import retrofit2.http.*

interface MatriculaController {
    @GET("alumno/matriculas/{alumno}")
    suspend fun getMatriculasAlumno(@Path("alumno") cedula_alumno: String): Response<GetMatriculasAlumnoResponse>

    @GET("matricula/{cedula_alumno}/{numero_grupo}")
    suspend fun getMatriculaAlumno(
        @Path("cedula_alumno") cedula_alumno: String,
        @Path("numero_grupo") numero_grupo: String
    ): Response<GetMatricula>

    @POST("crear-matricula")
    suspend fun crearMatricula(@Body matricula: Matricula): Response<Void>

    @POST("registrar-nota/{matricula}")
    suspend fun registrarNota(@Body matricula: Matricula, @Path("matricula") codigo_matricula: Int): Response<Void>

    @DELETE("desmatricular/{cedula_alumno}/{numero_grupo}")
    suspend fun desmatricularGrupo(
        @Path("cedula_alumno") cedula_alumno: String,
        @Path("numero_grupo") numero_grupo: String
    ): Response<Void>
}