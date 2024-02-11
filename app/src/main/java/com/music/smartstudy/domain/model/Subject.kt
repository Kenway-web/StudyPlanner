package com.music.smartstudy.domain.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.music.smartstudy.presentation.theme.gradient1
import com.music.smartstudy.presentation.theme.gradient2
import com.music.smartstudy.presentation.theme.gradient3
import com.music.smartstudy.presentation.theme.gradient4
import com.music.smartstudy.presentation.theme.gradient5


@Entity
data class Subject(
    val name:String,
    val goalHours:Float,
    val colors:List<Int>,
    @PrimaryKey(autoGenerate = true)
    val subjectId:Int?=null,
){
    companion object{
        // list of all 5 gradients user can choose in between
        val subjectCardColors = listOf(gradient1, gradient2, gradient3, gradient4, gradient5)
    }
}