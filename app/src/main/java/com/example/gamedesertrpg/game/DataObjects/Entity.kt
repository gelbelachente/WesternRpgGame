package de.gelbelachente.desertrpggame.game.DataObjects

import de.gelbelachente.desertrpggame.game.Constants
import de.gelbelachente.desertrpggame.game.MetaData.Generator
import de.gelbelachente.desertrpggame.game.getId
import java.util.*
import kotlin.math.roundToInt



data class Entity(val type : Material, var posX : Float = 0f, var posY : Float = 0f, var posRealX : Float, var posRealY : Float
                  , var height : Int = 0, var width : Int = 0, val id : Int = getId(), val gen: Generator? = null){

    override fun toString(): String {
        val x = Constants.ENTITY_GENERAL
        return "$type$x$posRealX$x$posRealY$x$id$x$gen$x"
    }

    companion object{
        fun fromString(str : String) : Entity {
            val tok = StringTokenizer(str,Constants.ENTITY_GENERAL)
            val type = Material.valueOf(tok.nextToken().trim())
            val posRealX = tok.nextToken().toFloat()
            val posRealY = tok.nextToken().toFloat()
            val id = tok.nextToken().toInt()
            val gen = Generator.fromString(tok.nextToken(),type.metaData)
            return Entity(type,0f,0f,posRealX,posRealY,0,0,id,gen)
        }
    }

    fun setHeight(tileSize : Float) : Entity {
        this.height = (tileSize*this.type.h).roundToInt()
        return this
    }
    fun setWidth(tileSize : Float) : Entity {
        this.width = (tileSize*this.type.w).roundToInt()
        return this
    }
    fun computePosition(playerPos : MutableList<Float>,tileSize: Float) : Entity {
        posX = (posRealX - playerPos[0]) * tileSize - this.width/2
        posY = (posRealY - playerPos[1]) * tileSize - this.height/2
        return this
    }

    override fun equals(other: Any?): Boolean {
        var equals = true
        equals = other is Entity
        if(!equals) return false;
        val sec = other as Entity
        return sec.id == this.id
    }

}