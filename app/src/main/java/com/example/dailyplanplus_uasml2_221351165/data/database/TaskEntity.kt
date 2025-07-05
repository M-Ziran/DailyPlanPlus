package com.example.dailyplanplus_uasml2_221351165.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val date: String,    // Format: "yyyy-MM-dd"
    val time: String     // Format: "HH:mm"
)
