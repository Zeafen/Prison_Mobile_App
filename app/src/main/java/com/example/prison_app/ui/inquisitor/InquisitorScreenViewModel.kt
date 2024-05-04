package com.example.prison_app.ui.inquisitor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prison_app.data.database.Inquisitor
import com.example.prison_app.data.database.InquisitorEvent
import com.example.prison_app.data.database.PrisonDAO
import dagger.hilt.android.lifecycle.HiltViewModel
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

@HiltViewModel
class InquisitorScreenViewModel @Inject constructor(
    val prisonDAO : PrisonDAO
) : ViewModel() {


    private val _prisonID = MutableStateFlow(UUID.randomUUID())
    private val _inquisitors : StateFlow<List<Inquisitor>> = _prisonID.flatMapLatest {prisonID ->
        prisonDAO.getInquisitorsAtPrison(prisonID)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(InquisitorState())

    val state = combine(_state, _inquisitors, _prisonID){ inquisitorState, inquisitors, prisonID ->
        inquisitorState.copy(
            inquisitors = inquisitors.filter { it.id != inquisitorState.authorisedInquisitor.id },
            prisonID = prisonID
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InquisitorState())

    fun onEvent(event : InquisitorEvent){
        when(event){
            is InquisitorEvent.AuthorisedInquisitorChanged -> {
                _state.update {
                    it.copy(authorisedInquisitor = event.inquisitor)
                }
                _prisonID.value = event.inquisitor.prisonID
            }
            is InquisitorEvent.DeleteInquisitor -> viewModelScope.launch {
                prisonDAO.removeInquisitor(event.inquisitorID)
            }
            InquisitorEvent.HideDialog -> _state.update {
                it.copy(isEditingInquisitor = false,
                    selectedInquisitor = null)
            }
            InquisitorEvent.OpenDialog -> _state.update {
                it.copy(isEditingInquisitor = true)
            }
            InquisitorEvent.SaveInquisitor -> {
                val name = state.value.inquisitorName
                val surname = state.value.inquisitorSurname
                val patronymic = state.value.inquisitorPatronymic
                val age = state.value.inquisitorAge
                val login = state.value.inquisitorLogin
                val password = state.value.inquisitorPassword
                val prisonID = state.value.prisonID

                val inquisitor = if (state.value.selectedInquisitor == null)
                    Inquisitor(
                        firstName = name,
                        surname = surname,
                        patronymic = patronymic,
                        age = age,
                        login = login,
                        password = password,
                        prisonID = prisonID
                    )
                else state.value.selectedInquisitor!!.copy(
                    firstName = name,
                    surname = surname,
                    patronymic = patronymic,
                    age = age,
                    login = login,
                    password = password,
                    prisonID = prisonID
                )

                viewModelScope.launch{
                    prisonDAO.upsertInquisitor(inquisitor)
                }
                _state.update {
                    it.copy(isEditingInquisitor = false)
                }
                _state.update {
                    it.copy(inquisitorName = "",inquisitorSurname = "",
                        inquisitorPatronymic = "",inquisitorAge = 0,
                        inquisitorLogin = "",inquisitorPassword =  "",
                    )
                }
            }
            is InquisitorEvent.SelectedInquisitorChanged -> _state.update {
                it.copy(
                    selectedInquisitor = event.inquisitor,
                    inquisitorName = event.inquisitor.firstName,
                    inquisitorSurname = event.inquisitor.surname,
                    inquisitorPatronymic = event.inquisitor.patronymic,
                    inquisitorAge = event.inquisitor.age,
                    inquisitorLogin = event.inquisitor.login,
                    inquisitorPassword = event.inquisitor.password
                )
            }
            is InquisitorEvent.SelectedPrisonChanged -> {
                _prisonID.value = event.prisonID
            }
            is InquisitorEvent.SetInquisitorAge -> _state.update {
                it.copy(inquisitorAge = event.inquisitorAge)
            }
            is InquisitorEvent.SetInquisitorLogin -> _state.update {
                it.copy(inquisitorLogin = event.inquisitorLogin)
            }
            is InquisitorEvent.SetInquisitorName -> _state.update {
                it.copy(inquisitorName = event.inquisitorName)
            }
            is InquisitorEvent.SetInquisitorPassword -> _state.update {
                it.copy(inquisitorPassword = event.inquisitorPassword)
            }
            is InquisitorEvent.SetInquisitorPatronymic -> _state.update {
                it.copy(inquisitorPatronymic = event.inquisitorPatronymic)
            }
            is InquisitorEvent.SetInquisitorSurname -> _state.update {
                it.copy(inquisitorSurname = event.inquisitorSurname)
            }
        }
    }
}