package com.example.skinwise.view

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.skinwise.R
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

class QuizResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_result)

        val score = intent.getIntExtra("score", 0)
        val total = intent.getIntExtra("total", 0)

        val resultText = findViewById<TextView>(R.id.resultText)
        val messageText = findViewById<TextView>(R.id.messageText)
        val backButton = findViewById<Button>(R.id.backButton)
        val retryButton = findViewById<Button>(R.id.retryButton)
        val konfettiView = findViewById<nl.dionsegijn.konfetti.xml.KonfettiView>(R.id.konfettiView)

        resultText.text = "Ai obținut $score din $total răspunsuri corecte."

        messageText.text = when {
            score == total -> "Excelent! Ești un adevărat expert în skincare! ✨"
            score >= total * 0.7 -> "Foarte bine! Mai citește puțin și vei fi expert! ✨"
            score >= total * 0.4 -> "Este bine! Dar poți învăța și mai mult! ✨"
            else -> "Nu-i nimic! Reia lecțiile și revino mai pregătit! ✨"
        }

        if (score >= total * 0.6) {
            konfettiView.visibility = View.VISIBLE
            val party = Party(
                speed = 10f,
                maxSpeed = 50f,
                damping = 0.9f,
                spread = 360,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                emitter = Emitter(duration = 2, TimeUnit.SECONDS).max(200),
                position = Position.Relative(0.5, 0.0)
            )
            konfettiView.start(party)
        }

        retryButton.setBackgroundResource(R.drawable.button_rounded)
        backButton.setBackgroundResource(R.drawable.button_rounded)

        retryButton.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
            finish()
        }

        backButton.setOnClickListener {
            startActivity(Intent(this, EducationActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            })
            finish()
        }

        animateButton(retryButton)
        animateButton(backButton)
    }

    private fun animateButton(button: Button) {
        button.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.animate().scaleX(0.95f).scaleY(0.95f).duration = 100
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    v.animate().scaleX(1f).scaleY(1f).duration = 100
                }
            }
            false
        }
    }
}
