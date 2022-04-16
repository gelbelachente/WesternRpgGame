package de.gelbelachente.desertrpggame.game.model

import android.util.Log
import de.gelbelachente.desertrpggame.Data.World
import com.example.gamedesertrpg.SharedStateHolderViewModel
import com.example.gamedesertrpg.game.MetaData.DefenseMetaData
import de.gelbelachente.desertrpggame.game.Constants
import de.gelbelachente.desertrpggame.game.DataObjects.*
import de.gelbelachente.desertrpggame.game.MetaData.FacilityMetaData
import de.gelbelachente.desertrpggame.game.MetaData.WeaponMetaData
import de.gelbelachente.desertrpggame.game._id
import java.lang.Exception
import java.util.*
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.roundToInt


class GameModel {

    var drawables: MutableList<Entity>
        get() = getUniverse().drawables
        set(value) {
            getUniverse().drawables = value
        }

    var mobs: MutableList<Mob>
        get() = getUniverse().mobs
        set(value) {
            getUniverse().mobs = mobs
        }

    var player = Player()

    val model = ComputerModel()

    val tileSize: Float get() = model.tileSize
    val mapSize: MutableList<Float> get() = model.mapSize

    @Volatile
    lateinit var gameWorld: World

    @Volatile
    var universe = mutableListOf<Universe>(Universe("Main"))

    @Synchronized
    fun getUniverse(): Universe {
        return universe.toMutableList().find { it.name.equals(player.currentUniverse) }!!
    }

    fun computeConstants(width: Float, height: Float) {
        model.computeConstants(width, height)
    }

    private fun xMapRange() =
        (player.playerPos[0] - (mapSize[0] / tileSize) / 1.5f)..(player.playerPos[0] + (mapSize[0] / tileSize) / 1.5f)

    private fun yMapRange() =
        (player.playerPos[1] - (mapSize[1] / tileSize) / 1.5f)..(player.playerPos[1] + (mapSize[1] / tileSize) / 1.5f)


    fun getEntities(): MutableList<Entity> {
        return getUniverse().getEntities(xMapRange(), yMapRange())
    }

    fun obtainMobs(): List<Mob> {
        return getUniverse().obtainMobs(xMapRange(), yMapRange())
    }

    fun updatePlayer(x: Float, y: Float) {
        player.setPos(x, y)
        getUniverse().updatePlayer(player.playerPos, model.tileSize)
    }

    fun startInteraction(
        func: (Trade, Boolean) -> (Unit), slept: (Boolean) -> (Unit),
        question: (Question, Material) -> (Unit), toast: (String) -> (Unit), eat: () -> (Unit)
    ): Boolean {
        return InteractionController.startInteraction(func, slept, question, toast, eat)
    }

    fun qaReward(material: Material, won: Boolean = true) {
        InteractionController.qaReward(material, won)
    }

    fun trade(trade: Trade) {
        InteractionController.trade(trade)
    }

    fun getMob(): NPC {
        val mob = getUniverse().type.npc.random()
        return if (Random().nextInt(mob.spawning) <= 1) {
            mob
        } else {
            getMob()
        }
    }

    fun getMult(): Int {
        val r = (Random().nextInt(2) - .5f)
        return (r / abs(r)).roundToInt()
    }

    fun getMobPos(): MutableList<Float> {

        val x = player.playerPos[0] + 4f * getMult() + (Random().nextInt(3)
            .toFloat() / 2f) * getMult() - .75f
        val y = player.playerPos[1] + 2f * getMult() + (Random().nextInt(3)
            .toFloat() / 2f) * getMult() - .75f
        return mutableListOf(x, y)
    }

    fun spawn() {
        if (getUniverse().type.npc.size > 0)
            if (WorldTickController.get().time == DayTime.Night) {
                getUniverse().mobs.add(
                    Mob(getMob(), 0f, 0f, getMobPos()[0], getMobPos()[1])
                        .setWidth(tileSize).setHeight(tileSize)
                        .computePosition(player.playerPos, tileSize)
                )
            }
    }


    private fun xRange(range: Float) =
        (player.playerPos[0] - range)..(player.playerPos[0] + range)

    private fun yRange(range: Float) =
        (player.playerPos[1] - range)..(player.playerPos[1] + range)

    fun enter(toast: (String) -> (Unit)): Boolean {
        val list = drawables.filter { it.posRealX in xRange(.3f) && it.posRealY in yRange(.3f) }
            .filter { it.type.type == Type.building && it.type.metaData != null && it.type.metaData is FacilityMetaData }
        if (list.isNotEmpty()) {
            val b = list[0]
            val name = b.type.toString()
            if (universe.any { it.name == name }) {
                player.changeUniverse(universe.find { it.name == name }!!)
            } else {
                val uni = Universe(name)
                uni.type = (b.type.metaData as FacilityMetaData).getUniverseType(b.type)

                universe.add(uni)
                player.changeUniverse(uni)
            }
            player.playerPosAll[player.currentUniverse] = mutableListOf(b.posRealX, b.posRealY)
            toast("travelled to $name")
            return true
        } else {
            player.changeUniverse(universe.find { it.name == "Main" }!!)
            toast("changed to Main")
            return false
        }
    }

    fun getPlayerDamage(): Int {
        return if (player.offHand == null) {
            5
        } else {
            if (player.offHand!!.type == Type.Weapon && player.offHand!!.metaData is WeaponMetaData) {
                val meta = player.offHand!!.metaData as WeaponMetaData
                meta.damage
            } else {
                5
            }
        }
    }

    private fun getDeriv(): Float {
        return (Random().nextInt(11).toFloat() / 10f) - .5f
    }


