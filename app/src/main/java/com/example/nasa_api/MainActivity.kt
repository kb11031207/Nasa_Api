package com.example.nasa_api


import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler.JSON
import com.example.nasa_api.databinding.ActivityMainBinding
import okhttp3.Headers

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var nasaAdapter: NasaAdapter
    private val nasaImages = mutableListOf<NasaImage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Setup RecyclerView
        nasaAdapter = NasaAdapter(nasaImages)
        binding.nasaList.apply {
            adapter = nasaAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        // Load initial images
        getImages()

        binding.ChangeImage.setOnClickListener {
            getImages()
        }
    }


    private fun getImages() {
        val client = AsyncHttpClient()
        val param = RequestParams()
        param["api_key"] = "j1TdnUIMVv6IP2VEcbuXLUBageXKdVg5mV6rvzJu"
        param["count"] = "10" // Request 10 random images

        client.get("https://api.nasa.gov/planetary/apod", param, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                try {
                    val jsonArray = json.jsonArray
                    val newImages = mutableListOf<NasaImage>()
                    
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        
                        val title = jsonObject.getString("title")
                        val date = jsonObject.getString("date")
                        val url = jsonObject.getString("url")
                        val explanation = jsonObject.getString("explanation")
                        val mediaType = jsonObject.optString("media_type", "image")
                        
                        // Only add images, skip videos for now
                        if (mediaType == "image") {
                            newImages.add(NasaImage(title, date, url, explanation, mediaType))
                        }
                    }
                    
                    // Update the RecyclerView
                    nasaImages.clear()
                    nasaImages.addAll(newImages)
                    nasaAdapter.updateData(nasaImages)
                    
                    Log.d("NASA", "Loaded ${newImages.size} images")
                    
                } catch (e: Exception) {
                    Log.e("NASA", "Error parsing JSON: ${e.message}")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                t: Throwable?
            ) {
                Log.e("NASA", "Error: $errorResponse")
            }
        })
    }
}
