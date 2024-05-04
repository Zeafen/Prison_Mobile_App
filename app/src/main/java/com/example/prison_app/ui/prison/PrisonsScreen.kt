package com.example.prison_app.ui.prison

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.prison_app.R
import com.example.prison_app.data.database.Prison
import com.example.prison_app.data.database.PrisonEvent
import java.util.UUID

@Composable
fun PrisonsScreen(
    modifier : Modifier = Modifier,
    state : PrisonState,
    onEvent : (PrisonEvent) -> Unit,
    onPrisonSelected : (prisonID : UUID) -> Unit
) {

    LazyColumn(modifier = modifier) {

        if(state.isEditingPrison){
            item { 
                PrisonConfiguringDialog(
                    state = state,
                    onEvent = onEvent,
                    onDismissRequest = { onEvent(PrisonEvent.HideDialog) }
                )
            }
        }
        
        items(state.prisons) {
            PrisonCell(modifier = Modifier
                .clickable {
                    onEvent(PrisonEvent.SelectedPrisonChanged(it))
                    onPrisonSelected(it.id)
                },
                prison = it)
        }
    }
}


@Composable
fun CaptainPrisonsScreen(
    modifier : Modifier = Modifier,
    state : PrisonState,
    onEvent : (PrisonEvent) -> Unit,
    onGoToPrisonInquisitors : (prisonID : UUID) -> Unit
) {

    Scaffold(modifier = modifier,
        topBar = {
            CaptainTopAppBar(
                onGoToAddPrison = {onEvent(PrisonEvent.OpenDialog)},
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
        ) {

            if(state.isEditingPrison)
                item{
                    PrisonConfiguringDialog(
                        state = state,
                        onEvent = onEvent,
                        onDismissRequest = { onEvent(PrisonEvent.HideDialog) })
                }

            items(state.prisons) {
                PrisonCell(modifier = Modifier
                    .clickable {
                        onEvent(PrisonEvent.SelectedPrisonChanged(it))
                        onEvent(PrisonEvent.OpenDialog)
                    },
                    prison = it,
                    onDeletePrison = { onEvent(PrisonEvent.DeletePrison(it.id)) },
                    onUpdatePrison = {
                        onEvent(PrisonEvent.SelectedPrisonChanged(it))
                        onEvent(PrisonEvent.OpenDialog) },
                    onGoToPrisonInquisitors = onGoToPrisonInquisitors)
            }
        }
    }
}


@Composable
fun  PrisonCell(
    modifier : Modifier = Modifier,
    prison : Prison,
    onDeletePrison : (prison : Prison) -> Unit,
    onUpdatePrison : (prison : Prison) -> Unit,
    onGoToPrisonInquisitors : (prisonID : UUID) -> Unit
) {

    var dropExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    Row(modifier = modifier) {
        if(prison.photoFileName.isNullOrEmpty())
            Image(modifier = Modifier
                .padding(start = 4.dp, end = 8.dp)
                .align(Alignment.CenterVertically)
                .weight(1f)
                .clickable {
                    //change Image
                },
                painter = painterResource(id = R.drawable.image_not_supported),
                contentDescription = stringResource(R.string.prison_image_desc))
        Column(modifier = Modifier
            .fillMaxHeight()
            .weight(5f)) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp),
                text = prison.prisonName,
                style = MaterialTheme.typography.titleLarge)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp),
                text = prison.prisonAddress,
                style = MaterialTheme.typography.bodySmall)}
        Box(modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth()
            .padding(8.dp)
            .align(Alignment.CenterVertically)
            .weight(1f)
        ) {
            IconButton(modifier = Modifier
                .align(Alignment.Center)
                .matchParentSize()
                .clip(CircleShape),
                onClick = { dropExpanded = true }) {
                Icon(
                    painter = painterResource(R.drawable.option_menu_ic),
                    contentDescription = stringResource(
                        R.string.options_menu_ic))}
            PrisonOperationsDropDownMenu(
                expanded = dropExpanded,
                onDismissRequest = { dropExpanded = false },
                onUpdatePrison = { onUpdatePrison(prison) },
                onDeletePrison = { onDeletePrison(prison) },
                onGoToPrisonInquisitors = { onGoToPrisonInquisitors(prison.id) }
            )
        }
    }
}

@Composable
fun  PrisonCell(
    modifier : Modifier = Modifier,
    prison : Prison,
) {
    Row(modifier = modifier) {
        if (prison.photoFileName.isNullOrEmpty())
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
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(5f)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp),
                text = prison.prisonName,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp),
                text = prison.prisonAddress,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview
@Composable
private fun PrisonCellPreview() {
    Scaffold(
        topBar = {
            CaptainTopAppBar(
                onGoToAddPrison = {},
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            items(10) {
                PrisonCell(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                    prison = Prison(prisonName = "Initial name", prisonAddress = "Initial address"),
                    onUpdatePrison = {},
                    onDeletePrison = {},
                    onGoToPrisonInquisitors = {})
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaptainTopAppBar(
    modifier : Modifier = Modifier,
    onGoToAddPrison : () -> Unit,
) {
    TopAppBar(modifier = modifier,
        title = {
            Text(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 4.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                text = stringResource(R.string.prisons_screen_header),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start
            )
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