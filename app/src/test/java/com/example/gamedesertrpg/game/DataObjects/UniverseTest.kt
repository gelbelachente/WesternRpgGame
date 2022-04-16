package com.example.gamedesertrpg.game.DataObjects

import de.gelbelachente.desertrpggame.game.DataObjects.Universe
import de.gelbelachente.desertrpggame.game.DataObjects.UniverseType
import org.junit.Assert.*

import org.junit.Test

class UniverseTest {

    @Test
    fun testToString() {
    val uni = Universe("Main", mutableListOf(), mutableListOf(),UniverseType.Lumberyard)
    val uc = Universe.fromString(uni.toString())
    assertEquals(uni.name,uc.name)
    assertEquals(uni.type,uc.type)
    }
}