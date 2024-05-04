package com.example.prison_app.data.database

import java.util.UUID

sealed interface PrisonerEvent {
    data object SavePrisoner: PrisonerEvent
    data object GoToGym : PrisonerEvent
    data object GoToDiningRoom : PrisonerEvent
    data class FightPrisoner(val enemy : Prisoner) : PrisonerEvent
    data class SetPrisonerName(val prisonerName : String) : PrisonerEvent
    data class SetPrisonerSurname(val prisonerSurname : String) : PrisonerEvent
    data class SetPrisonerPatronymic(val prisonerPatronymic : String) : PrisonerEvent
    data class SetPrisonerAge(val prisonerAge : Int) : PrisonerEvent
    data class SetPrisonerLogin(val prisonerLogin : String) : PrisonerEvent
    data class SetPrisonerPassword(val prisonerPassword : String) : PrisonerEvent
    data class SetPrisonerCrime(val prisonerCrime : String) : PrisonerEvent
    data class SetPrisonerImprisonmentPeriod(val prisonerPeriod : Double) : PrisonerEvent
    data class SetPrisonerHealth(val prisonerHealth : Int) : PrisonerEvent
    data class SetPrisonerPower(val prisonerPower : Int) : PrisonerEvent
    data class SetPrisonerIsDangerous(val prisonerIsDangerous : Boolean) : PrisonerEvent
    data class DeletePrisoner(val prisonerID : UUID) : PrisonerEvent
    data class SelectedPrisonerChanged(val prisoner : Prisoner) : PrisonerEvent
    data class SelectedPrisonChanged(val prisonID : UUID) : PrisonerEvent
    data class AuthorisedPrisonerChanged(val prisoner : Prisoner) : PrisonerEvent
    data object OpenEditDialog : PrisonerEvent
    data object OpenHealDialog : PrisonerEvent
    data object OpenPowerUpDialog : PrisonerEvent
    data object HideDialog : PrisonerEvent
}