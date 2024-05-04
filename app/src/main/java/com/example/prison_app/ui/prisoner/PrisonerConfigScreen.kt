package com.example.prison_app.ui.prisoner

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.prison_app.R
import com.example.prison_app.data.database.PrisonerEvent
import com.example.prison_app.ui.stuff.DoubleNumberPicker
import com.example.prison_app.ui.stuff.IntNumberPicker

@Composable
fun PrisonerConfiguringDialog(
    modifier : Modifier = Modifier,
    state : PrisonerState,
    onEvent : (PrisonerEvent) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        LazyColumn(modifier = modifier) {
            item {
                Row {
                    if (state.prisonerIsDangerous)
                        Image(
                            modifier = Modifier
                                .align(Alignment.CenterVertically),
                            painter = painterResource(id = R.drawable.prisoner_dangerous_ic),
                            contentDescription = stringResource(id = R.string.prisoner_dangerous_desc)
                        )

                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight()
                            .padding(vertical = 12.dp, horizontal = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                       onEvent(PrisonerEvent.SetPrisonerIsDangerous(!state.prisonerIsDangerous))
                            },
                        text = stringResource(R.string.personal_info_screen_header),
                        color = if (state.prisonerIsDangerous) Color.Red else Color.Black,
                        style = MaterialTheme.typography.displayMedium,
                        textAlign = TextAlign.Center
                    )

                    if (state.prisonerIsDangerous)
                        Image(
                            modifier = Modifier
                                .align(Alignment.CenterVertically),
                            painter = painterResource(id = R.drawable.prisoner_dangerous_ic),
                            contentDescription = stringResource(id = R.string.prisoner_dangerous_desc)
                        )
                }
            }
            item {


                if (state.prisonerPhotoFileName == null)
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(0.dp)
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
                        bitmap = BitmapFactory.decodeFile(state.prisonerPhotoFileName)
                            .asImageBitmap(),
                        contentDescription = stringResource(id = R.string.prisoner_image_desc)
                    )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterVertically),
                            color = Color.Red,
                            text = stringResource(R.string.prisoner_health_input),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Start
                        )
                        IntNumberPicker(
                            maxVal = 200,
                            minVal = 75,
                            defaultVal = state.prisonerHealth,
                            addingVal = 25,
                            reducingVal = 25,
                            onNumberChanged = { health ->
                                onEvent(PrisonerEvent.SetPrisonerHealth(health))
                            }
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterVertically),
                            color = Color.Blue,
                            text = stringResource(R.string.prisoner_power_input),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Start
                        )
                        IntNumberPicker(
                            maxVal = 5,
                            minVal = 1,
                            defaultVal = state.prisonerPower,
                            addingVal = 1,
                            reducingVal = 1,
                            onNumberChanged = { power ->
                                onEvent(PrisonerEvent.SetPrisonerPower(power))
                            }
                        )
                    }
                }
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
                                    text = stringResource(R.string.prisoner_name_field, ""),
                                    style = MaterialTheme.typography.headlineSmall,
                                    textAlign = TextAlign.Center
                                )
                            },
                            value = state.prisonerName,
                            textStyle = MaterialTheme.typography.headlineSmall,
                            onValueChange = { newName ->
                                onEvent(PrisonerEvent.SetPrisonerName(newName))
                            })

                        OutlinedTextField(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 12.dp, end = 12.dp),
                            label = {
                                Text(
                                    text = stringResource(R.string.prisoner_surname_field, ""),
                                    style = MaterialTheme.typography.headlineSmall,
                                    textAlign = TextAlign.Start
                                )
                            },
                            value = state.prisonerSurname,
                            textStyle = MaterialTheme.typography.headlineSmall,
                            onValueChange = { newSurname ->
                                onEvent(PrisonerEvent.SetPrisonerSurname(newSurname))
                            })

                        OutlinedTextField(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 12.dp, end = 12.dp),
                            label = {
                                Text(
                                    text = stringResource(R.string.prisoner_patronymic_field, ""),
                                    style = MaterialTheme.typography.headlineSmall,
                                    textAlign = TextAlign.Start
                                )
                            },
                            value = state.prisonerPatronymic,
                            textStyle = MaterialTheme.typography.headlineSmall,
                            onValueChange = { newPatronymic ->
                                onEvent(PrisonerEvent.SetPrisonerPatronymic(newPatronymic))
                            })

                        OutlinedTextField(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 12.dp, end = 12.dp),
                            label = {
                                Text(
                                    text = stringResource(R.string.login_input_header, ""),
                                    style = MaterialTheme.typography.headlineSmall,
                                    textAlign = TextAlign.Start
                                )
                            },
                            value = state.prisonerLogin,
                            textStyle = MaterialTheme.typography.headlineSmall,
                            onValueChange = { newLogin ->
                                onEvent(PrisonerEvent.SetPrisonerLogin(newLogin))
                            })


                        OutlinedTextField(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 12.dp, end = 12.dp),
                            label = {
                                Text(
                                    text = stringResource(R.string.password_input_header, ""),
                                    style = MaterialTheme.typography.headlineSmall,
                                    textAlign = TextAlign.Start
                                )
                            },
                            value = state.prisonerPassword,
                            textStyle = MaterialTheme.typography.headlineSmall,
                            visualTransformation = PasswordVisualTransformation(),
                            onValueChange = { newPassword ->
                                onEvent(PrisonerEvent.SetPrisonerPassword(newPassword))
                            })

                        Row(
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(vertical = 12.dp, horizontal = 24.dp),
                        ) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically),
                                text = stringResource(R.string.prisoner_age_input),
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = TextAlign.Center
                            )
                            IntNumberPicker(
                                maxVal = 100,
                                minVal = 18,
                                defaultVal = state.prisonerAge,
                                addingVal = 1,
                                reducingVal = 1,
                                onNumberChanged = { age ->
                                    onEvent(PrisonerEvent.SetPrisonerAge(age))
                                }
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 12.dp),
                        text = stringResource(
                            R.string.prisoner_imprisonment_period_input
                        ),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Start
                    )
                    DoubleNumberPicker(
                        maxVal = 100.0,
                        minVal = 0.0,
                        defaultVal = state.prisonerImprisonmentPeriod,
                        addingVal = 0.1,
                        extraAddingVal = 5.0,
                        reducingVal = 0.1,
                        extraReducingVal = 5.0,
                        onNumberChanged = { imprisonment ->
                            onEvent(PrisonerEvent.SetPrisonerImprisonmentPeriod(imprisonment))
                        }
                    )
                }
            }

            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 12.dp, end = 12.dp),
                    label = {
                        Text(
                            text = stringResource(R.string.guilty_for_field, ""),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Start
                        )
                    },
                    value = state.prisonerCrime,
                    textStyle = MaterialTheme.typography.titleLarge,
                    onValueChange = { newCrime ->
                        onEvent(PrisonerEvent.SetPrisonerCrime(newCrime))
                    },
                    maxLines = 3
                )
            }

            item {
                Button(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 12.dp),
                    onClick = {
                        onEvent(PrisonerEvent.SavePrisoner)
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

@Preview
@Composable
private fun PrisonerConfigPreview() {
    PrisonerConfiguringDialog(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        state = PrisonerState(),
        onEvent = {},
        onDismissRequest = {})
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateTopAppBar(
    modifier : Modifier = Modifier,
    onGoBack : () ->Unit,
    onSaveChanges : () -> Unit
) {
    TopAppBar(modifier = modifier,
        title = {
            Text(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 4.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                text = stringResource(R.string.personal_info_screen_header),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .padding(12.dp)
                    .clip(CircleShape),
                onClick = onGoBack
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.go_back_ic),
                    contentDescription = stringResource(id = R.string.home_screen_desc)
                )
            }
        },
        actions = {
            IconButton(
                modifier = Modifier
                    .padding(12.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterVertically),
                onClick = onSaveChanges
            ) {
                Image(
                    painter = painterResource(id = R.drawable.save_changes_ic),
                    contentDescription = stringResource(id = R.string.home_screen_desc)
                )
            }
        })
}