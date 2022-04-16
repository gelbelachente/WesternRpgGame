package de.gelbelachente.desertrpggame.game.model

import kotlin.math.floor
import kotlin.math.min

class ComputerModel() {

    @Volatile
    var mapSize = mutableListOf<Float>(0f, 0f)

    @Volatile
    var tileSize = 1f

    fun computeConstants(width: Float, height: Float) {
        val tileSize = min(floor(width / 2f), height) / 3
        mapSize = mutableListOf(width, height)
        this.tileSize = tileSize
    }


}