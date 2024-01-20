package com.music.smartstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.music.smartstudy.presentation.dashboard.DashBoardScreen
import com.music.smartstudy.presentation.theme.SmartStudyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartStudyTheme {
                // A surface container using the 'background' color from the theme
                DashBoardScreen()
            }
        }
    }
}

