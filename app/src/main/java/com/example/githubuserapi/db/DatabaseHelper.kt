package com.example.githubuserapi.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.githubuserapi.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_AVATAR_URL
import com.example.githubuserapi.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_LOGIN
import com.example.githubuserapi.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.example.githubuserapi.db.DatabaseContract.UserColumns.Companion._ID

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "DbFavoritUser"
        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE = "CREATE TABLE $TABLE_NAME" +
                "($_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $COLUMN_NAME_LOGIN TEXT NOT NULL UNIQUE," +
                " $COLUMN_NAME_AVATAR_URL TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}