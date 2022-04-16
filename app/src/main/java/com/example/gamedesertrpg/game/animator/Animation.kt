package de.gelbelachente.desertrpggame.game.animator

import de.gelbelachente.desertrpggame.game.DataObjects.Action

data class Animation(val walking : MutableList<Int>?,val attacking : MutableList<Int>?,
                    val hurt : MutableList<Int>?,val taunt : MutableList<Int>?,
                    val dying : MutableList<Int>?,val idle : MutableList<Int>?){

    fun get(action: Action) : MutableList<Int>?{
        return when(action){
            Action.MoveRight -> walking
            Action.MoveUp -> walking
            Action.MoveDown -> walking
            Action.MoveLeft -> walking
            Action.Attack -> attacking
            Action.Hurt -> hurt
            Action.Taunt -> taunt
            Action.Dying -> dying
            Action.Idle -> idle
        }

    }

}
