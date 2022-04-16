package de.gelbelachente.desertrpggame.Data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@androidx.room.Database(entities = arrayOf(World::class,Setting::class), version = 6)
abstract class Database() : RoomDatabase(){

    abstract fun worldDao() : worldDao

    companion object{
        @Volatile private var instance : Database? = null
        fun getDatabase(ctx : Context) : Database?{

            return instance ?: synchronized(this){
                val INSTANCE = Room.databaseBuilder(ctx,Database::class.java,"DesertRPGGameDatabase")
                    .allowMainThreadQueries().fallbackToDestructiveMigration().build()
                instance = INSTANCE
                INSTANCE
            }
        }
    }

}