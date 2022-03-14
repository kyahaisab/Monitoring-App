package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.fragments.GraphActivity
import com.example.notesapp.fragments.ClearDialogFragment
import com.example.notesapp.fragments.ClearDialogFragment.Companion.NO
import com.example.notesapp.fragments.ClearDialogFragment.Companion.YES
import com.example.notesapp.media.DeleteSound
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

        startService(Intent(this, StartBackgroundService::class.java))


        fab.setOnClickListener {
            // You can also use BuildConfig.FLAVOUR to know pais or free, etc
            if(BuildConfig.FLAVOR != "paid")
                Toast.makeText(this, "Full screen only for premium users", Toast.LENGTH_LONG).show()
            else {
                val bView: View = window.decorView
                bView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
                fab.visibility = View.GONE
            }
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
        startActivity(Display.newInstance(this, note))
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
        val clearDialog = ClearDialogFragment.getInstance(this)
        clearDialog.show(supportFragmentManager, "clear all")
    }

    override fun onSelectedYesNo(buttonClicked: String) {
       if(buttonClicked == YES) {
           if(BuildConfig.FLAVOR == "paid"){
               startService(Intent(this, DeleteSound::class.java))
           }
           noteViewModel.deleteAll()
       }
        else if(buttonClicked == NO) {
           Toast.makeText(this, "You pressed No", Toast.LENGTH_LONG).show()
       }
    }
}