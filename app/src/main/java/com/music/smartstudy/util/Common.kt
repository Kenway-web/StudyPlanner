package com.music.smartstudy.util

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color
import com.music.smartstudy.presentation.theme.Green
import com.music.smartstudy.presentation.theme.Orange
import com.music.smartstudy.presentation.theme.Red
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class Priority(val title: String, val color: Color, val value: Int) {
    LOW(title = "Low", color = Green, value = 1),
    MEDIUM(title = "Medium", color = Orange, value = 2),
    HIGH(title = "High", color = Red, value = 3);

    companion object {
        fun fromInt(value: Int) = values().firstOrNull { it.value == value } ?: MEDIUM
    }
}


// extension function   (nullable function)  Long to String
fun Long?.changeMillisToDateString(): String {
    val date: LocalDate = this?.let {
        Instant
            .ofEpochMilli(it)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    } ?: LocalDate.now()
    return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
}


fun Long.toHours():Float{
    val hours = this.toFloat()/3600f
    return "%.2f".format(hours).toFloat()
}

sealed class SnackBarEvent(){

    data class ShowSnackBar(
        val message:String,
        val duration: SnackbarDuration = SnackbarDuration.Short
    ): SnackBarEvent()

}
