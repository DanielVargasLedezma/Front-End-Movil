package com.example.app_matricula_movil.data.repository

import com.example.app_matricula_movil.data.models.curso.Curso
import com.example.app_matricula_movil.data.network.service.CursoService
import com.example.app_matricula_movil.data.responses.curso.GetCursosResponse

class CursoRepository {
    private val cursoService = CursoService()

    suspend fun getCursos(token: String): GetCursosResponse? = cursoService.getCursos(token)

    suspend fun insertarCurso(curso: Curso, token: String): Boolean = cursoService.insertarCurso(curso, token)

    suspend fun editarCurso(curso: Curso, token: String): Boolean = cursoService.editarCurso(curso, token)

    suspend fun eliminarCurso(codigo_curso: String, token: String): Boolean =
        cursoService.eliminarCurso(codigo_curso, token)
}