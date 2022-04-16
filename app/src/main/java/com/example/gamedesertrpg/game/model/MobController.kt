package de.gelbelachente.desertrpggame.game.model

import de.gelbelachente.desertrpggame.game.DataObjects.Action
import de.gelbelachente.desertrpggame.game.DataObjects.Behaviour
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt

class MobController {


    fun start(){

        WorldTickController.get().observer.add(0) {
            val d = GlobalScope.async {
                val list = GameModel.get().obtainMobs()
                val playerPos = GameModel.get().player.playerPos
                for (mob in list) {

                    val distX = playerPos[0] - mob.posRealX
                    val multiplierX = distX / abs(distX)
                    val distY = playerPos[1] - mob.posRealY
                    val multiplierY = distY / abs(distY)
                    var rotateIfX = false
                    val attacking = (mob.type.behaviour == Behaviour.Agressive || mob.type.behaviour == Behaviour.Neutral
                            && mob.attacked >= System.currentTimeMillis() - 10000)


                    if(mob.posRealX in (playerPos[0]-.2f)..(playerPos[0]+.2f) && mob.posRealY in (playerPos[1]-.2f)..(playerPos[1]+.2f)
                        && attacking){
                        mob.action = Action.Attack
                        rotateIfX = mob.rotate

                    }else
                    if (attacking &&
                                (mob.posRealX.roundToInt() in playerPos[0].roundToInt() - 3 until playerPos[0].roundToInt() + 4
                                && mob.posRealY.roundToInt() in playerPos[1].roundToInt() - 3 until playerPos[1].roundToInt() + 4)) {
                        mob.action = Action.MoveUp

                            if (abs(distX) >= abs(distY)) {
                                mob.posRealX += multiplierX * mob.type.speed * .01f
                                if(multiplierX < 0){
                                    rotateIfX = true
                                }
                            } else {
                                mob.posRealY += multiplierY * mob.type.speed * .01f
                                rotateIfX = mob.rotate
                            }
                    } else {
                        mob.action = Action.MoveUp
                        val multiplierZ = (Random().nextInt(2).toFloat()-.5f) / .5f
                       if(Random().nextInt(2) == 0){
                           mob.posRealX += multiplierZ * mob.type.speed * .01f
                           if(multiplierZ < 0){
                               rotateIfX = true
                           }
                       }else{
                           mob.posRealY += multiplierZ * mob.type.speed * .01f
                           rotateIfX = mob.rotate
                       }
                    }


                    mob.computePosition(playerPos, GameModel.get().tileSize)
                    mob.rotate = rotateIfX
                }
            }
        }
    }


    companion object{

        private val instance = MobController()

        fun get() : MobController {
            return instance
        }

    }


}