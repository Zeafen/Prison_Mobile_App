package com.example.prison_app.data

import com.example.prison_app.R

enum class AccountType(
    val text : Int
) {
    Captain(text = R.string.captain_account_type,),
    Inquisitor(text = R.string.inquisitor_account_type),
    Prisoner(text = R.string.prisoner_account_type)
}