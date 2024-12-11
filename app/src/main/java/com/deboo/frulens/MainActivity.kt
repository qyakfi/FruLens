package com.deboo.frulens

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.deboo.frulens.databinding.ActivityMainBinding
import com.deboo.frulens.helper.SettingViewModelFactory
import com.deboo.frulens.ui.setting.SettingsPreferences
import com.deboo.frulens.ui.setting.SettingsViewModel
import com.deboo.frulens.ui.setting.dataStore
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)

        val settingsViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(SettingsPreferences.getInstance(dataStore))
        )[SettingsViewModel::class.java]

        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                settingsViewModel.saveThemeSetting(true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                settingsViewModel.saveThemeSetting(false)
            }
        }
    }
}