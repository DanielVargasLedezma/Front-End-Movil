package com.example.app_matricula_movil.data.network.controller

import com.example.app_matricula_movil.data.models.curso.Curso
import com.example.app_matricula_movil.data.responses.GetCursosResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CursoController {
    @GET("cursos")
    suspend fun getCursos(): Response<GetCursosResponse>

    @POST("crear-cursos")
    suspend fun insertarCurso(@Body curso: Curso): Response<Void>

    @POST("curso/editar/{curso}")
    suspend fun editarCurso(@Body curso: Curso, @Path("curso") codigo_curso: String): Response<Void>

    @DELETE("curso/eliminar/{curso}")
    suspend fun eliminarCurso(@Path("curso") codigo_curso: String): Response<Void>
}