package com.example.app_matricula_movil.domain.useCase

import com.example.app_matricula_movil.data.repository.UsuarioRepository
import com.example.app_matricula_movil.data.responses.UsuarioResponseLogin

class DoLogin {

    /*
     * Es una instancia del repositorio que es la instancia que provee los datos a toda la aplicación, aquí van los varios
     * métodos relacionados con los usuarios. Deberían existir X repositorios para cada tipo de instancia y X casos de uso,
     * aunque no sé qué tan divertido sea hacer diez mil casos de uso. Igual es como hacer métodos dentro de un controller
     * en JS.
     */
    private val repository = UsuarioRepository()

    /*
     * Una vez más, es una función SUSPEND que indica que necesita un entorno asíncrono. Es un operador y se llama invoke
     * porque es lo que se va a ejecutar cuando se le llame al "nombreDeInstancia"() como en el View Model. Además, como
     * es de una solo línea se puede igualar a lo que va a retornar para que se vea ELEGANTE (Juan de Dios estaría
     * orgulloso). Además, ver que retorna un UsuarioResponseLogin"?", lo cual indica que puede que sea nulo. Puede ver
     * la respuesta si quiere de una o verla después en la interfaz del controller en data.network.controller.
     */
    suspend operator fun invoke(cedula: String, password: String, token: String) : UsuarioResponseLogin? = repository.login(cedula, password, token)
}