package com.example.notesapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
class Note(@ColumnInfo(name="text")val name: String) {
    @PrimaryKey(autoGenerate = true)var id:Int=0
}