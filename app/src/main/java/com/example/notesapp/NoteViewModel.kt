package com.example.notesapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    val allNotes: LiveData<List<Note>>

    init {
        val dao = NoteDataBase.getDatabase(application).getNoteDao()
        val repository = NoteRepository(dao)
        allNotes = repository.allNotes
    }


}