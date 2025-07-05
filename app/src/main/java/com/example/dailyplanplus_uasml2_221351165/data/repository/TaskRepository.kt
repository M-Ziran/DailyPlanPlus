package com.example.dailyplanplus_uasml2_221351165.data.repository

import androidx.lifecycle.LiveData
import com.example.dailyplanplus_uasml2_221351165.data.database.TaskDao
import com.example.dailyplanplus_uasml2_221351165.data.database.TaskEntity

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: LiveData<List<TaskEntity>> = taskDao.getAllTasks()

    suspend fun insert(task: TaskEntity) = taskDao.insert(task)

    suspend fun update(task: TaskEntity) {
        taskDao.update(task)
    }

    suspend fun delete(task: TaskEntity) {
        taskDao.delete(task)
    }

    fun searchTasks(query: String): LiveData<List<TaskEntity>> = taskDao.searchTasks(query)

}