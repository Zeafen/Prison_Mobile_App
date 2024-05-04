package com.example.prison_app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.prison_app.data.AccountRole
import com.example.prison_app.data.database.InquisitorEvent
import com.example.prison_app.data.database.LoginEvent
import com.example.prison_app.data.database.PrisonerEvent
import com.example.prison_app.ui.inquisitor.InquisitorScreenViewModel
import com.example.prison_app.ui.login.LoginScreen
import com.example.prison_app.ui.login.LoginViewModel
import com.example.prison_app.ui.prisoner.PrisonerScreenViewModel
import com.example.prison_app.ui.prison.PrisonsScreenViewModel
import com.example.prison_app.ui.inquisitor.InquisitorsScreen
import com.example.prison_app.ui.inquisitor.InquisitorMainScreen
import com.example.prison_app.ui.prison.CaptainPrisonsScreen
import com.example.prison_app.ui.prison.PrisonsScreen
import com.example.prison_app.ui.prisoner.AuthorisedPrisonerMainScreen
import com.example.prison_app.ui.prisoner.InquisitorPrisonersScreen
import com.example.prison_app.ui.prisoner.PrisonersScreen
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            val navController = rememberNavController()
            val prisonersVM : PrisonerScreenViewModel = hiltViewModel()
            val inquisitorsVM : InquisitorScreenViewModel = hiltViewModel()

            NavHost(navController = navController, startDestination = "auth"){
                navigation(startDestination = "login_prisons", route = "auth"){
                    composable("login_prisons") {
                        val loginVm = it.getParentViewModel<LoginViewModel>(navController = navController)

                        val prisonsVm =
                            it.getParentViewModel<PrisonsScreenViewModel>(navController = navController)
                        val state by prisonsVm.state.collectAsState()
                        Scaffold(
                            topBar = {
                                Login_PrisonsTopAppBar {
                                    navController.navigate("login")
                                }
                            }
                        ) { innerPadding ->
                            PrisonsScreen(modifier = Modifier
                                .padding(innerPadding),
                                state = state,
                                onPrisonSelected = {
                                    loginVm.onEvent(LoginEvent.SetPrisonID(it))
                                    navController.navigate("login")
                                },
                                onEvent = { prisonsVm.onEvent(it) })
                        }
                    }

                    composable(route = "login"){
                        val context = LocalContext.current
                        val loginVm = it.getParentViewModel<LoginViewModel>(navController = navController)

                        val state by loginVm.state.collectAsState()
                        LoginScreen(state = state,
                            onGoToPrisonsSelecting = {navController.navigateUp()},
                            onEvent = loginVm::onEvent,
                            onLoginCompleted = {
                                    when(state.currentRole){
                                        is AccountRole.Prisoner -> {
                                            navController.popBackStack()
                                            prisonersVM.onEvent(PrisonerEvent.SelectedPrisonChanged(state.prisonID))
                                            prisonersVM.onEvent(PrisonerEvent.AuthorisedPrisonerChanged(
                                                (state.currentRole as AccountRole.Prisoner).prisoner))
                                            navController.navigate("prisoner")
                                        }
                                        is AccountRole.Inquisitor -> {
                                            navController.popBackStack()
                                            prisonersVM.onEvent(PrisonerEvent.SelectedPrisonChanged(state.prisonID))
                                            inquisitorsVM.onEvent(InquisitorEvent.AuthorisedInquisitorChanged(
                                                (state.currentRole as AccountRole.Inquisitor).inquisitor))
                                            navController.navigate("inquisitor")
                                        }
                                        is AccountRole.Captain -> {
                                            navController.popBackStack()
                                            navController.navigate("captain")
                                        }
                                        null -> {
                                            Toast.makeText(context, "Account role haven`t been initialized", Toast.LENGTH_LONG).show()
                                        }
                                    }
                            })
                    }
                }

                navigation(startDestination = "prisoner_original", route = "prisoner"){
                    composable(route = "prisoner_original") {
                        val state by prisonersVM.state.collectAsState()
                        AuthorisedPrisonerMainScreen(
                            state = state,
                            onEvent = prisonersVM::onEvent,
                            onGoToPrisoners = {navController.navigate("prisoners")})
                    }

                    composable(route = "prisoners"){
                        val state by prisonersVM.state.collectAsState()
                        PrisonersScreen(
                            state = state,
                            onEvent = prisonersVM::onEvent)
                    }
                }

                navigation(startDestination = "inquisitor_prisoners", route = "inquisitor"){
                    composable(route = "inquisitor_original") {
                        val state by inquisitorsVM.state.collectAsState()

                        InquisitorMainScreen(
                            inquisitor = state.authorisedInquisitor,
                            onGoToPrisoners = { navController.navigate("inquisitor_prisoners") }
                        )
                    }

                    composable(route = "inquisitor_prisoners") {
                        val state by prisonersVM.state.collectAsState()

                        InquisitorPrisonersScreen(
                            state = state,
                            onGoToOriginalScreen = { navController.navigate("inquisitor_original") },
                            onEvent = prisonersVM::onEvent
                        )

                    }
                }

                navigation(startDestination = "captain_prisons", route = "captain"){
                    composable(route = "captain_prisons"){
                        val prisonsVM = it.getParentViewModel<PrisonsScreenViewModel>(navController = navController)
                        val state by prisonsVM.state.collectAsState()
                        CaptainPrisonsScreen(
                            state = state,
                            onEvent = { prisonsVM.onEvent(it) },
                            onGoToPrisonInquisitors = {
                                navController.navigate(
                                    "captain_inquisitors/{prisonID}"
                                        .replace(oldValue = "{prisonID}", newValue = it.toString())
                                )
                            })
                    }

                    composable(route = "captain_inquisitors/{prisonID}"){
                        val prisonID = it.arguments?.getString("prisonID")
                        inquisitorsVM.onEvent(InquisitorEvent.SelectedPrisonChanged(UUID.fromString(prisonID)))

                        val state by inquisitorsVM.state.collectAsState()
                        InquisitorsScreen(
                            state = state,
                            onEvent = inquisitorsVM::onEvent,
                            onGoToPrisonsScreen = { navController.navigate("captain_prisons") })
                    }
                }
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.getParentViewModel(navController: NavController) : T{
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()

    val parentEntry = remember(this){
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login_PrisonsTopAppBar(
    modifier : Modifier = Modifier,
    onGoToLogin : () -> Unit
) {
    TopAppBar(modifier = modifier,
        title = {
            Text(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 4.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                text = stringResource(R.string.prisons_screen_header),
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Start
            )
        },
        navigationIcon = {
            IconButton(modifier = Modifier
                .padding(12.dp)
                .clip(CircleShape),
                onClick = onGoToLogin ) {
                Icon(
                    painter = painterResource(id = R.drawable.login_ic),
                    contentDescription = stringResource(id = R.string.home_screen_desc)
                )
            }
        })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InquisitorTopAppBar(
    modifier : Modifier = Modifier,
    onGoToAddPrisoner : () -> Unit,
    onGoToOriginalScreen : () -> Unit
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
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .padding(12.dp)
                    .clip(CircleShape),
                onClick = onGoToOriginalScreen
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.home_screen_ic),
                    contentDescription = stringResource(id = R.string.home_screen_desc)
                )
            }
        },
        actions = {
            IconButton(
                modifier = Modifier
                    .padding(12.dp)
                    .clip(CircleShape),
                onClick = onGoToAddPrisoner
            ) {
                Image(
                    painter = painterResource(id = R.drawable.add_button_ic),
                    contentDescription = stringResource(id = R.string.home_screen_desc)
                )
            }
        })}
