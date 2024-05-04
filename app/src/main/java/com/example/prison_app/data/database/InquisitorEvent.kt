package com.example.prison_app.data.database

import java.util.UUID

sealed interface InquisitorEvent {
    data object SaveInquisitor: InquisitorEvent
    data class SetInquisitorName(val inquisitorName : String) : InquisitorEvent
    data class SetInquisitorSurname(val inquisitorSurname : String) : InquisitorEvent
    data class SetInquisitorPatronymic(val inquisitorPatronymic : String) : InquisitorEvent
    data class SetInquisitorAge(val inquisitorAge : Int) : InquisitorEvent
    data class SetInquisitorLogin(val inquisitorLogin : String) : InquisitorEvent
    data class SetInquisitorPassword(val inquisitorPassword : String) : InquisitorEvent
    data class DeleteInquisitor(val inquisitorID : UUID) : InquisitorEvent
    data class SelectedInquisitorChanged(val inquisitor : Inquisitor) : InquisitorEvent
    data class SelectedPrisonChanged(val prisonID : UUID) : InquisitorEvent
    data class AuthorisedInquisitorChanged(val inquisitor : Inquisitor) : InquisitorEvent
    data object OpenDialog : InquisitorEvent
    data object HideDialog : InquisitorEvent
}