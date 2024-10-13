package com.example.labfinal.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Dung Pham 2217963 10.10.24
// Provides a singleton instance of Retrofit API client with base URL for remote data source and
// Gson converter for JSON conversion. Uses lazy delegation to initialize "api" when first accessed.
object RetrofitInstance {
    private const val BASE_URL = "https://users.metropolia.fi/~dungpha/"

    val api: MPApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MPApi::class.java)
    }
}