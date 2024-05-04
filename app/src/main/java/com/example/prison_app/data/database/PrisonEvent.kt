package com.example.prison_app.data.database

import java.util.UUID

sealed interface PrisonEvent {
    data object SavePrison: PrisonEvent
    data class SetPrisonName(val prisonName : String) : PrisonEvent
    data class SetPrisonAddress(val prisonAddress : String) : PrisonEvent
    data class DeletePrison(val prisonID : UUID) : PrisonEvent
    data class SelectedPrisonChanged(val prison : Prison) : PrisonEvent
    data object OpenDialog : PrisonEvent
    data object HideDialog : PrisonEvent
}