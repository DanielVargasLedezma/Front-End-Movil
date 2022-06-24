package com.example.app_matricula_movil.data.network.controller

import com.example.app_matricula_movil.data.models.grupo.Grupo
import com.example.app_matricula_movil.data.models.matricula.Matricula
import com.example.app_matricula_movil.data.responses.grupo.GetGruposDeCursoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GrupoController {
    @GET("grupos/curso/{curso}")
    suspend fun getGruposDeCurso(@Path("curso") id_curso: String): Response<GetGruposDeCursoResponse>

    @GET("grupos/profesor/{profesor}")
    suspend fun getGruposDeProfesor(@Path("profesor") cedula_usuario: String): Response<GetGruposDeCursoResponse>

    @GET("grupos/alumno/{alumno}")
    suspend fun getGruposMatriculadosDeAlumno(@Path("alumno") cedula_alumno: String): Response<GetGruposDeCursoResponse>

    @GET("grupos/carrera/{carrera}")
    suspend fun getGruposDeCarrera(@Path("carrera") codigo_carrera: String): Response<GetGruposDeCursoResponse>

    @POST("crear-grupo")
    suspend fun insertarGrupo(@Body grupo: Grupo): Response<Void>

    @POST("grupos/editar/{grupo}")
    suspend fun editarGrupo(@Body grupo: Grupo, @Path("grupo") id_grupo: String): Response<Void>

    @DELETE("grupo/eliminar/{grupo}")
    suspend fun eliminarGrupo(@Path("grupo") numero_grupo: String): Response<Void>
}