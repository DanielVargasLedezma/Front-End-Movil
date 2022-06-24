package com.example.app_matricula_movil.data.repository

import com.example.app_matricula_movil.data.models.grupo.Grupo
import com.example.app_matricula_movil.data.network.service.GrupoService
import com.example.app_matricula_movil.data.responses.grupo.GetGruposDeCursoResponse

class GrupoRepository {
    private val grupoService = GrupoService()

    suspend fun getGruposDeCurso(id_curso: String, token: String): GetGruposDeCursoResponse? =
        grupoService.getGruposDeCurso(id_curso, token)

    suspend fun getGruposDeProfesor(cedula_profesor: String, token: String): GetGruposDeCursoResponse? =
        grupoService.getGruposDeProfesor(cedula_profesor, token)

    suspend fun getGruposMatriculadosDeAlumno(cedula_alumno: String, token: String): GetGruposDeCursoResponse? =
        grupoService.getGruposMatriculadosDeAlumno(cedula_alumno, token)

    suspend fun getGruposDeCarrera(codigo_carrera: String, token: String): GetGruposDeCursoResponse? =
        grupoService.getGruposDeCarrera(codigo_carrera, token)

    suspend fun insertarGrupo(grupo: Grupo, token: String): Boolean = grupoService.insertarGrupo(grupo, token)

    suspend fun editarGrupo(grupo: Grupo, token: String): Boolean = grupoService.editarGrupo(grupo, token)

    suspend fun eliminarGrupo(numero_grupo: String, token: String): Boolean =
        grupoService.eliminarGrupo(numero_grupo, token)
}