package com.example.notesapp.media

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.example.notesapp.R

class DeleteSound: Service() {
    private lateinit var mediaPlayer: MediaPlayer

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer = MediaPlayer.create(this, R.raw.delete)
        mediaPlayer.isLooping = false
        mediaPlayer.start()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        mediaPlayer.stop()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}