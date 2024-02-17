package com.music.smartstudy.presentation.dashboard

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.music.smartstudy.domain.model.Session
import com.music.smartstudy.domain.model.Subject
import com.music.smartstudy.domain.model.Task
import com.music.smartstudy.domain.repository.SessionRepository
import com.music.smartstudy.domain.repository.SubjectRepository
import com.music.smartstudy.domain.repository.TaskRepository
import com.music.smartstudy.util.SnackBarEvent
import com.music.smartstudy.util.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val sessionRepository: SessionRepository,
    private val taskRepository: TaskRepository
):ViewModel() {
    // bridge b/w UI and repository


    private val _state = MutableStateFlow(DashBoardState())

    val state = combine(
        _state,
        subjectRepository.getTotalSubjectCount(),
        subjectRepository.getTotalGoalHours(),
        subjectRepository.getAllSubjects(),
        sessionRepository.getTotalSessionDuration()

    ) { state,subjectCount,goalHours,subjects,TotalsessionDuration ->
        state.copy(
            totalSubjectCount = subjectCount,
            totalGoalStudyHours = goalHours,
            subjects = subjects,
            totalStudiedHours = TotalsessionDuration.toHours()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis=5000),
        initialValue = DashBoardState()
    )

    val tasks : StateFlow<List<Task>> = taskRepository.getAllUpcomingTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList( )
        )

    val recentSession : StateFlow<List<Session>> = sessionRepository.getRecentFiveSessions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList( )
        )

    private val _snackBarEventFlow = MutableSharedFlow<SnackBarEvent>()  // using MutableSharedFlow as it doesn't contain any value by default
    val snackBarEventFlow = _snackBarEventFlow.asSharedFlow()



    fun onEvent(event: DashBoardEvent){
        when(event){
            DashBoardEvent.DeleteSession -> TODO()
            is DashBoardEvent.OnDeleteSessionButtonClick -> {
                _state.update {
                    it.copy(session = event.session)
                }
            }
            is DashBoardEvent.OnGoalStudyHoursChange -> {
                _state.update {
                    it.copy(goalStudyHours = event.hours)
                }
            }
            is DashBoardEvent.OnSubjectCardColorChange ->{
                _state.update {
                    it.copy(subjectCardColors = event.colors)
                }
            }
            is DashBoardEvent.OnSubjectNameChange -> {
                _state.update {
                    it.copy(subjectName = event.name)
                }
            }
            DashBoardEvent.SaveSubject -> saveSubject()
            is DashBoardEvent.OnTaskIsCompleteChange -> {
                updateTask(event.task)
            }
        }
    }

    private fun updateTask(task:Task) {
        viewModelScope.launch {
            try {

                taskRepository.upsertTask(
                    task=task.copy(isComplete = !task.isComplete)
                )
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        "Saved in Completed Task."
                    )
                )
            }
            catch (e:Exception){
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        "Couldn't update task. ${e.message}",
                        SnackbarDuration.Long

                    )
                )
            }
        }
    }

    private fun saveSubject() {
        viewModelScope.launch {
            try {
                subjectRepository.upsertSubject(
                    subject = Subject(
                        name = state.value.subjectName,
                        goalHours = state.value.goalStudyHours.toFloatOrNull() ?:1f,
                        colors = state.value.subjectCardColors.map { it.toArgb() },

                        )
                )
                _state.update {
                    it.copy(
                        subjectName = "",
                        goalStudyHours = "",
                        subjectCardColors = Subject.subjectCardColors.random()
                    )
                }
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        "Subject Saved Successfully"
                    )
                )
            }
            catch (e:Exception){
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        "Couldn't save subject. ${e.message}",
                        SnackbarDuration.Long

                    )
                )
            }
        }
    }

}