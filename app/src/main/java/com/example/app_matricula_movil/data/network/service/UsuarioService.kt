package com.example.app_matricula_movil.data.network.service

import android.util.Log
import com.example.app_matricula_movil.core.RetrofitHelper
import com.example.app_matricula_movil.data.models.Usuario
import com.example.app_matricula_movil.data.network.controller.UsuarioController
import com.example.app_matricula_movil.data.responses.usuario.GetUsuariosResponse
import com.example.app_matricula_movil.data.responses.UsuarioResponseLogin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsuarioService {
    /*
     * El método principal, aquí se usa el retrofit helper. Esto es un objeto custom que viene siendo como un axios pero
     * un poco más complejo de explicar, pero para usarlo solo hay que usar el getRetrofit que es el método del objeto
     * custom. El getRetrofit necesita el token, el detallazo es que creo que está creando la instancia cada vez que
     * se invoca el método.
     */
    suspend fun login(cedula: String, password: String, token: String): UsuarioResponseLogin? {
        return withContext(Dispatchers.IO) {
            /*
             * Obtiene la instancia de Retrofit, luego crea un API conexión con la interfaz que se le brinda. Este tendrá
             * todos los métodos que se declaren en la interfaz, donde se hará una petición al enlace que se declare y
             * requirirá los elementos que se indiquen (body, queries -> parámetros en el url, entre otros).
             */
            val response = RetrofitHelper.getRetrofit(token)
                .create(UsuarioController::class.java)
                .login(Usuario(cedula, password))

            /*
             * Si la respuesta es efectiva, significa que el API no tiró algún tipo de error; si esto se da, el cuerpo
             * de la respuesta va a ser el tipo de respuesta que se declare en el método de la interfaz (UsuarioResponseLogin).
             */
            if (response.isSuccessful) {
                /*
                 * Esto equivale a retornar el cuerpo de la respuesta.
                 */
                response.body()
            } else {
                /*
                 * Esto equivale a retornar null, para hacer algo en el inicio de la cadena.
                 */
                Log.v("MainActivity", "${response.errorBody()}")
                null
            }
        }
    }

    suspend fun getUsuarios(token: String): GetUsuariosResponse? {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(UsuarioController::class.java)
                .getUsuarios()

            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }
    }

    suspend fun insertarUsuario(user: Usuario, token: String): Boolean {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(UsuarioController::class.java)
                .insertarUsuario(user)

            if (response.isSuccessful) {
                true
            } else {
                Log.v("MainActivity", "$response")
                false
            }
        }
    }

    suspend fun editarUsuario(user: Usuario, token: String): Boolean {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(UsuarioController::class.java)
                .editarUsuario(user, user.cedula_usuario)

            if (response.isSuccessful) {
                true
            } else {
                Log.v("MainActivity", "$response")
                false
            }
        }
    }

    suspend fun eliminarUsuario(cedula_usuario: String, token: String): Boolean {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(UsuarioController::class.java)
                .eliminarUsuario(cedula_usuario)

            if (response.isSuccessful) {
                true
            } else {
                Log.v("MainActivity", "$response")
                false
            }
        }
    }

    suspend fun logout(token: String): Boolean {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(UsuarioController::class.java)
                .logout()

            response.isSuccessful
        }
    }
}