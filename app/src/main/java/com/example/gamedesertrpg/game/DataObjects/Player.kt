package de.gelbelachente.desertrpggame.game.DataObjects

import android.util.Log
import com.example.gamedesertrpg.game.DataObjects.Skill
import de.gelbelachente.desertrpggame.game.Constants
import java.util.*


data class Player(
    var currentUniverse: String = "Main",
    var playerPosAll: MutableMap<String, MutableList<Float>>
    = mutableMapOf(currentUniverse to mutableListOf(0f, 0f)),
    var live: Int = 100,
    var offHand: Material? = null,
    var trades: MutableMap<Entity, Trade> = mutableMapOf<Entity, Trade>(),
    var lastSleepingPoint: MutableList<Float> =
        playerPosAll[currentUniverse]!!.toMutableList(),
    var inventory: MutableMap<Material, Int> = mutableMapOf<Material, Int>().apply {
        for (item in Material.values()) {
            put(item, 0)
        }
    },
    var skills : MutableList<Skill> = mutableListOf()
){

    override fun toString(): String {
        val x = Constants.PLAYER_GENERAL
        val y = Constants.PLAYER_LIST
        val m1 = Constants.PLAYER_MAP_ONE
        val m2 = Constants.PLAYER_MAP_TWO
        return this.currentUniverse + x + this.playerPosAll.map { it.key + m1 + it.value[0] + m2 + it.value[1] } + x + this.live + x +
                this.offHand?.toString() + x + this.trades.map { "${it.key.toString()}$y${it.value.toString()}"}.toString() +
                x + lastSleepingPoint.toString() + x + inventory.map { it.key.toString() + y + it.value }+
                x + skills.joinToString(y)
    }

    companion object{
        fun fromString(str: String) : Player
        {
            val x = Constants.PLAYER_GENERAL
            val y = Constants.PLAYER_LIST
            val m1 = Constants.PLAYER_MAP_ONE
            val m2 = Constants.PLAYER_MAP_TWO

            val tok = StringTokenizer(str,x)
            var uni = tok.nextToken()
            val playerPosAll = mutableMapOf<String,MutableList<Float>>()
            tok.nextToken().replace("[","").replace("]","")
                .split(",").forEach {
                    val univ = it.substringBefore(m1)
                    val a = it.substringAfter(m1).substringBefore(m2)
                    val b = it.substringAfter(m1).substringAfter(m2)
                    playerPosAll[univ] = mutableListOf(a.toFloat(),b.toFloat())
                }
            var live = tok.nextToken().toInt()
            var offhand = tok.nextToken()
            var Offhand : Material? = null
            if(offhand != null && offhand != "null"){
                Offhand = Material.valueOf(offhand)
            }
            var trades = mutableMapOf<Entity, Trade>()
            tok.nextToken().replace("[","").replace("]","")
                .split(",").filter { it != "" }.forEach {
                     if (it != null && it != ""){
                         Log.d("Bugger",it)
                         val a = it.split(y)[0].trim()
                         val b = it.split(y)[1].trim()
                         trades[Entity.fromString(a)] = Trade.fromString(b)
                     }
                 }
            var sleepingPoint = tok.nextToken().replace("[","").replace("]","")
                .split(",").map { it.toFloat() }.toMutableList()
            var inventory = mutableMapOf<Material,Int>()
            tok.nextToken().replace("[","").replace("]","")
                .split(",").forEach {
                    if (it != null && it != "") {
                        val a = it.split(y)[0].trim()
                        val b = it.split(y)[1].trim()
                        inventory[Material.valueOf(a)] = b.toInt()
                    }
                }
            var skills = tok.nextToken().split(y).filter{it != ""}.map { Skill.valueOf(it) }.toMutableList()
            return Player(uni,playerPosAll,live,Offhand, trades,sleepingPoint,inventory,skills)
        }
    }


    fun setPos(x : Float,y:Float){
        playerPosAll[currentUniverse]!![0]+=x
        playerPosAll[currentUniverse]!![1]+=y
    }

    fun changeUniverse(universe: Universe){
        this.currentUniverse = universe.name
        if(!this.playerPosAll.keys.contains(universe.name)) {
            this.playerPosAll[currentUniverse] = mutableListOf(0f, 0f)
        }
    }

 val playerPos : MutableList<Float> get(){
     val ret = playerPosAll[currentUniverse]
     return if(ret == null){
         playerPosAll[currentUniverse] = mutableListOf(0f,0f)
         mutableListOf(0f,0f)
     }else{
         ret!!
     }
 }



}
