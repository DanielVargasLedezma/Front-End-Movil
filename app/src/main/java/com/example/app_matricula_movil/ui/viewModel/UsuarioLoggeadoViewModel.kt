package com.example.app_matricula_movil.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_matricula_movil.data.models.Usuario
import com.example.app_matricula_movil.domain.useCase.DoLogin

/*
 * Es una clase normal, pero hereda de ViewModel, una dependencia
 * que hace que se pueda comportar diferente.
 */
class UsuarioLoggeadoViewModel : ViewModel() {

    // Intento no terminado de Singleton xdxd.
//    companion object {
//        fun getInstance() {
//
//        }
//
//        private val instance = UsuarioLoggeadoViewModel()
//    }
//
//    private constructor() {
//
//    }

    /*
     * Esto es un caso de uso en domain.useCase. En buena teoría, es algo en específico que vale la pena tenerla ahí
     * para evitarse luego tener que realizar cambios en muchos lugares. Este método es el que usa más abajo y es donde
     * se hace la cadena de llamadas perronas.
     */
    private val doLogin = DoLogin()

    /*
     * Esto es el núcleo del View Model, Datos vivos mutables. Estos son del tipo que se le especifica dentro de los <>,
     * estos datos son observables y mutables, cuando se cambian se notifica o ejecuta el código dentro de los observadores
     * declarados en las actividades. Ahí es donde se puedan recuperar los datos nuevos que se pushearon.
     */
    val token = MutableLiveData<String>()
    val usuario = MutableLiveData<Usuario>()
    val loggedState = MutableLiveData<Boolean>()

    /*
     * Método xd para setear el token a un valor vacío no nulo para la petición del login que no usa token.
     */
    fun onCreate() {
        token.postValue("")
    }

    /*
     * La función que empieza la cadena de llamadas. Nótese que es una SUSPEND fun, lo cual significa que se debe ejecutar
     * en un hilo aparte. Viene siendo como un ASYNC on JS, pero ahí a uno le puede valer verga eso. Usa los dos parámetros
     * a evaluar y llama al caso de uso de arriba que hace la cadena de llamadas. Recomendado ir ahí para ver t odo el
     * entorno.
     */
    suspend fun login(cedula: String, password: String) {

        /*
         * Se iguala a un response, pues la cadena de métodos retorna una posible respuesta que es nula si no hubo éxito
         * o una respuesta con el cuerpo custom declarado en data.responses. Solo se llama como si fuese un constructor
         * por como está creado el caso de uso. Ver el caso de uso para más información
         */
        val response = doLogin(cedula, password, "")

        /*
         * Se valida si es nula o no, si lo es se pone la bandera en falso que eso notifica al observer en la actividad
         * y muestra el error, lo que evita que se inicie la siguiente actividad. De otra manera, se actualizan los datos
         * del usuario y token; lo que notifica los observers en la actividad y pone los datos en el intent. Nótese que
         * se postean los valores con el postValue y no se iguala, esto actualiza el dato dentro del dato vivo mutable con
         * lo que se le dé en los parámetros. Lo cual viene siendo los elementos del cuerpo de la respuesta custom. Luego
         * el estado de logged se pone en verdadero, aunque eso como que no hace nada xdxd. Luego de ahí se redirige a la
         * próxima vista.
         */
        if (response != null) {
            this.token.postValue(response.token)
            this.usuario.postValue(response.usuario)

            loggedState.postValue(true)
        } else {
            loggedState.postValue(false)
        }
    }
}