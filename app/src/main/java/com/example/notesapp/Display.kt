package com.example.notesapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_display.*
import android.app.Activity
import android.view.inputmethod.InputMethodManager
import com.example.notesapp.fragments.ClearDialogFragment.Companion.YES
import com.example.notesapp.fragments.ItemListDialogFragment


class Display() : AppCompatActivity() {
    private lateinit var noteViewModel: NoteViewModel

    companion object {
        const val INFO = "INFO_ET"
        fun newInstance(context: Context, note: Note): Intent {
            val intent = Intent(context, Display::class.java)
            intent.putExtra(INFO, note)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)

        noteViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)

        twDispaly.movementMethod = ScrollingMovementMethod()
        val noteData = intent.getSerializableExtra(INFO) as Note
        twDispaly.setText(noteData.name)

        deleteD.setOnClickListener {
            ItemListDialogFragment.newInstance(30).apply {
                fListener ={
                    if(it==YES)
                    {
                        noteViewModel.deleteNote(noteData)
                        super.onBackPressed()
                    }
                    else{
                    }
                }
            }.show(supportFragmentManager, "dialog")
        }
        updateD.setOnClickListener {
            noteData.name = twDispaly.text.toString()
            noteViewModel.update(noteData)
            Toast.makeText(this, noteData.name, Toast.LENGTH_SHORT).show()

            //To close softKeypad
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        }
    }
}

