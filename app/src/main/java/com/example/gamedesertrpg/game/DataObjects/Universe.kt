package de.gelbelachente.desertrpggame.game.DataObjects

import android.util.Log
import de.gelbelachente.desertrpggame.game.Constants
import java.util.*


data class Universe(val name : String = "Main", @Volatile var drawables : MutableList<Entity> = mutableListOf<Entity>()
                    , @Volatile var mobs : MutableList<Mob> = mutableListOf<Mob>(), @Volatile  var type : UniverseType = UniverseType.Open
){


    override fun toString(): String {
        val x = Constants.UNIVERSE_GENERAL
        return "$name$x$type$x"
    }
    companion object{
        fun fromString(str : String) : Universe {
            val x = Constants.UNIVERSE_GENERAL
            val tok = StringTokenizer(str,x)
            val name = tok.nextToken().trim()
           /* val drawables = tok.nextToken().replace("[","").replace("]","")
                .split(",").filter { it != "" }.map { Entity.fromString(it) }.toMutableList()
            val mobs = tok.nextToken().replace("[","").replace("]","")
                .split(",").filter { it != "" }.map { Mob.fromString(it) }.toMutableList()*/
            val xy = tok.nextToken().trim()
            val type = UniverseType.values().find { it.toString() == xy }!!
            return Universe(name, mutableListOf(), mutableListOf(),type)
        }
    }


    fun getEntities(xRange : ClosedFloatingPointRange<Float>, yRange : ClosedFloatingPointRange<Float>): MutableList<Entity> {
        val list = drawables.filter { it.posRealX in xRange && it.posRealY in yRange }
            .toMutableList()
        list.sortBy { it.type.type.z }
        return list
    }

    fun obtainMobs(xRange : ClosedFloatingPointRange<Float>, yRange : ClosedFloatingPointRange<Float>): List<Mob> {
        return mobs.toMutableList().filter { it.posRealX in xRange && it.posRealY in yRange }
    }

    fun updatePlayer(playerPos : MutableList<Float>,tileSize : Float) {
        drawables = drawables.toMutableList().map { it.computePosition(playerPos, tileSize) }
            .toMutableList()
        mobs = mobs.toMutableList()
            .map { it.setHeight(tileSize).setWidth(tileSize).computePosition(playerPos, tileSize) }
            .toMutableList()
    }

    override fun equals(other: Any?): Boolean {
        return (other as Universe).name == this.name
    }

}
