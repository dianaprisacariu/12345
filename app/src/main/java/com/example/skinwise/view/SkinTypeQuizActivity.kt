package com.example.skinwise.view

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.skinwise.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class SkinTypeQuizActivity : AppCompatActivity() {

    private lateinit var questionText: TextView
    private lateinit var questionProgress: TextView
    private lateinit var optionsGroup: RadioGroup
    private lateinit var nextButton: Button

    private var currentQuestionIndex = 0
    private val answers = mutableListOf<Int>()

    private val skinTypeScores = mutableMapOf<String, Int>()

    private val questions = listOf(
        Question(
            "Cum se simte pielea ta după curățare?",
            listOf("Tensionată și uscată", "Catifelată", "Lucioasă", "Lucioasă în zona T, uscată pe obraji"),
            listOf("Uscată", "Normală", "Grasă", "Mixtă")
        ),
        Question(
            "Cum arată pielea ta la finalul zilei?",
            listOf("Uscată", "Echilibrată", "Lucioasă zona T", "Foarte grasă"),
            listOf("Uscată", "Normală", "Mixtă", "Grasă")
        ),
        Question(
            "Cât de frecvent ai coșuri sau puncte negre?",
            listOf("Niciodată", "Ocazional", "Frecvent în zona T", "Des în toată fața"),
            listOf("Normală", "Mixtă", "Mixtă", "Grasă")
        ),
        Question(
            "Pielea ta reacționează la produse noi?",
            listOf("Da, se irită", "Uneori", "Rar", "Niciodată"),
            listOf("Sensibilă", "Sensibilă", "Normală", "Normală")
        ),
        Question(
            "Cum simți textura pielii tale?",
            listOf("Aspră", "Fină", "Lucioasă", "Mix: uscată/grasă"),
            listOf("Uscată", "Normală", "Grasă", "Mixtă")
        ),
        Question(
            "Cum este pielea dimineața?",
            listOf("Strânsă și uscată", "Normală", "Lucioasă zona T", "Foarte lucioasă"),
            listOf("Uscată", "Normală", "Mixtă", "Grasă")
        ),
        Question(
            "Expunere la soare fără protecție?",
            listOf("Des", "Rar", "Niciodată", "Accidental"),
            listOf("Sensibilă", "Normală", "Normală", "Normală")
        ),
        Question(
            "Ai alergii la vreun ingredient cosmetic?",
            listOf("Parfum", "Alcool", "Niciunul", "Nu știu"),
            listOf("parfum", "alcohol", "none", "none") // folosit ca `allergy`
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skin_type_quiz)

        questionText = findViewById(R.id.questionText)
        questionProgress = findViewById(R.id.questionProgress)
        optionsGroup = findViewById(R.id.optionsGroup)
        nextButton = findViewById(R.id.nextButton)

        showQuestion()

        nextButton.setOnClickListener {
            val selectedId = optionsGroup.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this, "Selectează o opțiune", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedIndex = optionsGroup.indexOfChild(findViewById(selectedId))
            answers.add(selectedIndex)

            currentQuestionIndex++

            if (currentQuestionIndex < questions.size) {
                showQuestion()
            } else {
                calculateSkinType()
            }
        }
    }

    private fun showQuestion() {
        val question = questions[currentQuestionIndex]
        questionText.text = question.text
        questionProgress.text = "Întrebarea ${currentQuestionIndex + 1} din ${questions.size}"

        optionsGroup.removeAllViews()
        val letters = listOf("A", "B", "C", "D")

        question.options.forEachIndexed { index, option ->
            val radioButton = RadioButton(this).apply {
                text = "${letters[index]}. $option"
                textSize = 16f
                setPadding(32, 24, 32, 24)
                buttonDrawable = null
                background = ContextCompat.getDrawable(this@SkinTypeQuizActivity, R.drawable.radio_button_selector)
                setTextColor(ContextCompat.getColor(this@SkinTypeQuizActivity, android.R.color.black))

                layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 16, 0, 16)
                }
            }

            optionsGroup.addView(radioButton)
        }
    }

    private fun calculateSkinType() {
        skinTypeScores.clear()

        // Ultima întrebare este despre alergii
        val allergy = questions.last().effects[answers.last()]
        val skinAnswers = answers.dropLast(1)

        skinAnswers.forEachIndexed { index, selectedIndex ->
            val skinType = questions[index].effects[selectedIndex]
            skinTypeScores[skinType] = skinTypeScores.getOrDefault(skinType, 0) + 1
        }

        val finalSkinType = skinTypeScores.maxByOrNull { it.value }?.key ?: "Necunoscut"

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)

        val data = mapOf(
            "skinType" to finalSkinType,
            "allergy" to allergy
        )

        userRef.set(data, SetOptions.merge())
            .addOnSuccessListener {
                val intent = Intent(this, SkinTypeResultActivity::class.java)
                intent.putExtra("skinType", finalSkinType)
                intent.putExtra("allergy", allergy)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Eroare la salvare", Toast.LENGTH_SHORT).show()
            }
    }
}

data class Question(
    val text: String,
    val options: List<String>,
    val effects: List<String>
)
