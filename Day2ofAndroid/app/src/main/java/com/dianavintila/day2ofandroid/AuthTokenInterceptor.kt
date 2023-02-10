package com.dianavintila.day2ofandroid

import okhttp3.Interceptor
import okhttp3.Response

class AuthTokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .header("Authorization", "AuthToken")
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}

/*
*
* Refreshing the Access Token at Single Place
 override fun intercept(chain: Interceptor.Chain): Response {
    val accessToken = //our access Token
    val request = chain.request().newBuilder()
        .addHeader("Authorization", accessToken)
        .build()
    val response = chain.proceed(request)
    if (response.code() == 401) {
            val newToken: String = //fetch from some other source
            if (newToken != null) {
               val newRequest =  chain.request().newBuilder()
                    .addHeader("Authorization", newToken)
                    .build()
                return chain.proceed(newRequest)
            }
    }
    return response
}
* */