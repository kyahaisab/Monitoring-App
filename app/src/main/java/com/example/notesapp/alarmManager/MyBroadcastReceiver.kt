package com.example.notesapp.alarmManager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.notesapp.worker.MyWorkerService

class MyBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            MyWorkerService.startWorkManager(context)
        }
        //  Toast.makeText(context, "Wake Up", Toast.LENGTH_LONG).show()
        Log.e("SAGAR", "Wake up, startWorker called")
    }
}