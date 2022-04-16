package de.gelbelachente.desertrpggame.Data

import androidx.room.Entity
import androidx.room.PrimaryKey
import de.gelbelachente.desertrpggame.game.Constants
import de.gelbelachente.desertrpggame.game.DataObjects.Player
import de.gelbelachente.desertrpggame.game.DataObjects.Universe
import java.util.*

@Entity
data class World(@PrimaryKey var name : String, var lastPlayed : String = System.currentTimeMillis().toString(), var dayTime : Int = 0,
                 var player : String = Player().toString(), var mobs : String = "Main${Constants.MODEL_LIST}",
                 var entities : String = "Main${Constants.MODEL_LIST}",
                 var univs : String = mutableListOf(Universe("Main")).joinToString(Constants.MODEL_LIST),
                 val createdAt : String = System.currentTimeMillis().toString(), var maxId : Int = 0){

    override fun toString(): String {
        val x = Constants.WORLD_GENERAL
        return "$name$x$lastPlayed$x$dayTime$x$player$x$mobs$x$entities$x$univs$x$createdAt$x"
    }

    companion object {

        fun fromString(str: String) : World {
            val tok = StringTokenizer(str,Constants.WORLD_GENERAL)
            val name = tok.nextToken()
            val lastPlayed = tok.nextToken()
            val dayTime = tok.nextToken()
            val player = tok.nextToken()
            val mobs = tok.nextToken()
            val entities = tok.nextToken()
            val univs = tok.nextToken()
            val createdAt = tok.nextToken()

            return World(name,lastPlayed,dayTime.toInt(),player,mobs,entities,univs,createdAt)
        }
    }
}
