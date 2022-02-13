package com.example.notesapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.notesapp.worker.MyWorkerService
import com.example.notesapp.worker.MyWorkerService.Companion.workRequest
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

        MyWorkerService.startWorkManager(applicationContext)

//        WorkManager.getInstance(applicationContext)
//            .getWorkInfoByIdLiveData(workRequest.id)
//            .observe(this, { workInfo ->
//                if (workInfo != null && workInfo.state.isFinished) {
//                    Log.e("SAGAR", "observing some values and inserted")
//                    noteViewModel.insertNote(Note("x" + workInfo.outputData.getString(MyWorkerService.PARSE_CONSTANT)))
//                }
//                else{
//                    Log.e("SAGAR", "observing some values but not inserted $workInfo ${workInfo.state.isFinished}")
//                }
//            })
        //Above code is not working, only one time its working, bcz workTRequest is changing every time

        fab.setOnClickListener {
            val bView: View = window.decorView
            bView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
            fab.visibility = View.GONE

        }
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