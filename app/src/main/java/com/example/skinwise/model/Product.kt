package com.example.skinwise.model

data class Product(
    val id: Long = 0,  // <-- AICI am schimbat din String în Long
    val name: String = "",
    val brand: String? = null,
    val score: Double? = null,
    var validation_score: Double? = null,
    val eans: List<String>? = null,
    val categories: List<Map<String, Any>>? = null,
    val compositions: List<Map<String, Any>>? = null,
    val images: Map<String, String>? = null
)

