package com.example.prison_app.data


sealed class AccountRole {
    data class Inquisitor(val inquisitor : com.example.prison_app.data.database.Inquisitor) : AccountRole()
    data class Prisoner(val prisoner : com.example.prison_app.data.database.Prisoner) : AccountRole()
    data object Captain : AccountRole()
}