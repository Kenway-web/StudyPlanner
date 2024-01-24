package com.music.smartstudy.util

import androidx.compose.ui.graphics.Color
import com.music.smartstudy.presentation.theme.Green
import com.music.smartstudy.presentation.theme.Orange
import com.music.smartstudy.presentation.theme.Red

enum class Priority(val title: String, val color: Color, val value: Int) {
    LOW(title = "Low", color = Green, value = 1),
    MEDIUM(title = "Medium", color = Orange, value = 2),
    HIGH(title = "High", color = Red, value = 3);

    companion object {
        fun fromInt(value: Int) = values().firstOrNull { it.value == value } ?: MEDIUM
    }
}