package com.music.smartstudy.domain.repository

import com.music.smartstudy.domain.model.Subject
import kotlinx.coroutines.flow.Flow

interface SubjectRepository {

    suspend fun upsertSubject(subject: Subject)


    fun getTotalSubjectCount(): Flow<Int>


    fun getTotalGoalHours(): Flow<Float>


    suspend fun deleteSubject(subjectInt:Int)

    suspend fun getSubjectById(subjectInt:Int)


    fun getAllSubjects():Flow<List<Subject>>


}