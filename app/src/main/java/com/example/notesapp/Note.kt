package com.example.notesapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes_table")
data class Note(
    @JvmField
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @JvmField
    @ColumnInfo(name = "text") var name: String = "d",
    @JvmField
    @ColumnInfo(name = "time") var time: String = "d",
    @JvmField
    @ColumnInfo(name = "ram") var ram: String = "d",
    @JvmField
    @ColumnInfo(name = "charging") var charging: String = "d",
    @JvmField
    @ColumnInfo(name = "screen") var screen: String = "d",
    @JvmField
    @ColumnInfo(name = "bluetooth") var bluetooth: String = "d",
    @JvmField
    @ColumnInfo(name = "internet") var internet: String = "d",
    @JvmField
    @ColumnInfo(name = "location") var location: String = "d",

): Serializable
/*
@JvmField:
 1. @JvmField indicates weather the kotlin compiler should generate getters/setters for this property or not.
If its set then it will not generate getters/setters for this property and expose it as a field.
if you use try to access name from java file without using @JvmField , you have to use getName() and setName.
with this .name to get and set.
 */

/*
Serializable:
In Android we cannot just pass objects to activities. To do this the objects must either implement Serializable or Parcelable interface.
Helps to pass object of Note in intent using putExtra,
without Serializable putExtra("INGO", Note()) will show error. We can do the same using Parcelable but its difficult.
Parcelable process is much faster than Serializable
TypeCasting as Note in Display Activity: val noteData = intent.getSerializableExtra(INFO) as Note
 */