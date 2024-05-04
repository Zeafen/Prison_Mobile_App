package com.example.prison_app.ui.inquisitor

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.prison_app.R
import com.example.prison_app.data.database.Inquisitor
import com.example.prison_app.ui.prisoner.MainTopAppBar
import java.util.UUID


@Composable
fun InquisitorMainScreen(
    modifier : Modifier = Modifier,
    inquisitor: Inquisitor,
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

            item {
                if (inquisitor.photoFileName == null)
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(240.dp)
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        alignment = Alignment.Center,
                        painter = painterResource(id = R.drawable.image_not_supported),
                        contentDescription = stringResource(id = R.string.inquisitor_account_type)
                    )
                else
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(240.dp)
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        alignment = Alignment.Center,
                        bitmap = BitmapFactory.decodeFile(inquisitor.photoFileName)
                            .asImageBitmap(),
                        contentDescription = stringResource(id = R.string.inquisitor_image_desc)
                    )

                Text(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    color = Color.Gray,
                    text = stringResource(R.string.prisoner_personal_info_field),
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center
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
                            text = stringResource(
                                R.string.prisoner_name_field,
                                inquisitor.firstName
                            ),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Start
                        )
                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(vertical = 16.dp, horizontal = 24.dp),
                            text = stringResource(
                                R.string.prisoner_surname_field,
                                inquisitor.surname
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
                                inquisitor.patronymic
                            ),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Start
                        )
                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(vertical = 8.dp, horizontal = 24.dp),
                            text = stringResource(R.string.prisoner_age_field, inquisitor.age),
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
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun InquisitorMainScreenPreview() {
    Column {
        InquisitorMainScreen(
            inquisitor = Inquisitor(
                firstName = "Initial name",
                surname = "Initial surname",
                patronymic = "Initial Patronymic",
                age = 21,
                login = "initial login",
                password = "123",
                prisonID = UUID.randomUUID()
            ),
            onGoToPrisoners = {}
        )
    }
}