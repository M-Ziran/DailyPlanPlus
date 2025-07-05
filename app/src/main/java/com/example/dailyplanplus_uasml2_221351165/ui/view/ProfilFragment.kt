package com.example.dailyplanplus_uasml2_221351165.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dailyplanplus_uasml2_221351165.databinding.FragmentProfilBinding
import com.bumptech.glide.Glide
import com.example.dailyplanplus_uasml2_221351165.R

class ProfilFragment : Fragment() {

    private var _binding: FragmentProfilBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilBinding.inflate(inflater, container, false)

        setupProfileData()

        return binding.root
    }

    private fun setupProfileData() {
        binding.apply {
            profileName.text = "Nama: M.Ziran Maulana Ramadan"
            profileEmail.text = "Email: ziranmr7@gmail.com"
            profileUniv.text = "STT WASTUKANCANA PURWAKARTA"
            binding.btnInstagram.setOnClickListener {
                val url = "https://www.instagram.com/zirannnnn_/"
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = android.net.Uri.parse(url)
                }
                // Pastikan ada browser/Instagram yang bisa handle
                startActivity(intent)
            }

            // Load gambar bulat
            Glide.with(this@ProfilFragment)
                .load(R.drawable.profile)
                .circleCrop()
                .into(profileImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
