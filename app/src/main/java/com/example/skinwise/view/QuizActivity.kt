package com.example.skinwise.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.skinwise.R
import com.example.skinwise.model.QuizQuestion
import com.google.firebase.firestore.FirebaseFirestore

class QuizActivity : AppCompatActivity() {

    private val questions = mutableListOf<QuizQuestion>()
    private var currentIndex = 0
    private var score = 0

    private lateinit var questionText: TextView
    private lateinit var optionsGroup: RadioGroup
    private lateinit var submitAnswerButton: Button
    private lateinit var progressText: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        questionText = findViewById(R.id.questionText)
        optionsGroup = findViewById(R.id.optionsGroup)
        submitAnswerButton = findViewById(R.id.submitAnswerButton)
        progressText = findViewById(R.id.progressText)
        progressBar = findViewById(R.id.progressBar)

        animateButton(submitAnswerButton)
        loadQuestionsFromFirestore()

        submitAnswerButton.setOnClickListener {
            val selectedId = optionsGroup.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this, "Selectează un răspuns", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedIndex = optionsGroup.indexOfChild(findViewById(selectedId))
            val correctIndex = questions[currentIndex].correctAnswerIndex
            val correct = selectedIndex == correctIndex

            val selectedRadioButton = optionsGroup.getChildAt(selectedIndex) as RadioButton
            selectedRadioButton.setBackgroundColor(
                ContextCompat.getColor(this, if (correct) R.color.correct_green else R.color.error_red)
            )

            if (!correct) {
                val correctRadioButton = optionsGroup.getChildAt(correctIndex) as RadioButton
                correctRadioButton.setBackgroundColor(ContextCompat.getColor(this, R.color.correct_green))
            }

            Handler(Looper.getMainLooper()).postDelayed({
                if (correct) score++
                currentIndex++

                if (currentIndex < questions.size) {
                    showQuestion()
                } else {
                    val intent = Intent(this, QuizResultActivity::class.java)
                    intent.putExtra("score", score)
                    intent.putExtra("total", questions.size)
                    startActivity(intent)
                    finish()
                }
            }, 1000)
        }
    }

    private fun loadQuestionsFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.collection("quizQuestions")
            .get()
            .addOnSuccessListener { result ->
                questions.clear()
                for (document in result) {
                    val question = document.toObject(QuizQuestion::class.java)
                    questions.add(question)
                }
                if (questions.isNotEmpty()) {
                    showQuestion()
                } else {
                    Toast.makeText(this, "Nu s-au găsit întrebări.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Eroare la încărcarea întrebărilor.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showQuestion() {
        val question = questions[currentIndex]
        questionText.text = question.question
        optionsGroup.removeAllViews()

        progressText.text = "Întrebarea ${currentIndex + 1} din ${questions.size}"
        progressBar.progress = ((currentIndex + 1).toDouble() / questions.size * 100).toInt()

        val optionPrefixes = listOf("A.", "B.", "C.", "D.")
        question.options.forEachIndexed { index, optionText ->
            val radioButton = RadioButton(this).apply {
                text = "${optionPrefixes.getOrNull(index) ?: ""} $optionText"
                textSize = 16f
                setPadding(32, 24, 32, 24)
                background = ContextCompat.getDrawable(this@QuizActivity, R.drawable.radio_button_selector)
                setTextColor(ContextCompat.getColor(this@QuizActivity, R.color.black))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 12, 0, 12)
                }
                buttonDrawable = null
                typeface = ResourcesCompat.getFont(this@QuizActivity, R.font.poppins_regular)
            }

            optionsGroup.addView(radioButton)
        }
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
