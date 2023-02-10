package com.dianavintila.day2ofandroid

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ForceCacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.v("Main", "force cache")
        val builder: Request.Builder = chain.request().newBuilder()
        if (!MainActivity.appContext.isNetworkConnected) {
            builder.cacheControl(CacheControl.FORCE_CACHE);
        }
        return chain.proceed(builder.build());
    }

    private val Context.isNetworkConnected: Boolean
        get() {
            val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return manager.getNetworkCapabilities(manager.activeNetwork)?.let {
                it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        it.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) ||
                        it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                        it.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
            } ?: false
        }
}