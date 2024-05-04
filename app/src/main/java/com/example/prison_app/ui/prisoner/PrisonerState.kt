package com.example.prison_app.ui.prisoner

import com.example.prison_app.data.database.Prisoner
import java.util.UUID

data class PrisonerState(
    val prisoners : List<Prisoner> = emptyList(),
    val prisonID : UUID = UUID.randomUUID(),
    val authorisedPrisoner : Prisoner =
        Prisoner(login = "initial login", password = "123",
            firstName = "Initial name", surname = "Initial surname",
            patronymic = "Initial patronymic", prisonID = UUID.randomUUID(),
            age = 21, guiltyFor = "Being heretic", imprisonmentPeriod = 0.5),
    val selectedPrisoner : Prisoner? = null,
    val prisonerLogin : String = "",
    val prisonerPassword : String = "",
    val prisonerName : String = "",
    val prisonerSurname : String = "",
    val prisonerPatronymic : String = "",
    val prisonerAge : Int = 21,
    val prisonerPhotoFileName : String? = null,
    val prisonerCrime : String = "",
    val prisonerImprisonmentPeriod : Double = 1.0,
    val prisonerHealth : Int = 100,
    val prisonerPower : Int = 1,
    val prisonerIsDangerous : Boolean = false,
    val isEditingPrisoner: Boolean = false,
    val isHealing : Boolean = false,
    val isPoweringUp : Boolean = false,
    val isFighting : Boolean = false,
    )