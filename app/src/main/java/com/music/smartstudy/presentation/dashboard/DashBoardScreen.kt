package com.music.smartstudy.presentation.dashboard

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.music.smartstudy.R
import com.music.smartstudy.domain.model.Subject
import com.music.smartstudy.presentation.components.CountCard
import com.music.smartstudy.presentation.components.SubjectCard


@Composable
fun DashBoardScreen() {

    val subjecListt = listOf(
        Subject("English", 10f, Subject.subjectCardColors[0]),
        Subject("Math", 10f, Subject.subjectCardColors[1]),
        Subject("Physics", 10f, Subject.subjectCardColors[2]),
        Subject("Chemistry", 10f, Subject.subjectCardColors[4]),
        Subject("Biology", 10f, Subject.subjectCardColors[3]),
    )
    Scaffold(
        topBar = { DashboardTopAppBar() },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                CountCardSection(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    subjectCount = 5,
                    subjectHours = "10",
                    goalHours = "10"
                )
            }
            item {
                SubjectCardsSection(
                    modifier = Modifier.fillMaxWidth(),
                    subjectList = subjecListt
                )
            }
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DashboardTopAppBar() {
    CenterAlignedTopAppBar(title = {
        Text(
            text = "Study Smart", style = MaterialTheme.typography.headlineMedium
        )
    })
}

@Composable
 private fun CountCardSection(
    modifier: Modifier,
    subjectCount: Int,
    subjectHours: String,
    goalHours: String
) {
    Row(modifier = modifier) {
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Subject Count",
            count = "$subjectCount"
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Subject Hours",
            count = subjectHours
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Goal Study Hours",
            count = goalHours
        )
    }
}


@Composable
private fun SubjectCardsSection(
    modifier: Modifier,
    subjectList:List<Subject>,
    emptyListText:String = "You don't have any subjects.\n Click the + button to add new subject."
){
    Column(
        modifier = modifier
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
             Text(
                 text = "SUBJECTS",
                 style = MaterialTheme.typography.bodySmall,
                 modifier = Modifier.padding(12.dp)
             )
             
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Subject"
                )
            }
            
        }
        if(subjectList.isEmpty()){
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(R.drawable.img_books) ,
                contentDescription = emptyListText
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = emptyListText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 12.dp,end=12.dp)
        ){
            items(subjectList){ subject ->
                    SubjectCard(
                        subjectName = subject.name,
                        gradientColors =subject.colors,
                        onClick = {}
                    )
            }
        }
    }
}