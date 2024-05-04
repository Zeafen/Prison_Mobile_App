package com.example.prison_app.ui.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prison_app.data.AccountRole
import com.example.prison_app.data.AccountType
import com.example.prison_app.data.database.LoginEvent
import com.example.prison_app.data.database.PrisonDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val prisonDAO : PrisonDAO
) : ViewModel() {

    private val _currentPrisonID = MutableStateFlow(UUID.randomUUID())
    private val _accountType = MutableStateFlow(AccountType.Captain)
    private val _accountRole = MutableStateFlow<AccountRole?>(null)

    private val _state = MutableStateFlow(LoginState())
    val state =
        combine(_state, _currentPrisonID, _accountRole, _accountType){ loginState, currentPrisonID, accountRole, accountType ->
            loginState.copy(
                prisonID = currentPrisonID,
                currentRole = accountRole,
                currentType = accountType
            )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LoginState())

    fun onEvent(event : LoginEvent){
        when(event){
            is LoginEvent.ConfirmLogin -> {
                when(state.value.currentType){
                    AccountType.Captain -> {
                        _accountRole.value = AccountRole.Captain
                        event.onLoginCompleted(_accountRole.value as AccountRole.Captain)
                    }
                    AccountType.Inquisitor -> viewModelScope.launch{
                        prisonDAO.loginAsInquisitor(state.value.login, state.value.password, state.value.prisonID).
                        collectLatest {inquisitor ->
                            _accountRole.value = if(inquisitor != null) AccountRole.Inquisitor(inquisitor) else null
                            if(_accountRole.value != null) event.onLoginCompleted(_accountRole.value as AccountRole.Inquisitor)
                        }
                    }
                    AccountType.Prisoner -> viewModelScope.launch{
                        prisonDAO.loginAsPrisoner(state.value.login, state.value.password, state.value.prisonID).
                        collectLatest {prisoner ->
                            _accountRole.value = if(prisoner != null) AccountRole.Prisoner(prisoner) else null
                            if(_accountRole.value != null) event.onLoginCompleted(_accountRole.value as AccountRole.Prisoner)
                        }
                    }
                }
            }
            is LoginEvent.SetAccountType -> _accountType.value = event.accountType
            is LoginEvent.SetLogin -> _state.update {
                it.copy(login = event.newLogin)
            }
            is LoginEvent.SetPassword -> _state.update {
                it.copy(password = event.newPassword)
            }

            is LoginEvent.SetPrisonID -> _currentPrisonID.value = event.prisonID
        }
    }
}