    fun attack(): MutableList<Boolean> {
        val ret = mutableListOf<Boolean>()
        if (mobs.any { it.posRealX in xRange(.3f) && it.posRealY in yRange(.3f) }) {
            val mob =
                mobs.find { it.posRealX in xRange(.3f) && it.posRealY in yRange(.3f) }!!
            mob.live -= getPlayerDamage()
            mob.attacked = System.currentTimeMillis()
            ret.add(mob.live <= 0)
            if (mob.live <= 0) {
                for (item in mob.type.drops) {
                    if (Random().nextInt(10) <= 6) {
                        for (i in 1..Random().nextInt(item.value - 1) + 1) {
                            drawables.add(
                                Entity(
                                    item.key,
                                    0f,
                                    0f,
                                    player.playerPos[0] + getDeriv(),
                                    player.playerPos[1] + getDeriv()
                                )
                                    .setWidth(tileSize).setHeight(tileSize)
                                    .computePosition(player.playerPos, tileSize)
                            )
                        }
                    }
                }
            }
            ret.add(true)
        } else {
            ret.add(false)
            ret.add(false)
        }
        return ret
    }

    fun destroy() {
        InteractionController.destroy()
    }

    fun doDamage(id: Int): Boolean {
        try {
            val mob = mobs.find { it.id == id }!!
            if (mob.posRealX in xRange(.2f) && mob.posRealY in yRange(.2f)) {
                var damage = mob.type.damage
                if(player.offHand != null && player.offHand!!.metaData != null && player.offHand!!.metaData is DefenseMetaData){
                    damage -= ((player.offHand!!.metaData as DefenseMetaData).def * damage).roundToInt()
                }
                player.live -= damage
            }
            if (player.live <= 0) {
                player.inventory.keys.forEach { player.inventory[it] = 0 }
                player.playerPosAll[getUniverse().name] = player.lastSleepingPoint.toMutableList()
                mobs.remove(mob)
                player.live = 75
                return true
            }
        } catch (e: Exception) {
        }
        return false
    }


    fun render() {
        val missing = WorlRenderer.checkIfMissing(
            drawables.filter { it.type.type == Type.tile }.toMutableList(),
            player.playerPos,
            floor((mapSize[0] / tileSize) + 1f).toInt() / 2,
            floor((mapSize[1] / tileSize) + 1f).toInt() / 2
        )
        val xy = WorlRenderer.fillMissing(missing, getUniverse().type)
            .map {
                it.setHeight(tileSize).setWidth(tileSize)
                    .computePosition(player.playerPos, tileSize)
            }
        drawables.addAll(xy)
        mobs.map {
            it.setHeight(tileSize).setWidth(tileSize)
                .computePosition(player.playerPos, tileSize)
        }
    }

    fun build(m: Material, fee: Boolean = true): Boolean {
        return InteractionController.build(m, fee)
    }

    fun craft(recipe: Recipe) {
        if (!player.inventory.keys.contains(recipe.product)) {
            player.inventory[recipe.product] = 0
        }
        player.inventory[recipe.product] = player.inventory[recipe.product]!! + recipe.amount
        for ((idx, ing) in recipe.ingredients.withIndex()) {
            player.inventory[ing] = player.inventory[ing]!! - recipe.ingredientAmount[idx]
            if (player.inventory[ing]!! < 0) {
                player.inventory[ing] = 0
            }
        }
    }

    fun start() {

        val x = Constants.MODEL_GENERAL
        val y = Constants.MODEL_LIST
        val z = Constants.MODEL_NESTED_LIST
        val world = gameWorld

        universe = mutableListOf()
        WorldTickController.get().dayTime = world.dayTime
        world.univs.split(y).filter{it != ""}.forEach { universe.add(Universe.fromString(it)) }
        player = Player.fromString(world.player)
        _id = gameWorld.maxId
        world.mobs.split(x).filter{it != ""}.forEach {
                val univ = it.substringBefore(y).trim()
                val mob = it.substringAfter(y).trim()
                if (mob != "") {
                    universe.find { it.name == univ }!!.mobs =
                        mob.split(z).filter{it != ""}.map {
                            Mob.fromString(it)
                                .setHeight(tileSize).setWidth(tileSize)
                                .computePosition(player.playerPos, tileSize)
                        }.toMutableList()
                }
            }
        world.entities.split(x).filter{it != ""}.forEach {
                val univ = it.substringBefore(y).trim()
                val mob = it.substringAfter(y).trim()
                if (mob != "") {
                    universe.find { it.name == univ }!!.drawables =
                        mob.split(z).filter{it != ""}.map {
                            Entity.fromString(it)
                                .setHeight(tileSize).setWidth(tileSize)
                                .computePosition(player.playerPos, tileSize)
                        }.toMutableList()
                }
            }

    }

    fun stop() {
        synchronized(this) {
            val x = Constants.MODEL_GENERAL
            val y = Constants.MODEL_LIST
            val z = Constants.MODEL_NESTED_LIST
            WorldTickController.get().dayTime = gameWorld.dayTime
            gameWorld.maxId = _id
            gameWorld.player = player.toString()
            gameWorld.univs = universe.joinToString(""){it.toString() + y}
            gameWorld.mobs = universe.toMutableList()
                .joinToString { it.name + y + it.mobs.joinToString(""){it.toString() + z} + x }
            gameWorld.entities = universe.toMutableList()
                .joinToString("") { it.name + y + it.drawables.joinToString(""){it.toString() + z}.toString() + x }
            gameWorld.lastPlayed = System.currentTimeMillis().toString()
            SharedStateHolderViewModel.get().worldDao.updateWorld(gameWorld)
        }
    }

    fun setWorld(world: World) {
        gameWorld = world
    }


    companion object {

        private var instance = GameModel()

        fun get(): GameModel {
            return instance
        }

    }


}