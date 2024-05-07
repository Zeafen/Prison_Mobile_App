package com.example.prison_app.ui.prisoner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prison_app.data.database.PrisonDAO
import com.example.prison_app.data.database.Prisoner
import com.example.prison_app.data.database.PrisonerEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class PrisonerScreenViewModel @Inject constructor (
    val prisonDAO : PrisonDAO
): ViewModel() {
    private val _authorizesPrisoner = MutableStateFlow<Prisoner?>(null)
    private val _selectedPrisoner = MutableStateFlow<Prisoner?>(null)
    private val _prisonID = MutableStateFlow(UUID.randomUUID())
    private var _prisoners: StateFlow<List<Prisoner>> = _prisonID.flatMapLatest {prisonID ->
        prisonDAO.getHereticsInPrison(prisonID)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(PrisonerState())
    val state = combine(_state, _prisoners, _prisonID){ state, prisoners, prisonID ->
        state.copy(
            prisonID = prisonID,
            prisoners = prisoners.filter { it.id != state.authorisedPrisoner.id }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PrisonerState())

    fun onEvent(event : PrisonerEvent){
        when(event){
            PrisonerEvent.HideDialog -> {
                _state.update{
                    it.copy(
                        isEditingPrisoner = false,
                        isHealing = false,
                        isPoweringUp = false,
                        selectedPrisoner = null
                    )
                }
            }
            PrisonerEvent.OpenEditDialog -> _state.update {
                it.copy(isEditingPrisoner = true)
            }
            PrisonerEvent.OpenHealDialog -> _state.update {
                it.copy(isEditingPrisoner = false,
                    isHealing = true,
                    isPoweringUp = false)
            }
            PrisonerEvent.OpenPowerUpDialog -> _state.update {
                it.copy(isEditingPrisoner = false,
                    isHealing = false,
                    isPoweringUp = true)
            }
            PrisonerEvent.GoToDiningRoom -> {
                _state.update {
                    it.copy(isEditingPrisoner = false,
                        isHealing = true,
                        isPoweringUp = false)
                }
                viewModelScope.launch {
                    val prisoner = state.value.authorisedPrisoner.copy(
                        health = if(state.value.authorisedPrisoner.health <= 200) state.value.authorisedPrisoner.health + 25
                        else 225
                    )
                    prisonDAO.upsertPrisoner(prisoner)
                    delay(10000)
                    _state.update {
                        it.copy(
                            isHealing = false,
                            authorisedPrisoner = prisoner)
                    }
                }
            }
            PrisonerEvent.GoToGym ->viewModelScope.launch {
                _state.update {
                    it.copy(isEditingPrisoner = false,
                        isHealing = false,
                        isPoweringUp = true)
                }
                val prisoner = state.value.authorisedPrisoner.copy(
                    power = if (state.value.authorisedPrisoner.power < 5) state.value.authorisedPrisoner.power + 1
                    else 5
                )
                prisonDAO.upsertPrisoner(prisoner)

                delay(10000)
                _state.update {
                    it.copy(
                        isPoweringUp = false,
                        authorisedPrisoner = prisoner)
                }
            }
            PrisonerEvent.SavePrisoner -> {
                val login = state.value.prisonerLogin
                val password = state.value.prisonerPassword
                val name = state.value.prisonerName
                val surname = state.value.prisonerSurname
                val patronymic = state.value.prisonerPatronymic
                val age = state.value.prisonerAge
                val crime = state.value.prisonerCrime
                val imprisonPeriod = state.value.prisonerImprisonmentPeriod
                val health = state.value.prisonerHealth
                val power = state.value.prisonerPower
                val isDangerous = state.value.prisonerIsDangerous

                if(login.isBlank() || password.isBlank() || name.isBlank() || surname.isBlank()
                    || patronymic.isBlank() || age !in 1..100 || crime.isBlank() || imprisonPeriod !in 0.0..100.0 ||
                    health !in 1..100 || power !in 1..5)
                    return
                else {
                    val prisoner = if (state.value.selectedPrisoner == null) Prisoner(
                        login = login,
                        password = password,
                        firstName = name,
                        surname = surname,
                        patronymic = patronymic,
                        age = age,
                        guiltyFor = crime,
                        imprisonmentPeriod = imprisonPeriod,
                        health = health,
                        power = power,
                        isDangerous = isDangerous,
                        prisonID = state.value.prisonID
                    )
                    else state.value.selectedPrisoner!!.copy(
                        login = login,
                        password = password,
                        firstName = name,
                        surname = surname,
                        patronymic = patronymic,
                        age = age,
                        guiltyFor = crime,
                        imprisonmentPeriod = imprisonPeriod,
                        health = health,
                        power = power,
                        isDangerous = isDangerous,
                        prisonID = state.value.prisonID
                    )
                    viewModelScope.launch {
                        prisonDAO.upsertPrisoner(prisoner)
                    }
                    _state.update{
                        it.copy(
                            isEditingPrisoner = false,
                            prisonerLogin = "",
                            prisonerPassword = "",
                            prisonerName = "",
                            prisonerSurname = "",
                            prisonerPatronymic = "",
                            prisonerAge = 21,
                            prisonerCrime = "",
                            prisonerImprisonmentPeriod = 1.0,
                            prisonerHealth = 100,
                            prisonerPower = 1,
                            prisonerIsDangerous = false,
                        )
                    }
                }
            }
            is PrisonerEvent.SelectedPrisonChanged -> _prisonID.value = event.prisonID
            is PrisonerEvent.DeletePrisoner -> viewModelScope.launch {
                prisonDAO.burnHeretic(event.prisonerID)
            }
            is PrisonerEvent.SelectedPrisonerChanged -> _state.update {
                it.copy(selectedPrisoner = event.prisoner,
                    prisonerLogin = event.prisoner.login,
                    prisonerPassword = event.prisoner.password,
                    prisonerName = event.prisoner.firstName,
                    prisonerSurname = event.prisoner.surname,
                    prisonerPatronymic = event.prisoner.patronymic,
                    prisonerAge = event.prisoner.age,
                    prisonerCrime = event.prisoner.guiltyFor,
                    prisonerImprisonmentPeriod = event.prisoner.imprisonmentPeriod,
                    prisonerHealth = event.prisoner.health,
                    prisonerPower = event.prisoner.power,
                    prisonerIsDangerous = event.prisoner.isDangerous,
                )
            }
            is PrisonerEvent.SetPrisonerAge -> _state.update {
                it.copy(prisonerAge = event.prisonerAge)
            }
            is PrisonerEvent.SetPrisonerCrime -> _state.update {
                it.copy(prisonerCrime = event.prisonerCrime)
            }
            is PrisonerEvent.SetPrisonerHealth -> _state.update {
                it.copy(prisonerHealth = event.prisonerHealth)
            }
            is PrisonerEvent.SetPrisonerImprisonmentPeriod -> _state.update {
                it.copy(prisonerImprisonmentPeriod = event.prisonerPeriod)
            }
            is PrisonerEvent.SetPrisonerIsDangerous -> _state.update {
                it.copy(prisonerIsDangerous = event.prisonerIsDangerous)
            }
            is PrisonerEvent.SetPrisonerName -> _state.update {
                it.copy(prisonerName = event.prisonerName)
            }
            is PrisonerEvent.SetPrisonerPatronymic -> _state.update {
                it.copy(prisonerPatronymic = event.prisonerPatronymic)
            }
            is PrisonerEvent.SetPrisonerPower -> _state.update {
                it.copy(prisonerPower = event.prisonerPower)
            }
            is PrisonerEvent.SetPrisonerSurname -> _state.update {
                it.copy(prisonerSurname = event.prisonerSurname)
            }
            is PrisonerEvent.FightPrisoner -> {
                _state.update {
                    it.copy(isFighting = true)
                }
                when (event.enemy.power - state.value.authorisedPrisoner.power) {
                    in 4 downTo 1 -> {
                        _state.update {
                            it.copy(
                                authorisedPrisoner = state.value.authorisedPrisoner.copy(
                                    health = state.value.authorisedPrisoner.health -
                                            (100 * Random.nextDouble(from = 0.0,until = 1.0)).toInt()
                                )
                            )
                        }
                        burnOnDead(state.value.authorisedPrisoner)
                    }
                    in -1 downTo -2 -> viewModelScope.launch {
                        prisonDAO.upsertPrisoner(
                            event.enemy.copy(
                                health = event.enemy.health -
                                        (25 * Random.nextDouble(from = 0.0,until = 1.0)).toInt()
                            )
                        )
                    }
                    in -3 downTo -4 -> viewModelScope.launch {
                        prisonDAO.upsertPrisoner(
                            event.enemy.copy(
                                health = event.enemy.health -
                                        (60 * Random.nextDouble(from = 0.0,until = 1.0)).toInt()
                            )
                        )
                    }
                }
                viewModelScope.launch {
                    delay(10000)
                    _state.update {
                        it.copy(isFighting = false)
                    }
                }
            }
            is PrisonerEvent.AuthorisedPrisonerChanged -> _state.update {
                it.copy(authorisedPrisoner = event.prisoner)
            }
            is PrisonerEvent.SetPrisonerLogin -> _state.update {
                it.copy(prisonerLogin = event.prisonerLogin)
            }
            is PrisonerEvent.SetPrisonerPassword -> _state.update {
                it.copy(prisonerPassword = event.prisonerPassword)
            }
        }
    }

    private fun burnOnDead(prisoner: Prisoner) = viewModelScope.launch {
        if(prisoner.health <= 0)
            prisonDAO.burnHeretic(prisoner.id)
        else
            prisonDAO.upsertPrisoner(prisoner)
    }
}