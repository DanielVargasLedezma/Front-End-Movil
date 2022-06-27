package com.example.app_matricula_movil.data.network.service

import android.util.Log
import com.example.app_matricula_movil.core.RetrofitHelper
import com.example.app_matricula_movil.data.models.alumno.Alumno
import com.example.app_matricula_movil.data.network.controller.AlumnoController
import com.example.app_matricula_movil.data.responses.AlumnoResponseLogin
import com.example.app_matricula_movil.data.responses.alumno.GetAlumnosResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlumnoService {
    /*
     * El método principal, aquí se usa el retrofit helper. Esto es un objeto custom que viene siendo como un axios pero
     * un poco más complejo de explicar, pero para usarlo solo hay que usar el getRetrofit que es el método del objeto
     * custom. El getRetrofit necesita el token, el detallazo es que creo que está creando la instancia cada vez que
     * se invoca el método.
     */
    suspend fun login(cedula: String, password: String, token: String): AlumnoResponseLogin? {
        return withContext(Dispatchers.IO) {
            /*
             * Obtiene la instancia de Retrofit, luego crea un API conexión con la interfaz que se le brinda. Este tendrá
             * todos los métodos que se declaren en la interfaz, donde se hará una petición al enlace que se declare y
             * requirirá los elementos que se indiquen (body, queries -> parámetros en el url, entre otros).
             */
            val response = RetrofitHelper.getRetrofit(token)
                .create(AlumnoController::class.java)
                .login(Alumno(cedula, password))

            /*
             * Si la respuesta es efectiva, significa que el API no tiró algún tipo de error; si esto se da, el cuerpo
             * de la respuesta va a ser el tipo de respuesta que se declare en el método de la interfaz (AlumnoResponseLogin).
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
                Log.v("MainActivity", "$response")
                null
            }
        }
    }

    suspend fun getAlumnos(token: String): GetAlumnosResponse?{
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(AlumnoController::class.java)
                .getAlumnos()

            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }

    }

    suspend fun registrarAlumno(alumno: Alumno, token: String): Boolean{
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(AlumnoController::class.java)
                .registrarAlumno(alumno)

            if (response.isSuccessful) {
                true
            } else {
                Log.v("MainActivity", "$response")
                false
            }
        }

    }
    suspend fun editarAlumno(alumno: Alumno, token: String): Boolean{
        return withContext(Dispatchers.IO){
            val response = RetrofitHelper.getRetrofit(token)
                .create(AlumnoController::class.java)
                .editarAlumno(alumno, alumno.cedula_alumno)

            if (response.isSuccessful) {
                true
            } else {
                Log.v("MainActivity", "$response")
                false
            }

        }

    }
    suspend fun eliminarAlumno(cedula_alumno: String, token: String): Boolean{
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.getRetrofit(token)
                .create(AlumnoController::class.java)
                .eliminarAlumno(cedula_alumno)

            if (response.isSuccessful) {
                true
            } else {
                Log.v("MainActivity", "$response")
                false
            }
        }
    }

}