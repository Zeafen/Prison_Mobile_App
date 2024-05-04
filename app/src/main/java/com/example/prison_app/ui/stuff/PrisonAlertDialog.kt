package com.example.prison_app.ui.stuff

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.prison_app.R


@Composable
fun PrisonAlertDialog(
    dialogText : String,
    dialogTitle : String,
    dialogIcon : ImageVector,
    onDismissRequest : () -> Unit,
    onConfirmAction : () -> Unit,

    ) {
    AlertDialog(onDismissRequest = onDismissRequest,
        text = {
            Text(text = dialogText)
        },
        title = {
            Text(text = dialogTitle)
        },
        icon = { Icon(imageVector = dialogIcon, contentDescription = stringResource(id = R.string.alert_dialog_desc))},
        confirmButton = {
            TextButton(onClick = onConfirmAction.also { onDismissRequest() }) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Dismiss")
            }
        })
}

@Composable
fun PrisonAlertDialog(
    dialogText : String,
    dialogTitle : String,
    painter : Painter,
    onDismissRequest : () -> Unit,
    onConfirmAction : () -> Unit,

    ) {
    AlertDialog(onDismissRequest = onDismissRequest,
        text = {
            Text(text = dialogText)
        },
        title = {
            Text(text = dialogTitle)
        },
        icon = {
            Icon(
                painter = painter,
                contentDescription = stringResource(id = R.string.alert_dialog_desc)
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmAction) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Dismiss")
            }
        })
}