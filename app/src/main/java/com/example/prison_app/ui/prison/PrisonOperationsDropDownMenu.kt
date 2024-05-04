package com.example.prison_app.ui.prison

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.prison_app.R
import com.example.prison_app.data.database.Prison
import java.util.UUID

@Composable
fun PrisonOperationsDropDownMenu(
    modifier : Modifier = Modifier,
    expanded : Boolean,
    onDismissRequest : () -> Unit,
    onDeletePrison : () -> Unit,
    onUpdatePrison : () -> Unit,
    onGoToPrisonInquisitors : () -> Unit
) {
    DropdownMenu(modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) {
        DropdownMenuItem(
            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 8.dp),
            text = {
                Text(
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.delete_prison_button),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            onClick = onDeletePrison)
        DropdownMenuItem(
            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 8.dp),
            text = {
                Text(
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.update_prison_button),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            onClick = onUpdatePrison)
        DropdownMenuItem(
            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 8.dp),
            text = {
                Text(
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.observe_inquisitors_txt),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            onClick = onGoToPrisonInquisitors)
    }
}