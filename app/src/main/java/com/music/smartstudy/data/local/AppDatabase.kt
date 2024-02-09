package com.music.smartstudy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.music.smartstudy.domain.model.Session
import com.music.smartstudy.domain.model.Subject
import com.music.smartstudy.domain.model.Task


@Database(
    entities = [Subject::class, Session::class, Task::class], version = 1
)
@TypeConverters(ColorListConvertor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao

    abstract fun sessionDao(): SessionDao

    abstract fun taskDao(): TaskDao


}