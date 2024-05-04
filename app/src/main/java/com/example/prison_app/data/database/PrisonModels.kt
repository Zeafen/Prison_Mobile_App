package com.example.prison_app.data.database

import android.app.backup.BackupAgent
import androidx.annotation.OpenForTesting
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.FileNameMap
import java.util.UUID

@Entity
data class Prison(
    @PrimaryKey
    val id : UUID = UUID.randomUUID(),
    val prisonName : String,
    val prisonAddress : String,
    val photoFileName: String? = null
)

@Entity
data class Inquisitor(
    @PrimaryKey
    val id : UUID = UUID.randomUUID(),
    val prisonID : UUID,
    val firstName: String,
    val surname : String,
    val patronymic: String,
    val age : Int,
    val login : String,
    val password: String,
    val photoFileName : String? = null
)

@Entity
data class Prisoner(
    @PrimaryKey
    val id : UUID = UUID.randomUUID(),
    val login : String,
    val password : String,
    val firstName : String,
    val surname : String,
    val patronymic : String,
    val age : Int,
    val guiltyFor : String,
    val imprisonmentPeriod : Double,
    val isDangerous : Boolean = false,
    val health : Int = 100,
    val power : Int = 1,
    val prisonID : UUID,
    val photoFileName: String? = null
)
