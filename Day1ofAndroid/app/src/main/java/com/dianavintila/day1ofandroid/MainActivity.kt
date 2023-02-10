package com.dianavintila.day1ofandroid

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

class MainActivity : AppCompatActivity() {

    private var client: OkHttpClient = OkHttpClient()
    @Serializable
    data class BlogInfo(val title: String, val description: String)
    val viewModel: MainActivityViewModel by viewModels()
    lateinit var textViewTitle: TextView
    lateinit var textViewDescription: TextView
    lateinit var buttonFetch: Button
    lateinit var aolo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewTitle = findViewById(R.id.textview_blog_title)
        textViewDescription = findViewById(R.id.textview_blog_description)
        buttonFetch = findViewById(R.id.button_fetch)


        buttonFetch.setOnClickListener {
            Log.v("CEVA", "click")
            // Launch get request
            fetch("https://raw.githubusercontent.com/justmobiledev/android-kotlin-rest-1/main/support/data/bloginfo.json")
        }
        viewModel.title.observe(this, Observer {
            textViewTitle.text = it
        })
        viewModel.description.observe(this, Observer {
            textViewDescription.text = it
        })

    }


    private fun fetch(sUrl: String): BlogInfo? {
        var blogInfo: BlogInfo? = null
        lifecycleScope.launch(Dispatchers.IO) {
            val result = getRequest(sUrl)
            if (result != null) {
                try {
                    // Parse result string JSON to data class
                    blogInfo = Json.decodeFromString<BlogInfo>(result)
                    withContext(Dispatchers.Main) {
                        // Update view model
                        viewModel.title.value = blogInfo?.title
                        viewModel.description.value = blogInfo?.description
                    }
                }
                catch(err:Error) {
                    print("Error when parsing JSON: "+err.localizedMessage)
                }
            }
            else {
                print("Error: Get request returned no response")
            }
        }
        return blogInfo
    }

    private fun getRequest(sUrl: String): String? {
        var result: String? = null
        try {

            // Create URL
            val url = URL(sUrl)

            // Build request
            val request = Request.Builder().url(url).build()

            // Execute request
            val response = client.newCall(request).execute()

            result = response.body?.string()
        }
        catch(err:Error) {
            print("Error when executing get request: "+err.localizedMessage)
        }
        return result
    }
}