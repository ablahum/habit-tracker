package com.dicoding.habitapp.setting

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.dicoding.habitapp.R
import com.dicoding.habitapp.utils.DarkMode

class SettingsActivity : AppCompatActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                createToast("Notifications permission granted")
            } else {
                createToast("Notifications will not show without permission")
            }
        }

    private fun createToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT > 32) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            findPreference<Preference>(getString(R.string.pref_key_dark))?.setOnPreferenceChangeListener { _, newValue ->
                handleDarkModeChange(newValue.toString())
                true
            }
        }

        private fun handleDarkModeChange(newValue: String) {
            val selectedTheme = when (newValue) {
                getString(R.string.pref_dark_follow_system) -> DarkMode.FOLLOW_SYSTEM.value
                getString(R.string.pref_dark_on) -> DarkMode.ON.value
                getString(R.string.pref_dark_off) -> DarkMode.OFF.value
                else -> AppCompatDelegate.getDefaultNightMode()
            }

            if (selectedTheme != AppCompatDelegate.getDefaultNightMode()) {
                Log.d("SettingsFragment", "Selected theme: $selectedTheme")
                updateTheme(selectedTheme)
            }
        }

        private fun updateTheme(mode: Int) {
            if (isAdded) {
                AppCompatDelegate.setDefaultNightMode(mode)
                requireActivity().recreate()
            }
        }
    }
}
