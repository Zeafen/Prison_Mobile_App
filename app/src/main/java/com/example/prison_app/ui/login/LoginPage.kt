package com.example.prison_app.ui.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.prison_app.R
import com.example.prison_app.data.AccountRole
import com.example.prison_app.data.AccountType
import com.example.prison_app.data.database.LoginEvent
import kotlinx.coroutines.delay
import java.lang.IllegalArgumentException

@Composable
fun LoginScreen(
    modifier : Modifier = Modifier,
    state : LoginState,
    onEvent : (LoginEvent) -> Unit,
    onGoToPrisonsSelecting : () -> Unit,
    onLoginCompleted : (AccountRole) -> Unit
) {

    val context = LocalContext.current
    Scaffold(modifier = modifier,
        topBar = {
            LoginTopAppBar {
                onGoToPrisonsSelecting()
            }
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            OutlinedTextField(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 8.dp),
                label = {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 8.dp),
                        text = stringResource(id = R.string.login_input_header),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                value = state.login,
                isError = state.login.isEmpty(),
                onValueChange = { onEvent(LoginEvent.SetLogin(it)) })

            OutlinedTextField(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 8.dp)
                .align(Alignment.CenterHorizontally),
                label = {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 8.dp),
                        text = stringResource(id = R.string.password_input_header),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                isError = state.password.isEmpty(),
                value = state.password,
                onValueChange = { onEvent(LoginEvent.SetPassword(it)) })

            LazyRow(
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
            ) {
                items( AccountType.entries){accountType ->
                    AccountTypeCell(accountType = accountType,
                        isSelected = state.currentType == accountType,
                        onTypeSelected = { onEvent(LoginEvent.SetAccountType(it)) })
                }
            }

            Button(modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 24.dp),
                onClick = {
                    try {
                        onEvent(LoginEvent.ConfirmLogin(onLoginCompleted))
                    }
                    catch(ex : IllegalArgumentException){
                        Toast.makeText(context, "There s an error while logging in : ${ex.message}", Toast.LENGTH_LONG).show()
                        onGoToPrisonsSelecting()
                    }
                    catch(ex : Exception){
                        Toast.makeText(context, "There s an unexpected error while logging in : ${ex.message}", Toast.LENGTH_LONG).show()
                        onGoToPrisonsSelecting()
                    }
                }) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 16.dp, bottom = 12.dp),
                    text = stringResource(id = R.string.authorise_button_text),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginTopAppBar(
    modifier : Modifier = Modifier,
    onGoToPrisons : () -> Unit
) {
    TopAppBar(modifier = modifier,
        title = {
            Text(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 4.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                text = stringResource(R.string.login_screen_header),
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Start
            )
        },
        navigationIcon = {
            IconButton(modifier = Modifier
                .padding(12.dp)
                .clip(CircleShape),
                onClick = onGoToPrisons ) {
                Icon(
                    painter = painterResource(id = R.drawable.home_screen_ic),
                    contentDescription = stringResource(id = R.string.home_screen_desc)
                )
            }
        })
}


@Composable
fun AccountTypeCell(
    modifier : Modifier = Modifier,
    accountType: AccountType,
    isSelected : Boolean,
    onTypeSelected : (AccountType) -> Unit) {
    Row(modifier = modifier) {

        RadioButton(selected = isSelected, onClick = { onTypeSelected(accountType) })
        Text(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .align(Alignment.CenterVertically)
                .clickable {
                           onTypeSelected(accountType)
                },
            text = stringResource(id = accountType.text),
            textAlign = TextAlign.Left)
    }
}


@Preview
@Composable
private fun AccountTypeCellPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        repeat(10){
            AccountTypeCell(accountType = AccountType.Captain, isSelected = false, onTypeSelected = {})
        }
    }
}