package com.example.prison_app.ui.prisoner

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.prison_app.R
import com.example.prison_app.data.database.PrisonerEvent
import com.example.prison_app.ui.stuff.DialogWithGif
import kotlin.random.Random

@Composable
fun PrisonerMainDialog(
    modifier : Modifier = Modifier,
    state: PrisonerState,
    onDismissRequest : () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        LazyColumn(
            modifier = modifier
        ) {
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
                            .padding(vertical = 12.dp),
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .align(Alignment.CenterVertically),
                        color = Color.Red,
                        text = stringResource(R.string.prisoner_health_field, state.prisonerHealth),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        modifier = Modifier
                            .padding(end = 12.dp),
                        color = Color.Blue,
                        text = stringResource(R.string.prisoner_power_field, state.prisonerPower),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Start
                    )
                }

                if (state.prisonerPhotoFileName == null)
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(240.dp)
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        alignment = Alignment.Center,
                        painter = painterResource(id = R.drawable.image_not_supported),
                        contentDescription = stringResource(id = R.string.prisoner_image_desc)
                    )
                else
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(240.dp)
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        alignment = Alignment.Center,
                        bitmap = BitmapFactory.decodeFile(state.prisonerPhotoFileName)
                            .asImageBitmap(),
                        contentDescription = stringResource(id = R.string.prisoner_image_desc)
                    )

                Text(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(horizontal = 24.dp),
                    color = Color.Gray,
                    text = stringResource(R.string.prisoner_personal_info_field),
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Start
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .border(width = 4.dp, color = Color.Gray)
                        .wrapContentHeight()
                        .fillMaxWidth()
                ) {
                    Column {
                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(vertical = 8.dp, horizontal = 24.dp),
                            text = stringResource(R.string.prisoner_name_field, state.prisonerName),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Start
                        )
                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(vertical = 16.dp, horizontal = 24.dp),
                            text = stringResource(
                                R.string.prisoner_surname_field,
                                state.prisonerSurname
                            ),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Start
                        )
                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(vertical = 16.dp, horizontal = 24.dp),
                            text = stringResource(
                                R.string.prisoner_patronymic_field,
                                state.prisonerPatronymic
                            ),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Start
                        )
                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(vertical = 8.dp, horizontal = 24.dp),
                            text = stringResource(R.string.prisoner_age_field, state.prisonerAge),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Start
                        )
                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(vertical = 8.dp, horizontal = 24.dp),
                            text = stringResource(R.string.not_married),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Start
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(vertical = 8.dp, horizontal = 24.dp),
                            text = stringResource(
                                R.string.prisoner_imprisonment_period_field,
                                state.prisonerImprisonmentPeriod.toInt()
                            ),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Start
                        )
                    }
                }
                Text(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(vertical = 8.dp, horizontal = 24.dp),
                    text = stringResource(R.string.prisoner_crime_field, state.prisonerCrime),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}


