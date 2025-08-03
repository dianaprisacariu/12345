package com.example.skinwise.model

import java.io.Serializable

data class Lesson(
    val title: String,
    val description: String,
    val fullText: String,
    val imageResId: Int,
) : Serializable
