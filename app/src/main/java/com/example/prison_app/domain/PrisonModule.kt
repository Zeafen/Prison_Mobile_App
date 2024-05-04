package com.example.prison_app.domain

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.prison_app.data.database.PrisonDAO
import com.example.prison_app.data.database.PrisonDatabase
import com.example.prison_app.data.database.migration_1_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

const val DATABASE_NAME = "prison_database.db"

@Module
@InstallIn(SingletonComponent::class)
object PrisonModule {

    @Singleton
    @Provides
    fun providePrisonDAO(app: Application): PrisonDAO {
        val db =
            Room.databaseBuilder(app.applicationContext, PrisonDatabase::class.java, DATABASE_NAME)
                .addMigrations(
                    migration_1_2
                )
                .build()
        return db.prisonDAO();
    }
}