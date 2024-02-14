package com.music.smartstudy.presentation.subject

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.music.smartstudy.domain.model.Subject
import com.music.smartstudy.domain.repository.SessionRepository
import com.music.smartstudy.domain.repository.SubjectRepository
import com.music.smartstudy.domain.repository.TaskRepository
import com.music.smartstudy.presentation.navArgs
import com.music.smartstudy.util.SnackBarEvent
import com.music.smartstudy.util.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
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


    // snack bar event flow  with snackBar sealed class
    private val _snackBarEventFlow = MutableSharedFlow<SnackBarEvent>()  // using MutableSharedFlow as it doesn't contain any value by default
    val snackBarEventFlow = _snackBarEventFlow.asSharedFlow()



    fun onEvent(event: SubjectEvent){
        when(event){
            SubjectEvent.DeleteSession -> TODO()
            SubjectEvent.DeleteSubject -> TODO()
            is SubjectEvent.OnDeleteSessionButtonClick -> TODO()
            is SubjectEvent.OnGoalStudyHoursChange -> {
                _state.update {
                    it.copy(goalStudyHours = event.hours)
                }
            }
            is SubjectEvent.OnSubjectCardColorChange -> {
               _state.update {
                   it.copy(subjectCardColors = event.color)
               }
            }
            is SubjectEvent.OnSubjectNameChange -> {
                _state.update {
                    it.copy(subjectName = event.name)
                }
            }
            is SubjectEvent.OnTaskIsCompleteChange -> TODO()
            SubjectEvent.UpdateSubject -> updateSubject()
        }
    }

    private fun updateSubject() {
        viewModelScope.launch {

            try {
                subjectRepository.upsertSubject(
                    subject = Subject(
                        subjectId = state.value.currentSubjectId,
                        name = state.value.subjectName,
                        goalHours = state.value.goalStudyHours.toFloatOrNull()?:1f,
                        colors = state.value.subjectCardColors.map { it.toArgb() }
                    )
                )
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        "Subject Updated Successfully"
                    )
                )
            }
            catch (e:Exception){
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        "Couldn't Update subject. ${e.message}",
                        SnackbarDuration.Long
                    )
                )
            }


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