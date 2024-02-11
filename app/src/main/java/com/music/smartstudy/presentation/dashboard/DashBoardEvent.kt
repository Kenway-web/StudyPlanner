package com.music.smartstudy.presentation.dashboard

import androidx.compose.ui.graphics.Color
import com.music.smartstudy.domain.model.Session
import com.music.smartstudy.domain.model.Task

sealed class DashBoardEvent{

    data object SaveSubject: DashBoardEvent()

    data object DeleteSession  : DashBoardEvent()

    data class OnDeleteSessionButtonClick(val session: Session): DashBoardEvent()

    data class OnTaskIsCompleteChange(val task: Task): DashBoardEvent()

    data class OnSubjectCardColorChange(val colors:List<Color>):DashBoardEvent()

    data class OnSubjectNameChange(val name:String):DashBoardEvent()

    data class OnGoalStudyHoursChange(val hours:String):DashBoardEvent()


    // for some value input from user use data class
}

