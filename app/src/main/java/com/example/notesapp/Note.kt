package com.example.notesapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
class Note(
    @ColumnInfo(name = "text") val name: String,
    @ColumnInfo(name = "time") val time: String = "d",
    @ColumnInfo(name = "ram") val ram: String = "d",
    @ColumnInfo(name = "charging") val charging: String = "d",
    @ColumnInfo(name = "screen") val screen: String = "d",
    @ColumnInfo(name = "bluetooth") val bluetooth: String = "d",
    @ColumnInfo(name = "internet") val internet: String = "d",
    @ColumnInfo(name = "location") val location: String = "d",
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)