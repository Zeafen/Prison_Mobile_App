package com.example.prison_app.ui.prison

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.prison_app.R
import com.example.prison_app.data.database.PrisonEvent
import com.example.prison_app.ui.prisoner.UpdateTopAppBar

@Composable
fun PrisonConfiguringDialog(
    modifier : Modifier = Modifier,
    state : PrisonState,
    onEvent : (PrisonEvent) -> Unit,
    onDismissRequest : () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Scaffold(modifier = modifier,
            topBar = {
                UpdateTopAppBar(onGoBack = {
                    onEvent(PrisonEvent.HideDialog)
                },
                    onSaveChanges = {
                        onEvent(PrisonEvent.SavePrison)
                    })
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                item {
                    Text(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 8.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        text = stringResource(R.string.prison_info_screen_header),
                        style = MaterialTheme.typography.displayMedium,
                        textAlign = TextAlign.Center
                    )
                }
                item {
                    if (state.prisonPhotoFileName == null)
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(240.dp)
                                .padding(vertical = 12.dp, horizontal = 8.dp)
                                .clickable {
                                    /*ToDo*/
                                },
                            alignment = Alignment.Center,
                            painter = painterResource(id = R.drawable.image_not_supported),
                            contentDescription = stringResource(id = R.string.prisoner_image_desc)
                        )
                    else
                        Image(
                            modifier = Modifier
                                .size(240.dp)
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            alignment = Alignment.Center,
                            bitmap = BitmapFactory.decodeFile(state.prisonPhotoFileName)
                                .asImageBitmap(),
                            contentDescription = stringResource(id = R.string.prisoner_image_desc)
                        )
                }

                item {
                    Text(
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        color = Color.Gray,
                        text = stringResource(R.string.prisoner_personal_info_field),
                        style = MaterialTheme.typography.displaySmall,
                        textAlign = TextAlign.Start
                    )

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .border(width = 4.dp, color = Color.Gray)
                            .wrapContentHeight()
                            .fillMaxWidth()
                    ) {
                        Column {
                            OutlinedTextField(modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, start = 12.dp, end = 12.dp),
                                label = {
                                    Text(
                                        text = "",
                                        style = MaterialTheme.typography.headlineMedium,
                                        textAlign = TextAlign.Center
                                    )
                                },
                                value = state.prisonName,
                                textStyle = MaterialTheme.typography.titleLarge,
                                onValueChange = { newName ->
                                    onEvent(PrisonEvent.SetPrisonName(newName))
                                })

                            OutlinedTextField(modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, start = 12.dp, end = 12.dp),
                                label = {
                                    Text(
                                        text = stringResource(
                                            R.string.prisoner_patronymic_field,
                                            ""
                                        ),
                                        style = MaterialTheme.typography.headlineMedium,
                                        textAlign = TextAlign.Start
                                    )
                                },
                                value = state.prisonAddress,
                                textStyle = MaterialTheme.typography.titleLarge,
                                onValueChange = { newAddress ->
                                    onEvent(PrisonEvent.SetPrisonAddress(newAddress))
                                })
                        }
                    }
                }
                item {
                    Button(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 12.dp),
                        onClick = {
                            onEvent(PrisonEvent.SavePrison)
                            onDismissRequest()
                        }) {
                        Text(
                            text = stringResource(R.string.save_changes_btn),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }
        }
    }
}