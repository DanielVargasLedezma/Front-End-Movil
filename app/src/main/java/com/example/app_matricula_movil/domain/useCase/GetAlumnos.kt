package com.example.app_matricula_movil.domain.useCase

import com.example.app_matricula_movil.data.repository.AlumnoRepository
import com.example.app_matricula_movil.data.responses.GetAlumnosResponse


class GetAlumnos {
    private val repository = AlumnoRepository()

    suspend operator fun invoke(token: String) : GetAlumnosResponse? = repository.getAlumnos(token)
}