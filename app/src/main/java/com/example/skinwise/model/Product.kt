package com.example.skinwise.model

data class Product(
    val id: String,
    val product_name: String,  // Numele produsului
    val brands: String?,       // Brandul produsului (poate fi null)
    val ingredients_text: String?,  // Lista de ingrediente (poate fi null)
    val score: Int?,           // Scorul produsului (poate fi null)
    val recommended_age: String?  // Vârsta recomandată (poate fi null)
)
