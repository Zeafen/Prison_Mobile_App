package com.example.prison_app.ui.inquisitor

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.prison_app.R
import com.example.prison_app.data.database.Inquisitor
import com.example.prison_app.data.database.InquisitorEvent
import com.example.prison_app.ui.stuff.PrisonAlertDialog
import java.util.UUID

@Composable
fun InquisitorsScreen(
    modifier : Modifier = Modifier,
    state : InquisitorState,
    onEvent : (InquisitorEvent) -> Unit,
    onGoToPrisonsScreen: () -> Unit
) {

    var openAlert by rememberSaveable {
        mutableStateOf(false)
    }
    Scaffold(modifier = modifier,
        topBar = {
            CaptainTopAppBar(
                onGoToAddPrison = { onEvent(InquisitorEvent.OpenDialog) },
                onGoToPrisonsScreen = onGoToPrisonsScreen
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
        ) {

            if (openAlert)
                item {
                    PrisonAlertDialog(
                        dialogText = stringResource(id = R.string.inquisitor_deleting_text),
                        dialogTitle = stringResource(id = R.string.inquisitor_deleting_title),
                        dialogIcon = Icons.Default.Warning,
                        onDismissRequest = { openAlert = false },
                        onConfirmAction = { onEvent(InquisitorEvent.DeleteInquisitor(state.selectedInquisitor!!.id)) })
                }

            if (state.isEditingInquisitor)
                item {
                    InquisitorConfiguringDialog(
                        state = state,
                        onDismissDialog = { onEvent(InquisitorEvent.SaveInquisitor) },
                        onEvent = onEvent
                    )
                }

            items(state.inquisitors) { inquisitor ->
                InquisitorCell(modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 4.dp)
                    .clickable {
                        onEvent(InquisitorEvent.SelectedInquisitorChanged(inquisitor))
                        onEvent(InquisitorEvent.OpenDialog)
                    },
                    inquisitor = inquisitor,
                    onFireEmployee = {
                        openAlert = true
                        onEvent(InquisitorEvent.SelectedInquisitorChanged(inquisitor))
                    })
            }
        }
    }
}

@Composable
fun InquisitorCell(
    modifier : Modifier = Modifier,
    inquisitor : Inquisitor,
    onFireEmployee : (employeeID : UUID) -> Unit
) {
    Row(modifier = modifier) {
        if (inquisitor.photoFileName.isNullOrEmpty())
            Image(
                modifier = Modifier
                    .padding(start = 4.dp, end = 8.dp)
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                    .clickable {
                        //change Image
                    },
                painter = painterResource(id = R.drawable.image_not_supported),
                contentDescription = stringResource(R.string.prison_image_desc)
            )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .padding(bottom = 8.dp),
            text = stringResource(
                R.string.prisoner_initials_text,
                inquisitor.surname,
                inquisitor.firstName.substring(0, 1),
                inquisitor.patronymic.substring(0, 1)
            ),
            style = MaterialTheme.typography.titleLarge
        )
        IconButton(modifier = Modifier
            .padding(8.dp)
            .align(Alignment.CenterVertically)
            .clip(CircleShape),
            onClick = { onFireEmployee(inquisitor.id) }) {
            Icon(
                painter = painterResource(R.drawable.fire_employee_ic),
                contentDescription = stringResource(
                    R.string.options_menu_ic
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaptainTopAppBar(
    modifier : Modifier = Modifier,
    onGoToAddPrison : () -> Unit,
    onGoToPrisonsScreen : () -> Unit
) {
    TopAppBar(modifier = modifier,
        title = {
            Text(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 4.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                text = stringResource(R.string.inquisitors_screen_header),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .padding(12.dp)
                    .clip(CircleShape),
                onClick = onGoToPrisonsScreen
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.home_screen_ic),
                    contentDescription = stringResource(id = R.string.prisons_screen_header)
                )
            }
        },
        actions = {
            IconButton(
                modifier = Modifier
                    .padding(12.dp)
                    .clip(CircleShape),
                onClick = onGoToAddPrison
            ) {
                Image(
                    painter = painterResource(id = R.drawable.add_button_ic),
                    contentDescription = stringResource(id = R.string.add_prison_desc)
                )
            }
        })
}