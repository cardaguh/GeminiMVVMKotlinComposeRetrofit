package com.example.rickandmorty.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Object to create and configure the Retrofit instance.
object RetrofitClient {
    // Base URL of the Rick and Morty API.
    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    // Creates an OkHttpClient with a logging interceptor for debugging.
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    // Creates and configures the Retrofit instance.
    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}