package com.example.app_matricula_movil.domain.useCase

import com.example.app_matricula_movil.data.repository.UsuarioRepository
import com.example.app_matricula_movil.data.responses.GetUsuariosResponse

class GetUsuarios {
    private val repository = UsuarioRepository()

    suspend operator fun invoke(token: String) : GetUsuariosResponse? = repository.getUsuarios(token)
}