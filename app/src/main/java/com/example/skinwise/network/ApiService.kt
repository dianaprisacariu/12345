package com.example.skinwise.network

import com.example.skinwise.model.ProductResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("cgi/search.pl")
    fun searchProducts(
        @Query("search_terms") query: String,
        @Query("search_simple") searchSimple: Int = 1,
        @Query("action") action: String = "process",
        @Query("json") json: Int = 1
    ): Call<ProductResponse>
}
