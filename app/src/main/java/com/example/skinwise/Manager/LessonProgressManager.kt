package com.example.skinwise.Manager

import android.content.Context

class LessonProgressManager(context: Context) {
    private val prefs = context.getSharedPreferences("lesson_prefs", Context.MODE_PRIVATE)

    fun markAsRead(id: Int) {
        prefs.edit().putBoolean("lesson_$id", true).apply()
    }

    fun isLessonRead(id: Int): Boolean {
        return prefs.getBoolean("lesson_$id", false)
    }
}
