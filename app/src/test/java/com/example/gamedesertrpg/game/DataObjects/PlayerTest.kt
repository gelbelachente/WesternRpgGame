package com.example.gamedesertrpg.game.DataObjects

import de.gelbelachente.desertrpggame.game.DataObjects.Material
import de.gelbelachente.desertrpggame.game.DataObjects.Player
import org.junit.Assert.*

import org.junit.Test

class PlayerTest {

    @Test
    fun testToString() {
        val p = Player("Main", mutableMapOf("Main" to mutableListOf(-3.5f,-5.6f)),109, Material.BoatBroken,
        mutableMapOf(), mutableListOf(-3.5f,-8.8f), mutableMapOf(Material.Pit to 3, Material.Bush to 6))
        val str = p.toString()
        val pc = Player.fromString(str)
        assertEquals(p.inventory,pc.inventory)
        assertEquals(p.currentUniverse,pc.currentUniverse)
        assertEquals(p.lastSleepingPoint,pc.lastSleepingPoint)
        assertEquals(p.live,pc.live)
        assertEquals(p.offHand,pc.offHand)
        assertEquals(p.playerPosAll,pc.playerPosAll)
        assertEquals(p.trades,pc.trades)
    }
}