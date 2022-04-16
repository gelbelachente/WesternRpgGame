package de.gelbelachente.desertrpggame.game.DataObjects

import de.gelbelachente.desertrpggame.game.getId
import java.util.*
import kotlin.math.roundToInt

data class Mob(val type : NPC, var posX : Float, var posY : Float, var posRealX: Float, var posRealY: Float,
               var height: Int = 0, var width: Int = 0, var attacked : Long = 0, val id : Int = getId(), var rotate : Boolean = false,
               var anim : Int = 0, var live : Int = type.live, var viewCreated : Boolean = false,
               var action: Action = Action.MoveUp
){


    override fun toString(): String {
        return "$type%$posRealX%$posRealY%$attacked%$id%"
    }

    companion object{
        fun fromString(str : String) : Mob {
            val tok = StringTokenizer(str,"%")
            val type = NPC.valueOf(tok.nextToken())
            val posRealX = tok.nextToken().toFloat()
            val posRealY = tok.nextToken().toFloat()
            val attacked = tok.nextToken().toLong()
            val id = tok.nextToken().toInt()
            return Mob(type,0f,0f,posRealX,posRealY,0,0,attacked,id)
        }
    }

    fun setHeight(tileSize : Float) : Mob {
        this.height = (tileSize*this.type.h).roundToInt()
        return this
    }
    fun setWidth(tileSize : Float) : Mob {
        this.width = (tileSize*this.type.w).roundToInt()
        return this
    }
    fun computePosition(playerPos : MutableList<Float>,tileSize: Float) : Mob {
        posX = (posRealX - playerPos[0]) * tileSize - this.width/2
        posY = (posRealY - playerPos[1]) * tileSize - this.height/2
        return this
    }

    override fun equals(other: Any?): Boolean {
        var equals = true
        equals = other is Mob
        if(!equals) return false;
        val sec = other as Mob
        equals = equals && sec.type == this.type
        equals = equals && sec.posRealX == this.posRealX
        equals = equals && sec.posRealY == this.posRealY
        return equals
    }
}