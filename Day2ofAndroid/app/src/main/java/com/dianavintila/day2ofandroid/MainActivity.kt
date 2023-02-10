package com.dianavintila.day2ofandroid

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.net.URL

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit  var appContext: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appContext = applicationContext

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Create URL
                val url =
                    URL("https://raw.githubusercontent.com/justmobiledev/android-kotlin-rest-1/main/support/data/bloginfo.json")
                val request = Request.Builder().url(url).build()
                val client = myHttpClient()
                // Execute request
                val response = client.newCall(request).execute()

                var result = response.body?.string()
                Log.v("Main", "result: $result")
            } catch (error: Error) {
                Log.e("Main", "Error when executing get request: " + error.localizedMessage)
            }
        }
    }

    fun myHttpClient(): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
            .addInterceptor(ErrorInterceptor())
            .cache(Cache(File(applicationContext.cacheDir, "http-cache"), 10L * 1024L * 1024L)) // 10 MiB
            .addNetworkInterceptor(CacheInterceptor())
            .addInterceptor(ForceCacheInterceptor())
        return builder.build()
    }


}