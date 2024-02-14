package com.music.smartstudy.data.repository

import com.music.smartstudy.data.local.SessionDao
import com.music.smartstudy.data.local.SubjectDao
import com.music.smartstudy.data.local.TaskDao
import com.music.smartstudy.domain.model.Subject
import com.music.smartstudy.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubjectRepositoryImpl  @Inject constructor(
    private val subjectDao: SubjectDao,
    private val taskDao:TaskDao,
    private val sessionDao:SessionDao
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

    override suspend fun deleteSubject(subjectId: Int) {
       taskDao.deleteTaskBySubjectId(subjectId)
        sessionDao.deleteSessionsBySubjectId(subjectId)
        subjectDao.deleteSubject(subjectId)
    }

    override suspend fun getSubjectById(subjectInt: Int):Subject? {
        return subjectDao.getSubjectById(subjectInt)
    }

    override fun getAllSubjects(): Flow<List<Subject>> {
        return subjectDao.getAllSubject()
    }
}