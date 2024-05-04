package com.example.prison_app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Prisoner::class,
    Prison::class,
    Inquisitor::class],
    version=2)
abstract class PrisonDatabase : RoomDatabase() {
    abstract fun prisonDAO() : PrisonDAO
}
val migration_1_2 = object : Migration(1,2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE `Prisoner_BackUp` (" +
                "`id` BLOB NOT NULL, `login` TEXT NOT NULL, " +
                "`password` TEXT NOT NULL, `firstName` TEXT NOT NULL, " +
                "`surname` TEXT NOT NULL, `patronymic` TEXT NOT NULL, " +
                "`age` INTEGER NOT NULL, `guiltyFor` TEXT NOT NULL, " +
                "`imprisonmentPeriod` REAL NOT NULL, `isDangerous` INTEGER NOT NULL, " +
                "`health` INTEGER NOT NULL, `power` INTEGER NOT NULL, `prisonID` BLOB NOT NULL, " +
                "`photoFileName` TEXT, PRIMARY KEY(`id`))")
        db.execSQL("INSERT INTO Prisoner_BackUp SELECT id, " +
                "login, password, firstName, surname, patronymic," +
                " age, guiltyFor, imprisonmentPeriod, isDangerous," +
                " health, power, prisonID, photoFileName  FROM Prisoner")
        db.execSQL("DROP TABLE Prisoner")
        db.execSQL("ALTER TABLE Prisoner_BackUp RENAME to Prisoner")
    }
}