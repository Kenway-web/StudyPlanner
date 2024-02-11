package com.music.smartstudy.data.repository

import com.music.smartstudy.data.local.SubjectDao
import com.music.smartstudy.domain.model.Subject
import com.music.smartstudy.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubjectRepositoryImpl  @Inject constructor(
    private val subjectDao: SubjectDao
) :SubjectRepository{
    override suspend fun upsertSubject(subject: Subject) {
        subjectDao.upsertSubject(subject)
    }

    override fun getTotalSubjectCount(): Flow<Int> {
        return subjectDao.getTotalSubjectCount()
    }

    override fun getTotalGoalHours(): Flow<Float> {
        return  subjectDao.getTotalGoalHours()
    }

    override suspend fun deleteSubject(subjectInt: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getSubjectById(subjectInt: Int) {
        TODO("Not yet implemented")
    }

    override fun getAllSubjects(): Flow<List<Subject>> {
        return subjectDao.getAllSubject()
    }
}