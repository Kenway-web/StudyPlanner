package com.music.smartstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.music.smartstudy.domain.model.Session
import com.music.smartstudy.domain.model.Subject
import com.music.smartstudy.domain.model.Task
import com.music.smartstudy.presentation.dashboard.DashBoardScreen
import com.music.smartstudy.presentation.subject.SubjectScreen
import com.music.smartstudy.presentation.theme.SmartStudyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartStudyTheme {
                // A surface container using the 'background' color from the theme
//                DashBoardScreen()
                SubjectScreen()
            }
        }
    }
}

val subjecListt = listOf(
    Subject("English", 10f, Subject.subjectCardColors[0], subjectId = 0),
    Subject("Math", 10f, Subject.subjectCardColors[1],subjectId = 0),
    Subject("Physics", 10f, Subject.subjectCardColors[2],subjectId = 0),
    Subject("Chemistry", 10f, Subject.subjectCardColors[4],subjectId = 0),
    Subject("Biology", 10f, Subject.subjectCardColors[3],subjectId = 0)
)


val tasks = listOf(
    Task(
        title = "Prepare Notes",
        description = "",
        dueDate = 0L,
        priority = 1,
        relatedToSubject = "",
        isComplete = false,
        taskSubjectId = 0,
        taskId = 1
    ),
    Task(
        title = "Study Notes",
        description = "",
        dueDate = 0L,
        priority = 2,
        relatedToSubject = "",
        isComplete = false,
        taskSubjectId = 0,
        taskId = 1
    ),
    Task(
        title = "Study Chemistry",
        description = "",
        dueDate = 0L,
        priority = 2,
        relatedToSubject = "",
        isComplete = false,
        taskSubjectId = 0,
        taskId = 1
    ),
    Task(
        title = "Study  Math",
        description = "",
        dueDate = 0L,
        priority = 2,
        relatedToSubject = "",
        isComplete = false,
        taskSubjectId = 0,
        taskId = 1
    )
)

val sessions = listOf(
    Session(
        relatedToSubject = "English",
        date = 0L,
        duration = 2,
        sessionSubjectId =0 ,
        sessionId = 0
    ),
    Session(
        relatedToSubject = "Math",
        date = 0L,
        duration = 2,
        sessionSubjectId =0 ,
        sessionId = 0
    ),
    Session(
        relatedToSubject = "Chemistry",
        date = 0L,
        duration = 2,
        sessionSubjectId =0 ,
        sessionId = 0
    ),
    Session(
        relatedToSubject = "Hindi",
        date = 0L,
        duration = 2,
        sessionSubjectId =0 ,
        sessionId = 0
    )
)