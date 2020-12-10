package com.example.todolist

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val appSettingsPref: SharedPreferences = getSharedPreferences("AppSettingsPref", 0)
        val sharedPrefEdit: SharedPreferences.Editor = appSettingsPref.edit()
        val isNightMode: Boolean = appSettingsPref.getBoolean("NightMode", false)

        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            settingsDayNight.isChecked = true
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            settingsDayNight.isChecked = false
        }

        settingsDayNight.setOnClickListener {
            if (isNightMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefEdit.putBoolean("NightMode", false)
                sharedPrefEdit.apply()
            }

            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefEdit.putBoolean("NightMode", true)
                sharedPrefEdit.apply()
            }

        }
    }
}