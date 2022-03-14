package com.example.notesapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.notesapp.worker.MyWorkerService

class StartBackgroundService: Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MyWorkerService.startWorkManager(applicationContext)
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
       return null
    }

}