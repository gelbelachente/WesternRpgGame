package de.gelbelachente.desertrpggame.game.MetaData

import de.gelbelachente.desertrpggame.game.Constants
import java.lang.Math.min
import kotlin.math.floor
import kotlin.math.roundToInt

class FacilityGenerator(val fac : FacilityMetaData, var lastTimeCheck : Long = System.currentTimeMillis()
                        , var compensation : MutableList<Long> = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)) : Generator() {

    override fun toString(): String {
        val x = Constants.GENERATOR_GENERAL
        val y = Constants.GENERATOR_LIST
        return "facility$x$lastTimeCheck$x${compensation.joinToString("") { "$it$y" }}$x"
    }


    fun getOutput() : MutableList<Int>{
        val list = mutableListOf<Int>()
        val comp = mutableListOf<Long>()
        for((idx,m) in fac.production.withIndex()) {
            val output =
                (System.currentTimeMillis() - lastTimeCheck + compensation[idx]).toFloat() / fac.time[idx].toFloat()
            val totalOutput = floor(output).roundToInt()

            comp.add(((output - totalOutput) * fac.time[idx]).toLong())
            list.add(kotlin.math.min(10, totalOutput))
        }
        this.lastTimeCheck = System.currentTimeMillis()
        this.compensation = comp
        return list
    }


}