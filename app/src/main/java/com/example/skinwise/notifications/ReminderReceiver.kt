package com.example.skinwise.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.skinwise.R

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("ReminderReceiver", "Alarm triggered!")

        val builder = NotificationCompat.Builder(context, "default")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("E timpul pentru rutina ta de skincare! 🌿")
            .setContentText("Nu uita să ai grijă de pielea ta în seara asta ✨")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val permissionGranted =
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED

        if (permissionGranted) {
            NotificationManagerCompat.from(context).notify(2001, builder.build())
        } else {
            Log.w("ReminderReceiver", "Permisiunea pentru notificări nu este acordată.")
        }
    }
}
