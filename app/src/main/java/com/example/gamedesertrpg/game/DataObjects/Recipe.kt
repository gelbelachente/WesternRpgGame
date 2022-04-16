package de.gelbelachente.desertrpggame.game.DataObjects

data class Recipe(val product : Material, val amount : Int, val ingredients : MutableList<Material>, val ingredientAmount : MutableList<Int>)

