package com.example.dailyplanplus_uasml2_221351165.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyplanplus_uasml2_221351165.databinding.ItemTaskBinding
import com.example.dailyplanplus_uasml2_221351165.data.database.TaskEntity
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter(
    private var tasks: List<TaskEntity>,
    private val onItemClick: (TaskEntity) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.binding.apply {
            tvTitle.text = task.title
            tvDesc.text = task.description
            tvDate.text = formatTanggalIndonesia(task.date, task.time)

            root.setOnClickListener {
                onItemClick(task)
            }
        }
    }

    fun updateData(newTasks: List<TaskEntity>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    // 🔧 Fungsi bantu untuk format tanggal-waktu lokal
    private fun formatTanggalIndonesia(date: String, time: String): String {
        return try {
            val inputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
            val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
            val parsedDate = inputFormat.parse(date)
            val hariTanggal = outputFormat.format(parsedDate!!) // "Senin, 17 Juni 2025"
            "$hariTanggal • $time WIB"
        } catch (e: Exception) {
            "$date • $time"
        }
    }
}
