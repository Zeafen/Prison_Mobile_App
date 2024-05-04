package com.example.prison_app.data.database

import androidx.annotation.UiThread
import com.example.prison_app.data.AccountRole
import com.example.prison_app.data.AccountType
import java.util.UUID

sealed interface LoginEvent {
    data class SetLogin(val newLogin : String) : LoginEvent
    data class SetPassword(val newPassword : String) : LoginEvent
    data class SetAccountType(val accountType : AccountType) : LoginEvent
    data class SetPrisonID(val prisonID : UUID) : LoginEvent
    data class ConfirmLogin(val onLoginCompleted : (AccountRole) -> Unit) : LoginEvent
}