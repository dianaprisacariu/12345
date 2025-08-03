package com.example.skinwise.model

data class QuizQuestion(
    val question: String = "",
    val options: List<String> = emptyList(),
    val correctAnswerIndex: Int = 0
)
