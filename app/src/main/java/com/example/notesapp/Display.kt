package com.example.notesapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_display.*

class Display : AppCompatActivity() {

    companion object {
        const val INFO = "INFO_ET"
        fun newInstance(context: Context, msg: String): Intent {
            val intent = Intent(context, Display::class.java)
            intent.putExtra(INFO, msg)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)

        twDispaly.setMovementMethod(ScrollingMovementMethod())
        val st = getIntent().getStringExtra(INFO).toString()
        twDispaly.text = st
    }
}