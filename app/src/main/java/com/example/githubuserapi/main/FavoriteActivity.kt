package com.example.githubuserapi.main

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapi.R
import com.example.githubuserapi.adapter.FavoriteAdapter
import com.example.githubuserapi.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuserapi.db.UserHelper
import com.example.githubuserapi.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteAdapter
    private lateinit var favoriteUserHelper: UserHelper

    companion object {
        const val EXTRA_LOGIN = "extra_login"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        supportActionBar?.title = resources.getString(R.string.favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        showListFavoriteUser()

        favoriteUserHelper = UserHelper.getInstance(applicationContext)
        favoriteUserHelper.open()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadFavoriteUserAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

    }

    private fun showListFavoriteUser() {
        recyclerviewFavorite.layoutManager = LinearLayoutManager(this)
        recyclerviewFavorite.setHasFixedSize(true)

        adapter = FavoriteAdapter()

        recyclerviewFavorite.adapter = adapter
    }

    private fun loadFavoriteUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBarFavorite.visibility = View.VISIBLE
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressBarFavorite.visibility = View.INVISIBLE
            val favorites = deferredFavorites.await()
            if (favorites.size > 0) {
                adapter.list = favorites
            } else {
                adapter.list = ArrayList()
                val text = resources.getString(R.string.snackbar_no_data)
                Snackbar.make(recyclerviewFavorite, text, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putParcelableArrayList(EXTRA_LOGIN, adapter.list)
            putParcelableArrayList(EXTRA_AVATAR_URL, adapter.list)
        }
    }

    override fun onResume() {
        super.onResume()
        showListFavoriteUser()
        loadFavoriteUserAsync()
    }


    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

}