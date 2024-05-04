package com.example.prison_app.ui.prison

import com.example.prison_app.data.database.Prison

data class PrisonState(
    val prisons : List<Prison> = emptyList(),
    val selectedPrison : Prison? = null,
    val prisonName : String = "",
    val prisonAddress : String = "",
    val prisonPhotoFileName : String? = null,
    val isEditingPrison: Boolean = false
)