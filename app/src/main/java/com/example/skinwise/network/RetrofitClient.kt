package com.example.skinwise.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // URL-ul de bază al API-ului
    private const val BASE_URL = "https://world.openbeautyfacts.org/"


    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Setează URL-ul de bază
            .addConverterFactory(GsonConverterFactory.create()) // Folosește Gson pentru conversia datelor JSON
            .build()
            .create(ApiService::class.java) // Creează instanța ApiService
    }
}
