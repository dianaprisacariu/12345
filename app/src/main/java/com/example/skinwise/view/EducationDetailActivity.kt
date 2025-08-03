package com.example.skinwise.view

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.skinwise.Manager.LessonProgressManager
import com.example.skinwise.R
import com.example.skinwise.model.Lesson
import com.example.skinwise.utils.AccentUtils

class EducationDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_education_detail)

        val lesson = intent.getSerializableExtra("lesson") as? Lesson

        val titleTextView = findViewById<TextView>(R.id.lessonTitle)
        val contentTextView = findViewById<TextView>(R.id.lessonContent)
        val imageView = findViewById<ImageView>(R.id.lessonImage)


        lesson?.let {
            titleTextView.text = it.title
            contentTextView.text = it.fullText
            imageView.setImageResource(it.imageResId)
        }

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        // Accent personalizat
        AccentUtils.applyAccentFromPrefs(this, backButton)
        val prefs = getSharedPreferences("userPrefs", MODE_PRIVATE)
        val color = when (prefs.getString("themeColor", "green")) {
            "blue" -> "#448AFF"
            "pink" -> "#F06292"
            else -> "#91DDCF"
        }
        titleTextView.setTextColor(android.graphics.Color.parseColor(color))
    }
}
