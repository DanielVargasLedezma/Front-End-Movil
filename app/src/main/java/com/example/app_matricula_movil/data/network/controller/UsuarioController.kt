package com.example.app_matricula_movil.data.network.controller

import com.example.app_matricula_movil.data.models.Usuario
import com.example.app_matricula_movil.data.responses.usuario.GetUsuariosResponse
import com.example.app_matricula_movil.data.responses.UsuarioResponseLogin
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UsuarioController {
    /*
     * Método POST al enlace que se detalle en los paréntesis. Se hace igual con los diferentes tipos de métodos. Este
     * método recibe un cuerpo que se especifica con el Body, sirve como el formData en js para pasar datos al backend.
     * Retorna una respuesta de un tipo T, gracias a GSON converter este encarga de matchear los serialized names con las
     * keys de la respuesta del JSON. Todo un capo el GSON.
     * Dato importante, es un método suspendido y aquí es donde se empieza la cadena de async await o suspend.
     */
    @POST("login/user")
    suspend fun login(@Body user: Usuario): Response<UsuarioResponseLogin>

    @GET("usuarios")
    suspend fun getUsuarios(): Response<GetUsuariosResponse>

    @POST("crear-usuario")
    suspend fun insertarUsuario(@Body user: Usuario): Response<Void>

    @POST("usuario/editar/{usuario}")
    suspend fun editarUsuario(@Body user: Usuario, @Path("usuario") id: String): Response<Void>

    @DELETE("usuario/eliminar/{usuario}")
    suspend fun eliminarUsuario(@Path("usuario") id: String): Response<Void>

    @DELETE("logout")
    suspend fun logout(): Response<Void>
}