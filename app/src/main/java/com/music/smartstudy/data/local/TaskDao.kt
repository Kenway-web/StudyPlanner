package com.music.smartstudy.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.music.smartstudy.domain.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Upsert
    suspend fun upsertTask(task: Task)

    @Query("DELETE FROM TASK WHERE taskId = :taskId")
    suspend fun deleteTask(taskId:Int)

    @Query("DELETE FROM TASK WHERE taskSubjectId = :subjectId")
    suspend fun deleteTaskBySubjectId(subjectId:Int)

    @Query("SELECT * FROM TASK WHERE taskId = :taskId")
    suspend fun getTaskById(taskId: Int):Task?

    @Query("SELECT * FROM TASK WHERE taskSubjectId = :subjectId")
    suspend fun getTasksBySubjectId(subjectId: Int):Flow<List<Task>>

    @Query("SELECT * FROM TASK")
    fun getAllTasks():Flow<List<Task>>

}