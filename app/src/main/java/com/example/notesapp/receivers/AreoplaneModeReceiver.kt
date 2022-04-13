package com.example.notesapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.notesapp.worker.MyWorkerService

class AreoplaneModeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val isAirplaneModeEnabled = intent?.getBooleanExtra("state", false) ?: return


        if (isAirplaneModeEnabled) {
            Toast.makeText(context, "Airplane Mode Enabled", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Airplane Mode Disabled", Toast.LENGTH_LONG).show()
        }

        context?.let {
            MyWorkerService.startWorkManager(context)
        }
    }
}
/*
For this receiver no need to give permission in manifest, but in case of Alarm Broadcast receiver
give permission in manifest.

This is Broadcast listner is related to system (battery on off, airplaneMode, bluetooth), but
you can make events that listen to custom events like when to invoke alarm (alarmManager package).

You can send or receive broadcast from another app:
https://www.youtube.com/watch?v=bjlteHd_9rY&list=PLfuE3hOAeWhYCPPLA75AXfd0pILeyePjv&index=33&t=10s
 */