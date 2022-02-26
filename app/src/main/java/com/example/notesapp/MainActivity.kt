package com.example.notesapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.graph.GraphActivity
import com.example.notesapp.graph.ClearDialogFragment
import com.example.notesapp.graph.ClearDialogFragment.Companion.NO
import com.example.notesapp.graph.ClearDialogFragment.Companion.YES
import com.example.notesapp.worker.MyWorkerService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), INotesRVAdapter,
    ClearDialogFragment.GraphDialogFragmentListener {
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var listRam: List<Note>
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
            listRam = it
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

        graphPlot.setOnClickListener {
            val arrayList = ArrayList<Int>()
                for(i in listRam){
                    if(i.ram!="d")
                      arrayList.add(i.ram.toInt())
                }
                startActivity(GraphActivity.getInstance(this, arrayList))
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
            val newName = Note()
            newName.name = notetext
            noteViewModel.insertNote(newName)
            //  Toast.makeText(this, "Insert", Toast.LENGTH_SHORT).show()
        }
        input.setText("")
    }

    fun clearData(view: android.view.View) {
        val graphDialog = ClearDialogFragment.getInstance(this)
        graphDialog.show(supportFragmentManager, "see graph")
    }

    override fun onSelectedYesNo(buttonClicked: String) {
       if(buttonClicked == YES) {
           noteViewModel.deleteAll()
       }
        else if(buttonClicked == NO) {
           Toast.makeText(this, "You pressed No", Toast.LENGTH_LONG).show()
       }
    }
}