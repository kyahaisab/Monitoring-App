package com.example.notesapp.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DbMigrations {
    fun allMigrations(): Array<Migration> = arrayOf(
        GoFrom1to2()
    )

    // TODO: 04/03/22 Not implemented yet

    class GoFrom1to2 : Migration(1,2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE notes_table"+
                    " ADD COLUMN power TEXT")
        }
    }
}

