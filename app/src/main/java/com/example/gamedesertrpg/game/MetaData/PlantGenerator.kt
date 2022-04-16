package com.example.gamedesertrpg.game.MetaData

import de.gelbelachente.desertrpggame.game.Constants
import de.gelbelachente.desertrpggame.game.MetaData.Generator
import kotlin.math.floor
import kotlin.math.roundToInt

class PlantGenerator(val mt : PlantMetaData, var lastTimeCheck : Long = System.currentTimeMillis()
                     , var compensation : MutableList<Long> = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)) : Generator() {

    override fun toString(): String {
        val x = Constants.GENERATOR_GENERAL
        val y = Constants.GENERATOR_LIST
        return "plant$x$lastTimeCheck$x${compensation.joinToString("") { "$it$y" }}$x"
    }


    fun getOutput(level : Int) : MutableList<Int>{
        val list = mutableListOf<Int>()
        val comp = mutableListOf<Long>()
        for((idx,m) in mt.production.withIndex()) {
            if((m.metaData as FarmMetaData).level <= level) {
                val output =
                    (System.currentTimeMillis() - lastTimeCheck + compensation[idx]).toFloat() / mt.time[idx].toFloat()
                val totalOutput = floor(output).roundToInt()

                comp.add(((output - totalOutput) * mt.time[idx]).toLong())
                list.add(kotlin.math.min(5, totalOutput))
            }else{
                comp.add(0)
            }
        }
        this.lastTimeCheck = System.currentTimeMillis()
        this.compensation = comp
        return list
    }

}