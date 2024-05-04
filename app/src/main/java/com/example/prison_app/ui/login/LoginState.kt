package com.example.prison_app.ui.login

import com.example.prison_app.data.AccountRole
import com.example.prison_app.data.AccountType
import java.util.UUID

data class LoginState(
    val login : String = "",
    val password : String = "",
    val currentRole : AccountRole? = null,
    val currentType : AccountType = AccountType.Captain,
    val prisonID : UUID = UUID.randomUUID(),
)