package com.example.dailyplanplus_uasml2_221351165.ui.view

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyplanplus_uasml2_221351165.databinding.FragmentJadwalBinding
import com.example.dailyplanplus_uasml2_221351165.notification.NotificationReceiver
import com.example.dailyplanplus_uasml2_221351165.ui.adapter.TaskAdapter
import com.example.dailyplanplus_uasml2_221351165.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

class JadwalFragment : Fragment() {

    private var _binding: FragmentJadwalBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel: TaskViewModel by viewModels()
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJadwalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.title.text = "Jadwal Kegiatan"
        binding.desc.text = "Daftar kegiatan mingguanmu akan muncul di sini"

        setupRecyclerView()

        taskViewModel.allTasks.observe(viewLifecycleOwner) { tasks ->
            taskAdapter.updateData(tasks)

            // Update teks jika kosong
            binding.desc.text = if (tasks.isEmpty()) {
                "Belum ada jadwal"
            } else {
                "Kamu memiliki ${tasks.size} kegiatan yang terjadwal"
            }

            // Jadwalkan notifikasi
            for (task in tasks) {
                scheduleNotification(
                    context = requireContext(),
                    title = task.title,
                    message = task.description,
                    date = task.date,
                    time = task.time
                )
            }
        }
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(emptyList()) {
            // Tidak perlu aksi klik di halaman jadwal
        }
        binding.rvJadwal.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = taskAdapter
        }
    }

    private fun scheduleNotification(context: Context, title: String, message: String, date: String, time: String) {
        val sdf = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale("id", "ID"))
        val dateTime = "$date $time"
        val triggerMillis = try {
            sdf.parse(dateTime)?.time
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        if (triggerMillis != null && triggerMillis > System.currentTimeMillis()) {
            val intent = Intent(context, NotificationReceiver::class.java).apply {
                putExtra("title", title)
                putExtra("message", message)
            }

            val uniqueId = (title + date + time).hashCode()

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                uniqueId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
