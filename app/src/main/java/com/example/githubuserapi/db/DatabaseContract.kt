package com.example.githubuserapi.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.example.githubuserapi"
    const val SCHEME = "content"

     class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite_user"
            const val _ID = "_id"
            const val COLUMN_NAME_LOGIN = "login"
            const val COLUMN_NAME_AVATAR_URL = "avatar_url"

            val CONTENT_URI : Uri= Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}