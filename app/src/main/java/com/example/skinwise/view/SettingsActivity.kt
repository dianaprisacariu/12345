package com.example.skinwise.view

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.skinwise.R
import com.example.skinwise.notifications.ReminderReceiver
import java.util.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var notificationSwitch: Switch
    private lateinit var darkModeSwitch: Switch
    private lateinit var themeGroup: RadioGroup
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        prefs = getSharedPreferences("userPrefs", MODE_PRIVATE)

        notificationSwitch = findViewById(R.id.notificationSwitch)
        darkModeSwitch = findViewById(R.id.darkModeSwitch)
        themeGroup = findViewById(R.id.themeRadioGroup)

        createNotificationChannel()

        // Inițializare stări din SharedPreferences
        notificationSwitch.isChecked = prefs.getBoolean("notificationsEnabled", true)
        darkModeSwitch.isChecked = prefs.getBoolean("darkMode", false)

        when (prefs.getString("themeColor", "green")) {
            "green" -> themeGroup.check(R.id.themeGreen)
            "blue" -> themeGroup.check(R.id.themeBlue)
            "pink" -> themeGroup.check(R.id.themePink)
        }

        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkAndRequestNotificationPermission()
            } else {
                prefs.edit().putBoolean("notificationsEnabled", false).apply()
                cancelDailyReminder()
                Toast.makeText(this, "Notificările au fost dezactivate.", Toast.LENGTH_SHORT).show()
            }
        }

        darkModeSwitch.setOnCheckedChangeListener { _, isDark ->
            prefs.edit().putBoolean("darkMode", isDark).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
            recreate()
        }

        themeGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedColor = when (checkedId) {
                R.id.themeGreen -> "green"
                R.id.themeBlue -> "blue"
                R.id.themePink -> "pink"
                else -> "green"
            }
            prefs.edit().putString("themeColor", selectedColor).apply()
            Toast.makeText(this, "Culoare salvată. Repornește aplicația pt efect.", Toast.LENGTH_SHORT).show()
        }
        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()  // Inchide activitatea si te duce inapoi la activitatea precedenta
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                "default",
                "Skinwise Reminders",
                android.app.NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canal pentru mementouri de skincare"
            }
            val notificationManager =
                getSystemService(android.content.Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            } else {
                prefs.edit().putBoolean("notificationsEnabled", true).apply()
                sendNotification()
                scheduleDailyReminder()
            }
        } else {
            prefs.edit().putBoolean("notificationsEnabled", true).apply()
            sendNotification()
            scheduleDailyReminder()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            prefs.edit().putBoolean("notificationsEnabled", true).apply()
            sendNotification()
            scheduleDailyReminder()
        } else {
            notificationSwitch.isChecked = false
            prefs.edit().putBoolean("notificationsEnabled", false).apply()
            Toast.makeText(this, "Permisiunea pentru notificări a fost refuzată.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendNotification() {
        val builder = NotificationCompat.Builder(this, "default")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Notificări activate")
            .setContentText("Vei primi notificări personalizate.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this).notify(1001, builder.build())
        }
    }

    private fun scheduleDailyReminder() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            add(Calendar.MINUTE, 1) // pentru test
            set(Calendar.SECOND, 0)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }

        Toast.makeText(this, "Alarmă setată pentru: ${calendar.time}", Toast.LENGTH_LONG).show()
    }

    private fun cancelDailyReminder() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
    }
}
