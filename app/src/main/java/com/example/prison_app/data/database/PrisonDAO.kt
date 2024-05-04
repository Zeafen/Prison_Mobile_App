package com.example.prison_app.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface PrisonDAO {

    //Prisoners
    @Upsert
    suspend fun upsertPrisoner(newPrisoner: Prisoner)

    @Query("Delete from Prisoner where id = :prisoner_id")
    suspend fun burnHeretic(prisoner_id : UUID)

    @Query("Select * from Prisoner")
    fun getAllHeretics() : Flow<List<Prisoner>>

    @Query("Select * from Prisoner where prisonID = :prisonID")
    fun getHereticsInPrison(prisonID : UUID) : Flow<List<Prisoner>>

    @Query("Select * from Prisoner where id = :hereticID")
    fun getHeretic(hereticID : UUID) : Flow<Prisoner?>

    @Query("Select * from Prisoner where login = :login and password = :password and prisonID = :prisonID")
    fun loginAsPrisoner(login : String, password : String, prisonID: UUID) : Flow<Prisoner?>

    //Prisons
    @Upsert
    suspend fun upsertPrison(newPrison : Prison)

    @Query("Delete from Prison where id = :prisonID")
    suspend fun removePrison(prisonID : UUID)

    @Query("Select * from Prison")
    fun getAllPrisons() : Flow<List<Prison>>

    @Query("Select * from Prison where id = :prisonID")
    fun getPrison(prisonID : UUID) : Flow<Prison?>

    //Inquisitors
    @Upsert
    suspend fun upsertInquisitor(newPrison : Inquisitor)

    @Query("Select * from Inquisitor where id = :inquisitorID")
    fun getInquisitor(inquisitorID: UUID) : Flow<Inquisitor?>

    @Query("Delete from Inquisitor where id = :inquisitorID")
    suspend fun removeInquisitor(inquisitorID : UUID)

    @Query("Select * from Inquisitor")
    fun getAllInquisitors() : Flow<List<Inquisitor>>
    @Query("Select * from Inquisitor where prisonID = :prisonID")
    fun getInquisitorsAtPrison(prisonID : UUID) : Flow<List<Inquisitor>>
    @Query("Select * from Inquisitor where login = :login and password = :password and prisonID = :prisonID")
    fun loginAsInquisitor(login : String, password : String, prisonID: UUID) : Flow<Inquisitor?>
}