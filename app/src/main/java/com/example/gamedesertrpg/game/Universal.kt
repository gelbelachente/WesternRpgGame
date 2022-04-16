package de.gelbelachente.desertrpggame.game

import android.widget.ImageView
import com.example.gamedesertrpg.R
import kotlin.math.roundToInt







var _id : Int = 0
fun getId() : Int = _id++

fun Float.round(decimal : Double) : Float{
    var f = this * Math.pow(10.0, decimal)
    var ret = f.toFloat().roundToInt().toFloat()
    return ret/Math.pow(10.0,decimal).toFloat()
}

fun Int.toText() : String{
    return if (this == 0){
        "off"
    }else{
        "on"
    }
}

fun Int.toBoolean() : Boolean{
    return this != 0
}

val taunt_animation = mutableListOf<Int>(
    R.drawable.minotaur_01_taunt_000,
    R.drawable.minotaur_01_taunt_001,
    R.drawable.minotaur_01_taunt_002,
    R.drawable.minotaur_01_taunt_003,
    R.drawable.minotaur_01_taunt_004,
    R.drawable.minotaur_01_taunt_005,
    R.drawable.minotaur_01_taunt_006,
    R.drawable.minotaur_01_taunt_007,
    R.drawable.minotaur_01_taunt_008,
    R.drawable.minotaur_01_taunt_009,
    R.drawable.minotaur_01_taunt_010,
    R.drawable.minotaur_01_taunt_011,
    R.drawable.minotaur_01_taunt_012,
    R.drawable.minotaur_01_taunt_013,
    R.drawable.minotaur_01_taunt_014,
    R.drawable.minotaur_01_taunt_015,
    R.drawable.minotaur_01_taunt_016,
    R.drawable.minotaur_01_taunt_017,
)

val idle_animation = mutableListOf<Int>(
    R.drawable.minotaur_01_idle_000, R.drawable.minotaur_01_idle_001, R.drawable.minotaur_01_idle_002,
    R.drawable.minotaur_01_idle_003, R.drawable.minotaur_01_idle_004, R.drawable.minotaur_01_idle_005,
    R.drawable.minotaur_01_idle_006, R.drawable.minotaur_01_idle_007, R.drawable.minotaur_01_idle_008,
    R.drawable.minotaur_01_idle_009, R.drawable.minotaur_01_idle_010, R.drawable.minotaur_01_idle_011)

val walking_anim = mutableListOf<Int>(
    R.drawable.minotaur_01_walking_000, R.drawable.minotaur_01_walking_001, R.drawable.minotaur_01_walking_002,
    R.drawable.minotaur_01_walking_003, R.drawable.minotaur_01_walking_004, R.drawable.minotaur_01_walking_005,
    R.drawable.minotaur_01_walking_006, R.drawable.minotaur_01_walking_007, R.drawable.minotaur_01_walking_008,
    R.drawable.minotaur_01_walking_009, R.drawable.minotaur_01_walking_010, R.drawable.minotaur_01_walking_011,
    R.drawable.minotaur_01_walking_012, R.drawable.minotaur_01_walking_013, R.drawable.minotaur_01_walking_014,
    R.drawable.minotaur_01_walking_015, R.drawable.minotaur_01_walking_016, R.drawable.minotaur_01_walking_017
)

val attacking_anim = mutableListOf<Int>(
    R.drawable.minotaur_01_attacking_000, R.drawable.minotaur_01_attacking_001, R.drawable.minotaur_01_attacking_002,
    R.drawable.minotaur_01_attacking_003, R.drawable.minotaur_01_attacking_004, R.drawable.minotaur_01_attacking_005,
    R.drawable.minotaur_01_attacking_006, R.drawable.minotaur_01_attacking_007, R.drawable.minotaur_01_attacking_008,
    R.drawable.minotaur_01_attacking_009, R.drawable.minotaur_01_attacking_010, R.drawable.minotaur_01_attacking_011
)



val golem_walk = mutableListOf<Int>(
    R.drawable.golem_01_walking_000,
    R.drawable.golem_01_walking_001,
    R.drawable.golem_01_walking_002,
    R.drawable.golem_01_walking_003,
    R.drawable.golem_01_walking_004,
    R.drawable.golem_01_walking_005,
    R.drawable.golem_01_walking_006,
    R.drawable.golem_01_walking_007,
    R.drawable.golem_01_walking_008,
    R.drawable.golem_01_walking_009,
    R.drawable.golem_01_walking_010,
    R.drawable.golem_01_walking_011,
    R.drawable.golem_01_walking_012,
    R.drawable.golem_01_walking_013,
    R.drawable.golem_01_walking_014,
    R.drawable.golem_01_walking_015,
    R.drawable.golem_01_walking_016
)
val golem_attack = mutableListOf<Int>(
    R.drawable.golem_01_attacking_000,
    R.drawable.golem_01_attacking_001,
    R.drawable.golem_01_attacking_002,
    R.drawable.golem_01_attacking_003,
    R.drawable.golem_01_attacking_004,
    R.drawable.golem_01_attacking_005,
    R.drawable.golem_01_attacking_006,
    R.drawable.golem_01_attacking_007,
    R.drawable.golem_01_attacking_008,
    R.drawable.golem_01_attacking_009,
    R.drawable.golem_01_attacking_010,
    R.drawable.golem_01_attacking_011,
)

val dragon_walk = mutableListOf<Int>(
    R.drawable.walk1,
    R.drawable.walk2,
    R.drawable.walk3,
    R.drawable.walk4,
    R.drawable.walk5
)

val dragon_attack = mutableListOf<Int>(
    R.drawable.attack1,
    R.drawable.attack2,
    R.drawable.attack3,
    R.drawable.attack4
)
