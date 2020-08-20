package com.example.githubuserapi.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.example.githubuserapi.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_LOGIN
import com.example.githubuserapi.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.example.githubuserapi.db.DatabaseContract.UserColumns.Companion._ID

class UserHelper(context: Context) {

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private lateinit var database : SQLiteDatabase

        private var INSTANCE : UserHelper? = null
        fun getInstance(context: Context) : UserHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserHelper(context)
            }
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun isOpen(): Boolean {
        return try {
            database.isOpen
        } catch (e: Exception) {
            false
        }
    }

    fun queryAll() : Cursor {
        return database.query(
            DATABASE_TABLE, null, null, null, null, null, "$_ID ASC"
        )
    }

    fun queryByLogin(login : String) : Cursor {
        return database.query(
            DATABASE_TABLE, null, "$COLUMN_NAME_LOGIN = ?", arrayOf(login), null, null, null, null
        )
    }

    fun insert(value : ContentValues) : Long {
        return database.insert(DATABASE_TABLE, null, value)
    }

    fun deleteByLogin(login : String) : Int {
        return database.delete(DATABASE_TABLE, "$COLUMN_NAME_LOGIN = '$login'", null)
    }
}