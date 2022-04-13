package com.example.notesapp.contentProviders

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
import com.example.notesapp.NoteDataBase
import com.example.notesapp.NoteRepository
import java.lang.UnsupportedOperationException

class NoteDataProvider: ContentProvider() {
    lateinit var uriX: UriMatcher


    override fun onCreate(): Boolean {
        uriX = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI("com.example.notesapp.provider", "TimeAndRamTable1", 1)
        }
        Log.e("SAGAR", "Provider, oncreate")
        return true
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {

        val arr = arrayOf("Time", "Ram")
        val crsr = MatrixCursor(arr)
//        val dao = context?.let { NoteDataBase.getDatabase(context = it.applicationContext).getNoteDao() }
//        dao?.let {
//            val repository = NoteRepository(dao)
//
//        }
        crsr.newRow()
            .add("Time","kharab hai")
            .add("Ram", "Rade bolo Shyam")
        Log.e("SAGAR", "Provider, query")
        return crsr
    }

    override fun getType(p0: Uri): String? {
        return null
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
       throw UnsupportedOperationException("This is not excepted operation")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        throw UnsupportedOperationException("This is not excepted operation")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        throw UnsupportedOperationException("This is not excepted operation")
    }
}