package com.music.smartstudy.presentation.Task

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.music.smartstudy.presentation.components.DeleteDialog
import com.music.smartstudy.presentation.components.SubjectListBottomSheet
import com.music.smartstudy.presentation.components.TaskCheckBox
import com.music.smartstudy.presentation.components.TaskDatePicker
import com.music.smartstudy.presentation.theme.Red
import com.music.smartstudy.subjecListt
import com.music.smartstudy.util.Priority
import com.music.smartstudy.util.changeMillisToDateString
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import java.time.Instant


data class TaskScreenNavArgs(
    val taskId: Int?,
    val subjectId: Int?
)

@Destination(navArgsDelegate = TaskScreenNavArgs::class)
@Composable
fun TaskScreenRoute(
    navigator: DestinationsNavigator
){
    TaskScreen(
        onBackButtonClick = { navigator.navigateUp() }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskScreen(
    onBackButtonClick: () -> Unit,
){

    var title by remember{ mutableStateOf("") }
    var description by remember{ mutableStateOf("") }

    var taskTitleError by rememberSaveable{
        mutableStateOf<String?>(null)
    }

    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDatePickerDialogOpen by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )

    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetOpen by remember { mutableStateOf(false) }

    taskTitleError = when{
        title.isBlank() -> "Please Enter Task Title."
        title.length < 4 -> "Task Title is too short."
        title.length >20 -> "Task Title is too long"
        else -> null
    }
    
    DeleteDialog(
        isOpen = isDeleteDialogOpen,
        title ="Delete Task?",
        bodyText ="Are you sure, you want to delete this task? "+
                "This action can not be undone",
        onDismissRequest = { isDeleteDialogOpen = false },
        onConfirmButtonClick = { isDeleteDialogOpen = false}
    )

    TaskDatePicker(
        state = datePickerState,
        isOpen = isDatePickerDialogOpen,
        onDismissButton = {
            isDatePickerDialogOpen  = false
        },
        onConfirmButtonClick = {
            isDatePickerDialogOpen = false
        }
    )

    SubjectListBottomSheet(
        sheetState = sheetState,
        isOpen = isBottomSheetOpen,
        subjects = subjecListt,
        onDismissRequest = {isBottomSheetOpen = false},
        onSubjectClicked = {
            scope.launch {
                        sheetState.hide()
            }.invokeOnCompletion {
                if(!sheetState.isVisible) isBottomSheetOpen = false
            }
        }
    )


     Scaffold(
         topBar = {
             TaskScreenTopBar(
                 isTaskExist = true,
                 isComplete =false ,
                 checkBoxBorderColor = Red ,
                 onBackButtonClick = onBackButtonClick,
                 onDeleteButtonClick = { isDeleteDialogOpen = true },
                 onCheckBoxClick = {}
             )
         }
     )
     {  paddingValues ->

         Column(
             modifier = Modifier
                 .verticalScroll(state = rememberScrollState())
                 .fillMaxWidth()
                 .padding(paddingValues)
                 .padding(horizontal = 12.dp)
         ) {
                OutlinedTextField(
                   modifier=Modifier.fillMaxWidth(),
                    value = title,
                    onValueChange = {title=it},
                    label = { Text(text = "Title")},
                    singleLine = true,
                    isError = taskTitleError !=null && title.isNotBlank(),
                    supportingText = { Text(text = taskTitleError.orEmpty())}
                )
                Spacer(modifier = Modifier.height(10.dp))

             OutlinedTextField(
                 modifier=Modifier.fillMaxWidth(),
                 value = description,
                 onValueChange = {description = it},
                 label = { Text(text = "Description")},
             )

             Spacer(modifier = Modifier.height(20.dp))

             Text(
                 text = "Due Date",
                 style = MaterialTheme.typography.bodySmall
             )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = datePickerState.selectedDateMillis.changeMillisToDateString(),
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = { isDatePickerDialogOpen = true}) {
                       Icon(
                           imageVector = Icons.Default.DateRange,
                           contentDescription ="Select Due Date"
                       )
                }
            }

             Spacer(modifier = Modifier.height(20.dp))

             Text(
                 text = "Priority",
                 style = MaterialTheme.typography.bodySmall
             )

             Spacer(modifier = Modifier.height(10.dp))

             Row(modifier = Modifier.fillMaxWidth()) {
                    Priority.entries.forEach{ priority ->

                        PriorityButton(
                            modifier = Modifier.weight(1f),
                            label = priority.title,
                            backgroundColor = priority.color,
                            borderColor = if (priority == Priority.MEDIUM) {
                                Color.White
                            } else Color.Transparent,
                            labelColor = if(priority == Priority.MEDIUM){
                                Color.White
                            }else Color.White.copy(alpha = 0.7f),
                            onClick = {}
                        )
                        
                    }
             }



             Spacer(modifier = Modifier.height(30.dp))

             Text(
                 text = "Related to subject",
                 style = MaterialTheme.typography.bodySmall
             )

             Row(
                 modifier = Modifier.fillMaxWidth(),
                 horizontalArrangement = Arrangement.SpaceBetween,
                 verticalAlignment = Alignment.CenterVertically
             ){
                 Text(
                     text = "English",
                     style = MaterialTheme.typography.bodyLarge
                 )
                 IconButton(onClick = { isBottomSheetOpen = true}) {
                     Icon(
                         imageVector = Icons.Default.ArrowDropDown,
                         contentDescription ="Select Subject"
                     )
                 }
             }
             
             Button(
                 enabled = taskTitleError==null,
                 onClick = { /*TODO*/ },
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(vertical = 12.dp)
             ) {
                    Text(text = "Save")
             }
         }

     }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskScreenTopBar(
    isTaskExist : Boolean,
    isComplete : Boolean,
    checkBoxBorderColor: Color,
    onBackButtonClick:() -> Unit,
    onDeleteButtonClick:() -> Unit,
    onCheckBoxClick: () -> Unit
    ) {
        TopAppBar(
            navigationIcon = {
                    IconButton(onClick = onBackButtonClick) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Navigation Back"
                            )
                    }
            },
            title = {
                Text(text = "Task", style = MaterialTheme.typography.headlineSmall)
            },
            actions = {
                if(isTaskExist){
                    TaskCheckBox(
                        isCompleted = isComplete,
                        borderColor = checkBoxBorderColor,
                        onCheckBoxClick= onCheckBoxClick
                    )
                    IconButton(onClick = onDeleteButtonClick) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Task"
                        )
                    }

                }
            }
        )
}


@Composable
private fun PriorityButton(
    modifier: Modifier = Modifier,
    label:String,
    backgroundColor:Color,
    borderColor:Color,
    labelColor:Color,
    onClick: () -> Unit
){
    Box (
         modifier = modifier
             .background(backgroundColor)
             .clickable { onClick() }
             .padding(5.dp)
             .border(1.dp, borderColor, RoundedCornerShape(5.dp))
             .padding(5.dp),
             contentAlignment = Alignment.Center
    ){
        Text(text = label, color = labelColor)
    }
}
