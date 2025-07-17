package com.example.nasa_api


import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler.JSON
import com.codepath.asynchttpclient.callback.TextHttpResponseHandler
import com.example.nasa_api.databinding.ActivityMainBinding
import okhttp3.Headers

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var first = true

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

        getImage()

        binding.ChangeImage.setOnClickListener {

            getImage()
        }
    }


    fun getImage() {
        val client = AsyncHttpClient()
        val param = RequestParams()
        param["api_key"] = "j1TdnUIMVv6IP2VEcbuXLUBageXKdVg5mV6rvzJu"
        if (!first){
            val day = (1..28).random() // keep it simple to avoid invalid dates
            val month = (1..12).random()
            val year = (1995..2024).random()

            val date = String.format("%04d-%02d-%02d", year, month, day)

            param["date"] = date
            first = false
        } else {
            first = false
        }


        client.get("https://api.nasa.gov/planetary/apod", param, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                val imageUrl = json.jsonObject.getString("url")
                val title = json.jsonObject.getString("title")
                val date = json.jsonObject.getString("date")
                val explanation = json.jsonObject.getString("explanation")

                Glide.with(this@MainActivity)
                    .load(imageUrl)
                    .into(binding.imageView)

                binding.TitleText.text = Editable.Factory.getInstance().newEditable(title)
                binding.DateText.text = date
                binding.Explanation.text = Editable.Factory.getInstance().newEditable(explanation)

                Log.d("NASA", "Loaded image: $imageUrl")
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
