package com.example.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), INotesRVAdapter {
    private lateinit var noteViewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = NotesRVAdapter(this, this)
        recyclerView.adapter = adapter

        noteViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)
        noteViewModel.allNotes.observe(this, Observer {
            adapter.updateList(it)
        })
    }

    override fun onDeleteClicked(note: Note) {
        noteViewModel.deleteNote(note)
      //  Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClicked(note: Note) {
        startActivity(Display.newInstance(this, note.name))
    }

    fun submitData(view: android.view.View) {
        val notetext = input.text.toString()
        if (notetext.isNotEmpty()) {
            noteViewModel.insertNote(Note(notetext))
          //  Toast.makeText(this, "Insert", Toast.LENGTH_SHORT).show()
        }
        input.setText("")
    }
}