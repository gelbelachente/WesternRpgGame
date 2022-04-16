package de.gelbelachente.desertrpggame.Data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface worldDao {

    @Query("SELECT * FROM World ORDER BY lastPlayed DESC")
    fun getWorlds() : Flow<List<World>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWorld(world: World)

    @Delete
    fun removeWorld(world: World)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateWorld(world: World)

    @Query("SELECT * FROM World ORDER BY lastPlayed DESC")
    fun getWorldsAsPrimitive() : List<World>

    @Query("Select * from World Where name = :name")
    fun getWorld(name : String) : World

    @Query("SELECT * FROM Setting")
    fun getSettings() : List<Setting>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setSetting(setting : Setting)

}