package com.deboo.frulens.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.deboo.frulens.R
import com.deboo.frulens.data.SettingsPreferences
import com.deboo.frulens.data.dataStore
import com.deboo.frulens.helper.SettingViewModelFactory
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsFragment : Fragment() {

    private lateinit var switchTheme: SwitchMaterial
    private val settingViewModel: SettingsViewModel by viewModels {
        SettingViewModelFactory(SettingsPreferences.getInstance(requireContext().dataStore))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        switchTheme = view.findViewById(R.id.switch_theme)

        // Observe theme settings and update switch
        settingViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        // Handle switch toggle
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.saveThemeSetting(isChecked)
        }

        val setAcc: Button = view.findViewById(R.id.SetAcBtn)
        setAcc.setOnClickListener {
            // Handle image button click
            Toast.makeText(requireContext(), "Setting Account button clicked!", Toast.LENGTH_SHORT).show()
        }

        val out: Button = view.findViewById(R.id.btn_out)
        out.setOnClickListener {
            // Handle image button click
            Toast.makeText(requireContext(), "Sign Out button clicked!", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}