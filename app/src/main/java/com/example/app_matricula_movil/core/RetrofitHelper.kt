package com.example.app_matricula_movil.core

import com.example.app_matricula_movil.api_url.Url
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
 * Un objeto para no tener que crear alguna instancia. Es lo que nos da Retrofit (El Axios de móvil). Está customizado
 * para que reciba un toque que le pasa al cliente, el cliente es el que tiene interceptores que son los headers. Es para
 * darle información adicional a las peticiones. Usa el ConverterFactory para hacer parseo directo según los nombres
 * serializados elegidos y las keys del JSON de la respuesta. Usa el BASEURL del API que está declarado por aparte.
 */
object RetrofitHelper {
    fun getRetrofit(token: String) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(Url.apiUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(Client.getClient(token))
            .build()
    }
}