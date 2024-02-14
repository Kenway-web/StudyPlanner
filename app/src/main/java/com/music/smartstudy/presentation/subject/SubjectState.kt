package com.music.smartstudy.presentation.subject

import androidx.compose.ui.graphics.Color
import com.music.smartstudy.domain.model.Session
import com.music.smartstudy.domain.model.Subject
import com.music.smartstudy.domain.model.Task

data class SubjectState(
    val currentSubjectId:Int?=null,
    val subjectName:String="",
    val goalStudyHours:String="",
    val subjectCardColors:List<Color> = Subject.subjectCardColors.random(),
    val studiedHours:Float=0f,
    val recentSession:List<Session> = emptyList(),
    val upcomingTask:List<Task> = emptyList(),
    val completedTask:List<Task> = emptyList(),
    val session:Session?=null,
    val progress:Float= 0f,
    val isLoading:Boolean=false
)
