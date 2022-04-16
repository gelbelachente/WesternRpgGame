package com.example.gamedesertrpg.game.DataObjects

import de.gelbelachente.desertrpggame.game.DataObjects.Material
import de.gelbelachente.desertrpggame.game.DataObjects.Trade
import org.junit.Assert.*

import org.junit.Test

class TradeTest {

    @Test
    fun testToString() {
        val trade = Trade(mutableListOf(Material.Bush,Material.Pit), mutableListOf(3,4)
        , mutableListOf(Material.BoatBroken,Material.Bush), mutableListOf(5,6))
        val str = trade.toString()
        val tc = Trade.fromString(str)
        assertEquals(trade,tc)
    }
}