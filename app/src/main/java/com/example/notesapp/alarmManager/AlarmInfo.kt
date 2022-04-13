package com.example.notesapp.alarmManager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService

class AlarmInfo {

    companion object {
        fun setAlert(context: Context) {
            try {
                val time = 20
                val intent = Intent(
                    context,
                    MyBroadCastReceiver::class.java
                )
                val pendingIntent = PendingIntent.getBroadcast(context, 234324243, intent, 0)
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.e("SAGAR", "exact and Ideal")
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + time * 1000,
                        pendingIntent
                    )
                }
                else{
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + time * 1000,
                        pendingIntent
                    )
                }
                Log.e("SAGAR", "Alarm is created")
                // Toast.makeText(context, "Alarm create in $time seconds", Toast.LENGTH_LONG).show()
            } catch (error: Throwable) {
                Log.e("SAGAR", "error occoured while setting alarm")
            }
        }
    }
}