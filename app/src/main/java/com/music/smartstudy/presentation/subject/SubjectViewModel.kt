package com.music.smartstudy.presentation.subject

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.music.smartstudy.domain.model.Subject
import com.music.smartstudy.domain.model.Task
import com.music.smartstudy.domain.repository.SessionRepository
import com.music.smartstudy.domain.repository.SubjectRepository
import com.music.smartstudy.domain.repository.TaskRepository
import com.music.smartstudy.presentation.navArgs
import com.music.smartstudy.util.SnackBarEvent
import com.music.smartstudy.util.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val taskRepository: TaskRepository,
    private val sessionRepository: SessionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArgs: SubjectScreenNavArgs = savedStateHandle.navArgs()

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
    ) { state, upcomingTask, completedTask, recentSessions, totalSessionDuration ->
        state.copy(
            upcomingTask = upcomingTask,
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
    private val _snackBarEventFlow =
        MutableSharedFlow<SnackBarEvent>()  // using MutableSharedFlow as it doesn't contain any value by default
    val snackBarEventFlow = _snackBarEventFlow.asSharedFlow()


    fun onEvent(event: SubjectEvent) {
        when (event) {
            SubjectEvent.DeleteSession -> {}
            is SubjectEvent.OnDeleteSessionButtonClick -> {}
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

            is SubjectEvent.OnTaskIsCompleteChange -> updateTask(event.task)
            SubjectEvent.UpdateSubject -> updateSubject()
            SubjectEvent.UpdateProgress -> {
                val goalStudyHours = state.value.goalStudyHours.toFloatOrNull() ?: 1f
                _state.update {
                    it.copy(
                        progress = (state.value.studiedHours / goalStudyHours).coerceIn(0f, 1f)
                    )
                }
            }

            SubjectEvent.DeleteSubject -> deleteSubject()
        }
    }

    private fun updateSubject() {
        viewModelScope.launch {

            try {

                subjectRepository.upsertSubject(subject = Subject(subjectId = state.value.currentSubjectId,
                    name = state.value.subjectName,
                    goalHours = state.value.goalStudyHours.toFloatOrNull() ?: 1f,
                    colors = state.value.subjectCardColors.map { it.toArgb() }))
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        "Subject Updated Successfully"
                    )
                )
            } catch (e: Exception) {
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        "Couldn't Update subject. ${e.message}", SnackbarDuration.Long
                    )
                )
            }


        }
    }


    private fun fetchSubject() {
        viewModelScope.launch {
            subjectRepository.getSubjectById(navArgs.subjectId)?.let { subject ->
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


    private fun deleteSubject() {
        viewModelScope.launch {

            try {
                val currentSubjectId = state.value.currentSubjectId
                if (currentSubjectId != null) {
                    withContext(Dispatchers.IO) {
                        subjectRepository.deleteSubject(subjectId = currentSubjectId)
                    }
                    _snackBarEventFlow.emit(
                        SnackBarEvent.ShowSnackBar("Subject Deleted Successfully.")
                    )
                    _snackBarEventFlow.emit(SnackBarEvent.NavigateUp)
                } else {
                    _snackBarEventFlow.emit(
                        SnackBarEvent.ShowSnackBar("No Subject to delete")
                    )
                }
            } catch (e: Exception) {
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(message = "Subject Not Deleted. ${e.message}")
                )
            }

        }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            try {

                taskRepository.upsertTask(
                    task = task.copy(isComplete = !task.isComplete)
                )
                if (task.isComplete) {
                    _snackBarEventFlow.emit(
                        SnackBarEvent.ShowSnackBar(
                            "Saved in Upcoming Task."
                        )
                    )
                } else {
                    _snackBarEventFlow.emit(
                        SnackBarEvent.ShowSnackBar(
                            "Saved in Completed Task."
                        )
                    )
                }


            } catch (e: Exception) {
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        "Couldn't update task. ${e.message}", SnackbarDuration.Long

                    )
                )
            }
        }
    }
}