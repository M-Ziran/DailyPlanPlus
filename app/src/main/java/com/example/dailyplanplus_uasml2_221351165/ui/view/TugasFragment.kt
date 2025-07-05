package com.example.dailyplanplus_uasml2_221351165.ui.view

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyplanplus_uasml2_221351165.data.database.TaskEntity
import com.example.dailyplanplus_uasml2_221351165.databinding.DialogAddTaskBinding
import com.example.dailyplanplus_uasml2_221351165.databinding.FragmentTugasBinding
import com.example.dailyplanplus_uasml2_221351165.notification.NotificationReceiver
import com.example.dailyplanplus_uasml2_221351165.ui.adapter.TaskAdapter
import com.example.dailyplanplus_uasml2_221351165.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

class TugasFragment : Fragment() {

    private var _binding: FragmentTugasBinding? = null
    private val binding get() = _binding!!
    private val taskViewModel: TaskViewModel by viewModels()
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTugasBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observeTasks()
        setupSearch()

        binding.fabAddTask.setOnClickListener {
            showAddTaskDialog()
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(emptyList()) { task ->
            showEditTaskDialog(task)
        }
        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = taskAdapter
        }
    }

    private fun observeTasks() {
        taskViewModel.allTasks.observe(viewLifecycleOwner) { tasks ->
            taskAdapter.updateData(tasks)

            // Ubah teks deskripsi berdasarkan jumlah tugas
            if (tasks.isEmpty()) {
                binding.textViewDesc.text = "Belum ada tugas"
            } else {
                binding.textViewDesc.text = "Kamu memiliki ${tasks.size} tugas yang harus diselesaikan"
            }
        }
    }


    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    taskViewModel.searchTasks(it).observe(viewLifecycleOwner) { results ->
                        taskAdapter.updateData(results)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    taskViewModel.searchTasks(it).observe(viewLifecycleOwner) { results ->
                        taskAdapter.updateData(results)
                    }
                }
                return true
            }
        })
    }

    private fun showAddTaskDialog() {
        val dialogBinding = DialogAddTaskBinding.inflate(layoutInflater)

        // TimePicker setup
        dialogBinding.etTime.setOnClickListener {
            val c = Calendar.getInstance()
            TimePickerDialog(requireContext(), { _, hour, minute ->
                dialogBinding.etTime.setText(String.format("%02d:%02d", hour, minute))
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }

        dialogBinding.etDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, y, m, d ->
                val cal = Calendar.getInstance()
                cal.set(y, m, d)
                val formatter = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                dialogBinding.etDate.setText(formatter.format(cal.time))
            }, year, month, day)

            datePicker.show()
        }


        AlertDialog.Builder(requireContext())
            .setTitle("Tambah Tugas")
            .setView(dialogBinding.root)
            .setPositiveButton("Simpan") { _, _ ->
                val title = dialogBinding.etTitle.text.toString().trim()
                val desc = dialogBinding.etDesc.text.toString().trim()
                val date = dialogBinding.etDate.text.toString().trim()
                val time = dialogBinding.etTime.text.toString().trim()

                // Validasi semua field wajib diisi
                if (title.isEmpty() || date.isEmpty() || time.isEmpty()) {
                    Toast.makeText(context, "Semua field wajib diisi!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Validasi format tanggal dan waktu
                val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                val timeFormat = SimpleDateFormat("HH:mm", Locale("id", "ID"))
                val isDateValid = try {
                    dateFormat.isLenient = false
                    dateFormat.parse(date)
                    true
                } catch (e: Exception) {
                    false
                }

                val isTimeValid = try {
                    timeFormat.isLenient = false
                    timeFormat.parse(time)
                    true
                } catch (e: Exception) {
                    false
                }

                if (!isDateValid || !isTimeValid) {
                    Toast.makeText(context, "Format tanggal atau jam tidak valid!", Toast.LENGTH_LONG).show()
                    return@setPositiveButton
                }

                // Simpan jika valid
                val task = TaskEntity(title = title, description = desc, date = date, time = time)
                taskViewModel.insert(task)
                scheduleNotification(title, desc, date, time)
                Toast.makeText(context, "Tugas ditambahkan", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }


    private fun showEditTaskDialog(task: TaskEntity) {
        val dialogBinding = DialogAddTaskBinding.inflate(layoutInflater)

        dialogBinding.etTitle.setText(task.title)
        dialogBinding.etDesc.setText(task.description)
        dialogBinding.etDate.setText(task.date)
        dialogBinding.etTime.setText(task.time)

        dialogBinding.etDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, y, m, d ->
                val cal = Calendar.getInstance()
                cal.set(y, m, d)
                val formatter = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                dialogBinding.etDate.setText(formatter.format(cal.time))
            }, year, month, day)

            datePicker.show()
        }


        dialogBinding.etDate.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, year, month, day ->
                val cal = Calendar.getInstance()
                cal.set(year, month, day)
                val format = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                dialogBinding.etDate.setText(format.format(cal.time))
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }


        AlertDialog.Builder(requireContext())
            .setTitle("Edit Tugas")
            .setView(dialogBinding.root)
            .setPositiveButton("Update") { _, _ ->
                val newTitle = dialogBinding.etTitle.text.toString()
                val newDesc = dialogBinding.etDesc.text.toString()
                val newDate = dialogBinding.etDate.text.toString()
                val newTime = dialogBinding.etTime.text.toString()

                if (newTitle.isNotEmpty() && newDate.isNotEmpty() && newTime.isNotEmpty()) {
                    val updatedTask = task.copy(
                        title = newTitle,
                        description = newDesc,
                        date = newDate,
                        time = newTime
                    )
                    taskViewModel.update(updatedTask)
                    scheduleNotification(newTitle, newDesc, newDate, newTime)
                    Toast.makeText(context, "Tugas diperbarui", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Semua field wajib diisi!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .setNeutralButton("Hapus") { _, _ ->
                taskViewModel.delete(task)
                Toast.makeText(context, "Tugas dihapus", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun scheduleNotification(title: String, message: String, date: String, time: String) {
        val sdf = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale("id", "ID"))
        val dateTime = "$date $time"
        val triggerTime = try {
            sdf.parse(dateTime)?.time
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Format tanggal/waktu salah", Toast.LENGTH_SHORT).show()
            return
        }

        if (triggerTime != null && triggerTime > System.currentTimeMillis()) {
            val intent = Intent(requireContext(), NotificationReceiver::class.java).apply {
                putExtra("title", title)
                putExtra("message", message)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                System.currentTimeMillis().toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        } else {
            Toast.makeText(context, "Waktu tugas tidak valid!", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
