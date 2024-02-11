package com.music.smartstudy.di

import com.music.smartstudy.data.repository.SessionRepositoryImpl
import com.music.smartstudy.data.repository.SubjectRepositoryImpl
import com.music.smartstudy.data.repository.TaskRepositoryImpl
import com.music.smartstudy.domain.repository.SessionRepository
import com.music.smartstudy.domain.repository.SubjectRepository
import com.music.smartstudy.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


    @Singleton
    @Binds
    abstract  fun bindSubjectRepository(
        impl:SubjectRepositoryImpl
    ):SubjectRepository

    @Singleton
    @Binds
    abstract fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ):TaskRepository

    @Singleton
    @Binds
    abstract fun bindSessionRepository(
        impl: SessionRepositoryImpl
    ):SessionRepository

}