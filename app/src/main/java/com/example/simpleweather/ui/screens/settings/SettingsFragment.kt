package com.example.simpleweather.ui.screens.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.simpleweather.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}