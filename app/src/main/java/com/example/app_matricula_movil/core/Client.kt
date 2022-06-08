package com.example.app_matricula_movil.core

import com.example.app_matricula_movil.core.interceptors.HeaderInterceptor
import okhttp3.OkHttpClient

/*
 * Un objeto para no tener que crear una instancia. Está personalizado porque recibe un token para el header interceptor,
 * que es añadido cuando se construye el OkHttpClient. No tiene mucha explicación.
 */
object Client {
    fun getClient(token: String): OkHttpClient {
        val interceptor = HeaderInterceptor()
        interceptor.token = token

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }
}