@Composable
fun AuthorisedPrisonerMainScreen(
    modifier : Modifier = Modifier,
    state: PrisonerState,
    onEvent : (PrisonerEvent) -> Unit,
    onGoToPrisoners : () -> Unit
) {
    Scaffold(modifier = modifier,
        topBar = {
            MainTopAppBar(onGoToPrisoners = onGoToPrisoners)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding),
        ) {
            if(state.isHealing)
                item{
                    DialogWithGif(drawableID = R.drawable.eating) {
                    }
                }
            if(state.isPoweringUp)
                item{
                    DialogWithGif(drawableID = if (Random.nextInt(from = 0, until = 1) == 1) R.drawable.light_weight
                    else R.drawable.weight_lifting) {
                    }
                }
            item {
                Row {
                    if (state.authorisedPrisoner.isDangerous)
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
                            .padding(vertical = 12.dp),
                        text = stringResource(R.string.personal_info_screen_header),
                        color = if (state.authorisedPrisoner.isDangerous) Color.Red else Color.Black,
                        style = MaterialTheme.typography.displayMedium,
                        textAlign = TextAlign.Center
                    )
                    if (state.authorisedPrisoner.isDangerous)
                        Image(
                            modifier = Modifier
                                .align(Alignment.CenterVertically),
                            painter = painterResource(id = R.drawable.prisoner_dangerous_ic),
                            contentDescription = stringResource(id = R.string.prisoner_dangerous_desc)
                        )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {

                        OutlinedButton(
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 4.dp)
                                .align(Alignment.Start),
                            onClick = { onEvent(PrisonerEvent.GoToDiningRoom) }
                        ) {
                            Text(
                                color = Color.DarkGray,
                                text = stringResource(R.string.prisoner_restore_health_field),
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Start
                            )
                        }
                        Text(
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(horizontal = 12.dp),
                            color = Color.Red,
                            text = stringResource(R.string.prisoner_health_field, state.authorisedPrisoner.health),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Start
                        )
                    }

                    Column {
                        OutlinedButton(
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            onClick = { onEvent(PrisonerEvent.GoToGym) }
                        ) {
                            Text(
                                color = Color.DarkGray,
                                text = stringResource(R.string.prisoner_power_up_filed),
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Start
                            )
                        }
                        Text(
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(horizontal = 12.dp),
                            color = Color.Blue,
                            text = stringResource(R.string.prisoner_power_field, state.authorisedPrisoner.power),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }

            item {
                if (state.authorisedPrisoner.photoFileName == null)
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(240.dp)
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        alignment = Alignment.Center,
                        painter = painterResource(id = R.drawable.image_not_supported),
                        contentDescription = stringResource(id = R.string.prisoner_image_desc)
                    )
                else
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(240.dp)
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        alignment = Alignment.Center,
                        bitmap = BitmapFactory.decodeFile(state.authorisedPrisoner.photoFileName)
                            .asImageBitmap(),
                        contentDescription = stringResource(id = R.string.prisoner_image_desc)
                    )

                Text(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(horizontal = 24.dp),
                    color = Color.Gray,
                    text = stringResource(R.string.prisoner_personal_info_field),
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Start
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .border(width = 4.dp, color = Color.Gray)
                        .wrapContentHeight()
                        .fillMaxWidth()
                ) {
                    Column {
                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(vertical = 8.dp, horizontal = 24.dp),
                            text = stringResource(R.string.prisoner_name_field, state.authorisedPrisoner.firstName),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Start
                        )
                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(vertical = 16.dp, horizontal = 24.dp),
                            text = stringResource(
                                R.string.prisoner_surname_field,
                                state.authorisedPrisoner.firstName
                            ),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Start
                        )
                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(vertical = 16.dp, horizontal = 24.dp),
                            text = stringResource(
                                R.string.prisoner_patronymic_field,
                                state.authorisedPrisoner.firstName
                            ),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Start
                        )
                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(vertical = 8.dp, horizontal = 24.dp),
                            text = stringResource(R.string.prisoner_age_field, state.authorisedPrisoner.age),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Start
                        )
                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(vertical = 8.dp, horizontal = 24.dp),
                            text = stringResource(R.string.not_married),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Start
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(vertical = 8.dp, horizontal = 24.dp),
                            text = stringResource(
                                R.string.prisoner_imprisonment_period_field,
                                state.authorisedPrisoner.imprisonmentPeriod.toInt()
                            ),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Start
                        )
                    }
                }
                Text(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(vertical = 8.dp, horizontal = 24.dp),
                    text = stringResource(R.string.prisoner_crime_field, state.authorisedPrisoner.guiltyFor),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Preview
@Composable
private fun PrisonerPreview() {
    AuthorisedPrisonerMainScreen(
        modifier = Modifier.background(Color.White),
        state = PrisonerState(),
        onEvent = {},
        onGoToPrisoners = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    modifier : Modifier = Modifier,
    onGoToPrisoners : () -> Unit
) {
    TopAppBar(modifier = modifier,
        title = {
            Text(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 4.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                text = stringResource(R.string.home_screen_desc),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start
            )
        },
        actions = {
            IconButton(modifier = Modifier
                .padding(12.dp)
                .clip(CircleShape),
                onClick = onGoToPrisoners ) {
                Icon(
                    painter = painterResource(id = R.drawable.prisoners_screen_ic),
                    contentDescription = stringResource(id = R.string.home_screen_desc)
                )
            }
        })
}