package de.gelbelachente.desertrpggame.game.DataObjects

import de.gelbelachente.desertrpggame.game.Constants
import java.util.*

data class Trade(val offer : MutableList<Material>, val offerAmount : MutableList<Int>, val ingredients : MutableList<Material>,
                 val ingredientAmount : MutableList<Int>){

    override fun toString(): String {
        val x = Constants.TRADE_GENERAL
        val y = Constants.TRADE_LIST
        return "${offer.joinToString("") {"$it$y"}}$x${offerAmount.joinToString("") {"$it$y"}}$x${ingredients.joinToString("") {"$it$y"}}"+
        "$x${ingredientAmount.joinToString("") {"$it$y"}}$x"
    }

    companion object{
        fun fromString(str : String) : Trade {
            val x = Constants.TRADE_GENERAL
            val y = Constants.TRADE_LIST
            val tok = StringTokenizer(str,x)
            val offer = tok.nextToken().replace("[","").replace("]","")
                .split(y).filter { it != "" }.map { Material.valueOf(it) }.toMutableList()
            val offerAmount = tok.nextToken().replace("[","").replace("]","")
                .split(y).filter { it != "" }.map { it.toInt() }.toMutableList()
            val ingredients = tok.nextToken().replace("[","").replace("]","")
                .split(y).filter { it != "" }.map { Material.valueOf(it) }.toMutableList()
            val ingredientsAmount = tok.nextToken().replace("[","").replace("]","")
                .split(y).filter { it != "" }.map { it.toInt() }.toMutableList()
            return Trade(offer,offerAmount,ingredients,ingredientsAmount)
        }
    }

}