package com.example.dailyplanplus_uasml2_221351165.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.dailyplanplus_uasml2_221351165.data.database.AppDatabase
import com.example.dailyplanplus_uasml2_221351165.data.database.TaskEntity
import com.example.dailyplanplus_uasml2_221351165.data.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository
    val allTasks: LiveData<List<TaskEntity>>

    init {
        val dao = AppDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(dao)
        allTasks = repository.allTasks
    }

    fun insert(task: TaskEntity) = viewModelScope.launch {
        repository.insert(task)
    }

    fun update(task: TaskEntity) = viewModelScope.launch {
        repository.update(task)
    }

    fun delete(task: TaskEntity) = viewModelScope.launch {
        repository.delete(task)
    }

    fun searchTasks(query: String): LiveData<List<TaskEntity>> = repository.searchTasks(query)
}