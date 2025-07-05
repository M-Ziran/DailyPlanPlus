package com.example.dailyplanplus_uasml2_221351165.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.dailyplanplus_uasml2_221351165.data.database.TaskEntity

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Query("SELECT * FROM tasks ORDER BY date ASC")
    fun getAllTasks(): LiveData<List<TaskEntity>>

    @Insert
    suspend fun insertTask(task: TaskEntity)

    //query pencarian
    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY date ASC")
    fun searchTasks(query: String): LiveData<List<TaskEntity>>

}