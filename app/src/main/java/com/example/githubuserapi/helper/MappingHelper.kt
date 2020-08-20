package com.example.githubuserapi.helper

import android.database.Cursor
import com.example.githubuserapi.db.DatabaseContract
import com.example.githubuserapi.model.GithubUser
import java.util.ArrayList

object MappingHelper {

    fun mapCursorToArrayList(favoriteUserCursor: Cursor?): ArrayList<GithubUser> {
        val favoriteUserItemsList = ArrayList<GithubUser>()

        favoriteUserCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val login = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_NAME_LOGIN))
                val avatarUrl = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_NAME_AVATAR_URL))
                favoriteUserItemsList.add(GithubUser(id, login, avatarUrl, null, null, null, null, null, null, null))
            }
        }
        return favoriteUserItemsList
    }
}