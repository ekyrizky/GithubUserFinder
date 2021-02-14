package com.ekyrizky.githubezfinder.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import androidx.core.content.edit
import com.ekyrizky.githubezfinder.R
import com.ekyrizky.githubezfinder.alarm.AlarmReceiver
import com.ekyrizky.githubezfinder.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var settingBinding: ActivitySettingBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var alarmReceiver: AlarmReceiver

    companion object {
        const val PREFS_NAME = "setting_pref"
        private const val REMINDER = "reminder"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingBinding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(settingBinding.root)
        setSupportActionBar(settingBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_white_24dp)

        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        alarmReceiver = AlarmReceiver()

        settingBinding.btnLanguage.setOnClickListener(this)

        settingBinding.swReminder.isChecked = preferences.getBoolean(REMINDER, false)
        settingBinding.swReminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                alarmReceiver.setRepeatingAlarm(applicationContext)
            } else {
                alarmReceiver.setCancelAlarm(applicationContext)
            }
            saveChange(isChecked)
        }
    }

    private fun saveChange(checked: Boolean) {
        preferences.edit { putBoolean(REMINDER, checked) }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            settingBinding.btnLanguage.id -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}