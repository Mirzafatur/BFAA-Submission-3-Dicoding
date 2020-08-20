package com.example.githubuserapi.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.githubuserapi.R
import com.example.githubuserapi.main.fragment.PreferenceFragment

class SettingPreferenceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_preference)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.alarm)

        supportFragmentManager.beginTransaction()
            .add(R.id.setting_preference, PreferenceFragment())
            .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }
}