package com.example.skinwise.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.skinwise.R
import com.example.skinwise.model.Lesson
import com.example.skinwise.utils.AccentUtils
import com.example.skinwise.view.QuizActivity

class EducationAdapter(
    private val lessons: List<Lesson>,
    private val onClick: (Lesson) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Tipuri de iteme
    private val VIEW_TYPE_LESSON = 0
    private val VIEW_TYPE_BUTTON = 1

    override fun getItemCount(): Int = lessons.size + 1 // lecții + buton

    override fun getItemViewType(position: Int): Int {
        return if (position < lessons.size) VIEW_TYPE_LESSON else VIEW_TYPE_BUTTON
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_LESSON) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lesson, parent, false)
            LessonViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quiz_button, parent, false)
            QuizButtonViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LessonViewHolder && position < lessons.size) {
            holder.bind(lessons[position])
        } else if (holder is QuizButtonViewHolder) {
            holder.bind()
        }
    }

    inner class LessonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.lessonTitle)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.lessonDescription)
        private val imageView: ImageView = itemView.findViewById(R.id.lessonImage)

        fun bind(lesson: Lesson) {
            titleTextView.text = lesson.title
            descriptionTextView.text = lesson.description
            imageView.setImageResource(lesson.imageResId)

            // Culoare de accent pe titlu
            val color = AccentUtils.getAccentColor(itemView.context)
            titleTextView.setTextColor(color)

            itemView.setOnClickListener {
                onClick(lesson)
            }
        }
    }

    inner class QuizButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            val button = itemView.findViewById<Button>(R.id.quizStartButton)
            button.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, QuizActivity::class.java)
                context.startActivity(intent)
            }
        }
    }
}