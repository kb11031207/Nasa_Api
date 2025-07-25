package com.example.nasa_api

data class NasaImage(
    val title: String,
    val date: String,
    val url: String,
    val explanation: String,
    val mediaType: String = "image" // to handle both images and videos
) 