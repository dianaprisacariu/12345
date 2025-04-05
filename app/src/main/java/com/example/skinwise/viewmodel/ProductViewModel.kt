package com.example.skinwise.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.skinwise.model.Product
import com.example.skinwise.model.ProductResponse
import com.example.skinwise.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG: String = "CHECK_RESPONSE"
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    // Funcția pentru a căuta produse folosind termenul de căutare
    fun searchProducts(query: String) {
        val call = RetrofitClient.apiService.searchProducts(query)

        call.enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "API Response Success: ${response.body()}")
                    _products.value = response.body()?.products ?: emptyList()
                } else {
                    Log.e(TAG, "API Response Error: ${response.errorBody()?.string()}")
                    _products.value = emptyList()
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Log.e(TAG, "API Failure: ${t.message}")
                _products.value = emptyList()
            }
        })
    }

}