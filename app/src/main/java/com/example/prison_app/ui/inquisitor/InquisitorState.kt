package com.example.prison_app.ui.inquisitor

import com.example.prison_app.data.database.Inquisitor
import java.util.UUID

data class InquisitorState(
    val inquisitors : List<Inquisitor> = emptyList(),
    val authorisedInquisitor : Inquisitor =
        Inquisitor(firstName = "Initial name", surname = "Initial surname",
            patronymic = "Initial Patronymic", age = -21, login = "initial login",
            password = "123", prisonID = UUID.randomUUID()),
    val selectedInquisitor : Inquisitor? = null,
    val inquisitorName : String = "",
    val inquisitorSurname : String = "",
    val inquisitorPatronymic : String = "",
    val inquisitorAge : Int = 21,
    val inquisitorLogin : String = "",
    val inquisitorPassword : String = "",
    val inquisitorPhotoFileName : String? = null,
    val prisonID : UUID = UUID.randomUUID(),
    val isEditingInquisitor : Boolean = false
)
