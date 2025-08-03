package com.example.skinwise.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.skinwise.R

object AccentUtils {
    fun applyAccent(view: View, colorKey: String) {
        val color = when (colorKey) {
            "blue" -> Color.parseColor("#448AFF")
            "pink" -> Color.parseColor("#F06292")
            else -> Color.parseColor("#91DDCF") // verde default
        }
        view.setBackgroundColor(color)
    }

    fun applyAccentFromPrefs(activity: Activity, vararg views: View) {
        val prefs = activity.getSharedPreferences("userPrefs", Activity.MODE_PRIVATE)
        val themeColor = prefs.getString("themeColor", "green") ?: "green"
        views.forEach { applyAccent(it, themeColor) }
    }
    fun applyAccentToButton(context: Context, button: Button) {
        val prefs = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val color = when (prefs.getString("themeColor", "green")) {
            "blue" -> "#448AFF"
            "pink" -> "#F06292"
            else -> "#91DDCF"
        }
        button.setBackgroundColor(Color.parseColor(color))
    }
    fun getAccentColor(context: Context): Int {
        val prefs = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        return when (prefs.getString("themeColor", "green")) {
            "blue" -> Color.parseColor("#448AFF")
            "pink" -> Color.parseColor("#F06292")
            else -> Color.parseColor("#91DDCF")
        }
    }
    fun applyTextColor(textView: TextView, colorKey: String) {
        val color = when (colorKey) {
            "blue" -> Color.parseColor("#448AFF")
            "pink" -> Color.parseColor("#F06292")
            else -> Color.parseColor("#91DDCF") // verde default
        }
        textView.setTextColor(color)
    }
    fun applyAccentToButtons(context: Context, vararg buttons: Button) {
        val prefs = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val color = when (prefs.getString("themeColor", "green")) {
            "blue" -> "#448AFF"
            "pink" -> "#F06292"
            else -> "#91DDCF" // green default
        }

        buttons.forEach {
            it.setBackgroundColor(Color.parseColor(color))
            it.setTextColor(Color.WHITE)
        }
    }

}
