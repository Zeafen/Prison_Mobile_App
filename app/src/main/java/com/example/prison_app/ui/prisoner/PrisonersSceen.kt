package com.example.prison_app.ui.prisoner

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.prison_app.InquisitorTopAppBar
import com.example.prison_app.R
import com.example.prison_app.data.database.Prisoner
import com.example.prison_app.data.database.PrisonerEvent
import com.example.prison_app.ui.stuff.DialogWithGif
import com.example.prison_app.ui.stuff.PrisonAlertDialog

@Composable
fun InquisitorPrisonersScreen(
    modifier : Modifier = Modifier,
    state : PrisonerState,
    onGoToOriginalScreen : () -> Unit,
    onEvent : (PrisonerEvent) -> Unit
) {

    Scaffold(modifier = modifier,
        topBar = {
            InquisitorTopAppBar(
                onGoToAddPrisoner = { onEvent(PrisonerEvent.OpenEditDialog) },
                onGoToOriginalScreen = onGoToOriginalScreen
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            if(state.isEditingPrisoner){
                item { 
                    PrisonerConfiguringDialog(
                        state = state,
                        onDismissRequest = { onEvent(PrisonerEvent.HideDialog) },
                        onEvent = { onEvent(it) })
                }
            }

            items(state.prisoners) { prisoner ->
                PrisonerCell(modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 4.dp)
                    .clickable {
                        onEvent(PrisonerEvent.SelectedPrisonerChanged(prisoner))
                        onEvent(PrisonerEvent.OpenEditDialog)
                    },
                    prisoner = prisoner,
                    onDeletePrisoner = { onEvent(PrisonerEvent.DeletePrisoner(prisoner.id)) })
            }
        }

    }
}

@Composable
fun PrisonersScreen(
    modifier : Modifier = Modifier,
    state : PrisonerState,
    onEvent: (PrisonerEvent) -> Unit,
) {

    var showAlert by rememberSaveable {
        mutableStateOf(false)
    }

    LazyColumn(modifier = modifier) {

        if(showAlert && state.selectedPrisoner != null)
            item {
                PrisonAlertDialog(
                    dialogText = "Are you sure you want to fight this prisoner?",
                    dialogTitle = "! CAUTION !",
                    dialogIcon = Icons.Default.Warning,
                    onDismissRequest = { showAlert = false },
                    onConfirmAction = { onEvent(PrisonerEvent.FightPrisoner(state.selectedPrisoner)) })
            }

        if(state.selectedPrisoner != null && !showAlert) {
            item {
                PrisonerMainDialog(
                    state = state,
                    onDismissRequest = { onEvent(PrisonerEvent.HideDialog) })
            }
        }

        if(state.isFighting)
            item{
                DialogWithGif(drawableID = R.drawable.fencing) {
                    onEvent(PrisonerEvent.HideDialog)
                }
            }

        items(state.prisoners) { prisoner ->
            PrisonerCell(modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 4.dp)
                .clickable { onEvent(PrisonerEvent.SelectedPrisonerChanged(prisoner)) },
                prisoner = prisoner,
                onDeletePrisoner = {
                    onEvent(PrisonerEvent.SelectedPrisonerChanged(prisoner))
                    showAlert = true
                })
        }
    }

}


@Composable
fun PrisonerCell(
    modifier : Modifier = Modifier,
    prisoner : Prisoner,
    onDeletePrisoner : () -> Unit
) {
    Row(modifier = modifier) {
        if (prisoner.photoFileName.isNullOrEmpty())
            Image(
                modifier = Modifier
                    .padding(start = 4.dp, end = 8.dp)
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                painter = painterResource(id = R.drawable.image_not_supported),
                contentDescription = stringResource(R.string.prison_image_desc)
            )
        else
            Image(
                modifier = Modifier
                    .padding(start = 4.dp, end = 8.dp)
                    .align(Alignment.CenterVertically)
                    .weight(1f),
               bitmap = BitmapFactory.decodeFile(prisoner.photoFileName).asImageBitmap(),
                contentDescription = stringResource(R.string.prison_image_desc)
            )

        Column(
            modifier = Modifier
                .weight(5f)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp),
                text = stringResource(
                    R.string.prisoner_initials_text,
                    prisoner.surname,
                    prisoner.firstName.substring(0, 1),
                    prisoner.patronymic.substring(0, 1)
                ),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp),
                text = stringResource(
                    id = R.string.guilty_for_field,
                    if (prisoner.guiltyFor.length > 60) prisoner.guiltyFor.substring(0, 60) + "..."
                    else prisoner.guiltyFor
                ),
                style = MaterialTheme.typography.bodySmall
            )
        }
        IconButton(modifier = Modifier
            .padding(8.dp)
            .align(Alignment.CenterVertically)
            .clip(CircleShape),
            onClick = { onDeletePrisoner() }) {
            Image(
                painter = painterResource(R.drawable.burn_heretic_ic),
                contentDescription = stringResource(
                    R.string.options_menu_ic
                )
            )
        }
    }
}