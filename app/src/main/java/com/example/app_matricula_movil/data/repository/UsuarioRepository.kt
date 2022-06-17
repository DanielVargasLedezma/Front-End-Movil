package com.example.app_matricula_movil.data.repository

import com.example.app_matricula_movil.data.models.Usuario
import com.example.app_matricula_movil.data.network.service.UsuarioService
import com.example.app_matricula_movil.data.responses.usuario.GetUsuariosResponse
import com.example.app_matricula_movil.data.responses.UsuarioResponseLogin

class UsuarioRepository {

    /*
     * Usa el servicio, como otra cadena más para realizar la conexión con el API. No tiene mucha lógica, solo es como
     * se supone que funcan las capas de una arquitectura "limpia". En este repositorio se podrían meter todos los métodos
     * relacionados con los Usuarios que conectaría con los propios métodos del servicio.
     */
    private val usuarioService = UsuarioService()

    /*
     * Método SUSPEND pero esta vez normal, solo llama al método de login del Servicio, igual que en el Caso de Uso.
     */
    suspend fun login(cedula: String, password: String, token: String): UsuarioResponseLogin? =
        usuarioService.login(cedula, password, token)

    suspend fun getUsuarios(token: String): GetUsuariosResponse? = usuarioService.getUsuarios(token)

    suspend fun eliminarUsuario(cedula_usuario: String, token: String): Boolean =
        usuarioService.eliminarUsuario(cedula_usuario, token)

    suspend fun insertarUsuario(usuario: Usuario, token: String): Boolean =
        usuarioService.insertarUsuario(usuario, token)

    suspend fun editarUsuario(usuario: Usuario, token: String): Boolean = usuarioService.editarUsuario(usuario, token)

    suspend fun logout(token: String) : Boolean = usuarioService.logout(token)
}