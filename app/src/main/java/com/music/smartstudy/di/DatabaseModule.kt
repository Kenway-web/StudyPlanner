package com.music.smartstudy.di

import android.app.Application
import androidx.room.Room
import com.music.smartstudy.data.local.AppDatabase
import com.music.smartstudy.data.local.SessionDao
import com.music.smartstudy.data.local.SubjectDao
import com.music.smartstudy.data.local.TaskDao
import com.music.smartstudy.domain.model.Subject
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)  // defines the component where module should be installed
object DatabaseModule {


    @Provides    // used on functions that they are used for providing dependencies.
    @Singleton
    fun provideDatabase(
        application: Application
    ): AppDatabase {
        return Room
            .databaseBuilder(
                application,
                AppDatabase::class.java,
                "studysmart.db"
            )
            .build()
    }


    // object of daos

    @Provides
    @Singleton
    fun provideSubjectDao(database: AppDatabase):SubjectDao{
        return  database.subjectDao()
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: AppDatabase):TaskDao{
        return  database.taskDao()
    }

    @Provides
    @Singleton
    fun provideSessionDao(database: AppDatabase):SessionDao{
        return  database.sessionDao()
    }
}