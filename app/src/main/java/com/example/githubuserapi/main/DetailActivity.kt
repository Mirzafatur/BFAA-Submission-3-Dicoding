package com.example.githubuserapi.main


import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserapi.R
import com.example.githubuserapi.adapter.SectionPagerAdapter
import com.example.githubuserapi.api.ApiConfig
import com.example.githubuserapi.db.DatabaseContract
import com.example.githubuserapi.db.UserHelper
import com.example.githubuserapi.model.GithubUser
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"
    }

    private lateinit var sectionPagerAdapter: SectionPagerAdapter
    private lateinit var userHelper: UserHelper
    private lateinit var username : String
    private lateinit var avatarUrl : String
    private var isFavorite : Boolean = false
    private lateinit var menuItem : Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        username = intent?.getStringExtra(EXTRA_NAME).toString()

        setActionBar()
        getDetailApi(username)
        initSectionPager()
        sectionPagerAdapter.username = username

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        favoriteState()
    }

    private fun initSectionPager(){
        sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionPagerAdapter
        tab_layout.setupWithViewPager(view_pager)
    }

    private fun getDetailApi(username : String?){
        val client = username?.let {
            ApiConfig.getApiServices().getDetailUser(it)
        }
        client?.enqueue(object : Callback<GithubUser>{

            override fun onFailure(call: Call<GithubUser>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "Failed load", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
                progressBar.visibility = View.INVISIBLE
            }

            override fun onResponse(call: Call<GithubUser>, response: Response<GithubUser>) {
                try {
                    val data = response.body()
                    //progressBarDetail.visibility = View.VISIBLE

                    Glide.with(this@DetailActivity)
                        .load(data?.avatar)
                        .apply(RequestOptions())
                        .into(img_detail_photo)

                    tv_detail_fullname.text = if (data?.name != null) data.name else "-"
                    tv_detail_username.text = if (data?.username != null) data.username else "-"
                    tv_detail_company.text = if (data?.company != null) data.company else "-"
                    tv_detail_location.text = if (data?.location != null) data.location else "-"
                    tv_detail_repo.text = if (data?.repository != null) data.repository else "-"
                    tv_detail_followers.text = if (data?.follower != null) data.follower else "-"
                    tv_detail_following.text = if (data?.following != null) data.following else "-"
                    progressBarDetail.visibility = View.INVISIBLE
                } catch (e : Exception){
                    Toast.makeText(this@DetailActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

        })
    }
    private fun setActionBar(){
        supportActionBar?.title = resources.getString(R.string.title_bar_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f
    }

    private fun favoriteState() {
        username = intent?.getStringExtra(EXTRA_NAME).toString()
        val result = userHelper.queryByLogin(username)
        val favorite = (1 .. result.count).map {
            result.apply {
                moveToNext()
                getInt(result.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_NAME_LOGIN))
            }
        }
        if (favorite.isNotEmpty()) isFavorite = true
    }

    private fun addFavoriteUser() {
        try {
            username = intent?.getStringExtra(EXTRA_NAME).toString()
            avatarUrl = intent?.getStringExtra(EXTRA_AVATAR_URL).toString()

            val values = ContentValues().apply {
                put(DatabaseContract.UserColumns.COLUMN_NAME_LOGIN, username)
                put(DatabaseContract.UserColumns.COLUMN_NAME_AVATAR_URL, avatarUrl)
            }
            userHelper.insert(values)
            val text = resources.getString(R.string.set_toast_add)
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            Log.d("onInsert:.. ", values.toString())
        } catch (e : SQLiteConstraintException) {
            e.printStackTrace()
        }
    }

    private fun removeFavoriteUser() {
        try {
            username = intent?.getStringExtra(EXTRA_NAME).toString()
            val result = userHelper.deleteByLogin(username)
            val text = resources.getString(R.string.set_toast_delete)
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

            Log.d("on:Remove..", result.toString())
        } catch (e : SQLiteConstraintException){
            e.printStackTrace()
        }
    }

    private fun setFavorite() {
        if (isFavorite) {
            menuItem.getItem(1)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite)
        } else menuItem.getItem(1)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_border)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_language){
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }
        if (item.itemId == R.id.action_favorite) {
            if (isFavorite) removeFavoriteUser() else addFavoriteUser()

            isFavorite = !isFavorite
            setFavorite()
        }
        return super.onOptionsItemSelected(item)
    }

}