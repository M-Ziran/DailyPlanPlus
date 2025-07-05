package com.example.dailyplanplus_uasml2_221351165.ui.view


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dailyplanplus_uasml2_221351165.ui.view.JadwalFragment
import com.example.dailyplanplus_uasml2_221351165.ui.view.ProfilFragment
import com.example.dailyplanplus_uasml2_221351165.ui.view.TugasFragment

// Adapter ini akan menghubungkan ViewPager dengan fragment-fragment yang ditampilkan
class TabAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3 // Jumlah tab/fragments

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TugasFragment()
            1 -> JadwalFragment()
            2 -> ProfilFragment()
            else -> TugasFragment()
        }
    }
}