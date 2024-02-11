package com.music.smartstudy.data.repository

import com.music.smartstudy.data.local.SessionDao
import com.music.smartstudy.domain.model.Session
import com.music.smartstudy.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class SessionRepositoryImpl  @Inject constructor(
    private val sessionDao: SessionDao
):SessionRepository{
    override suspend fun insertSession(session: Session) {
            sessionDao.insertSession(session)
        }

    override suspend fun deleteSession(session: Session) {
        TODO("Not yet implemented")
    }

    override fun getAllSessions(): Flow<List<Session>> {
        TODO("Not yet implemented")
    }

    override fun getRecentFiveSessions(): Flow<List<Session>> {
       return sessionDao.getAllSessions().take(5)
    }

    override fun getRecentTenSessionsForSubject(subjectId: Int): Flow<List<Session>> {
        TODO("Not yet implemented")
    }

    override fun getTotalSessionDuration(): Flow<Long> {
       return sessionDao.getTotalSessionsDuration()
    }

    override fun getTotalSessionDurationBySubjectId(subjectId: Int): Flow<Long> {
        TODO("Not yet implemented")
    }

}