package com.example.dailyplanplus_uasml2_221351165.ui.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailyplanplus_uasml2_221351165.R


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dailyplanplus_uasml2_221351165.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Daftar nama tab
    private val tabTitles = listOf("Tugas", "Jadwal", "Profil")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menghubungkan view binding ke layout activity_main.xml
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set adapter untuk ViewPager
        val adapter = TabAdapter(this)
        binding.viewPager.adapter = adapter

        // Hubungkan TabLayout dan ViewPager
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "dailyplan_channel",
                "DailyPlan+ Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }


    }
}