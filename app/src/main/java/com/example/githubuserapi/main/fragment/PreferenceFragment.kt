package com.example.githubuserapi.main.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.githubuserapi.R
import com.example.githubuserapi.notification.AlarmReceiver

class PreferenceFragment : PreferenceFragmentCompat() {

    private lateinit var switchPreference : SwitchPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference)

        val alarmReceiver = AlarmReceiver()

        switchPreference = findPreference(resources.getString(R.string.notification))!!

        switchPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener{ _, _ ->
            if (switchPreference.isChecked) {
                activity?.let { alarmReceiver.cancelAlarm(it) }
                val text = resources.getString(R.string.off_alarm_set)
                Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
                switchPreference.isChecked = false
            } else {
                activity?.let { alarmReceiver.setRepeatAlarm(it) }
                val text = resources.getString(R.string.alarm_set)
                Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
                switchPreference.isChecked = true
            }
            false
        }
    }
}