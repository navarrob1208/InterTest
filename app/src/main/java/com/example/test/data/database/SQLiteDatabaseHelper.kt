package com.example.test.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.test.data.net.responses.Table

class SQLiteDatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
    }

    companion object {
        const val DATABASE_NAME = "test66.db"
        const val DATABASE_VERSION = 1
    }
}

