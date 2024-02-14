package com.music.smartstudy.presentation.subject

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.music.smartstudy.domain.repository.SessionRepository
import com.music.smartstudy.domain.repository.SubjectRepository
import com.music.smartstudy.domain.repository.TaskRepository
import com.music.smartstudy.presentation.navArgs
import com.music.smartstudy.util.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SubjectViewModel  @Inject constructor(
    private val subjectRepository:SubjectRepository,
    private val taskRepository:TaskRepository,
    private val sessionRepository: SessionRepository,
    savedStateHandle: SavedStateHandle
):ViewModel() {

    private val navArgs:SubjectScreenNavArgs = savedStateHandle.navArgs()

    init {
        fetchSubject()
    }

    private val _state = MutableStateFlow(SubjectState())

    val state = combine(
        _state,
        taskRepository.getUpcomingTasksForSubject(navArgs.subjectId),
        taskRepository.getCompletedTasksForSubject(navArgs.subjectId),
        sessionRepository.getRecentTenSessionsForSubject(navArgs.subjectId),
        sessionRepository.getTotalSessionDurationBySubject(navArgs.subjectId),
    ){ state,upcomingTask,completedTask,recentSessions,totalSessionDuration ->
        state.copy(
            upcomingTask=upcomingTask,
            completedTask = completedTask,
            recentSession = recentSessions,
            studiedHours = totalSessionDuration.toHours()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = SubjectState()
    )


    fun onEvent(event: SubjectEvent){
        when(event){
            SubjectEvent.DeleteSession -> TODO()
            SubjectEvent.DeleteSubject -> TODO()
            is SubjectEvent.OnDeleteSessionButtonClick -> TODO()
            is SubjectEvent.OnGoalStudyHoursChange -> TODO()
            is SubjectEvent.OnSubjectCardColorChange -> TODO()
            is SubjectEvent.OnSubjectNameChange -> TODO()
            is SubjectEvent.OnTaskIsCompleteChange -> TODO()
            SubjectEvent.UpdateSubject -> TODO()
        }
    }


    private fun fetchSubject(){
        viewModelScope.launch {
            subjectRepository.getSubjectById(navArgs.subjectId)?.let { subject->
               _state.update {
                  it.copy(
                      subjectName = subject.name,
                      goalStudyHours = subject.goalHours.toString(),
                      subjectCardColors = subject.colors.map { Color(it) },
                      currentSubjectId = subject.subjectId
                  )
               }
            }
        }
    }
}