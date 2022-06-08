package com.example.app_matricula_movil.core.interceptors

import okhttp3.Interceptor
import okhttp3.Response

/*
 * Al necesitar atributos, es un objeto. Es el objeto o clase que pone los headers. En este caso, se incluyen con el
 * addHeader y en este caso se ponen dos. Luego retorna el Interceptor; nuevamente, no hay mucho detalle técnico aquí.
 */
class HeaderInterceptor : Interceptor {
    var token: String = ""

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().addHeader(
            "Accept",
            "application/json"
        )
            .addHeader(
                "Authorization",
                "Bearer $token"
            )
            .build()

        return chain.proceed(request)
    }
}