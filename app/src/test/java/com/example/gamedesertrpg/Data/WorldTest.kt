package com.example.gamedesertrpg.Data

import de.gelbelachente.desertrpggame.Data.World
import org.junit.Assert.*

import org.junit.Test

class WorldTest {

    @Test
    fun testToString() {
        val w = World("sdfsdf")
        val wc = World.fromString(w.toString())
        assertEquals(w,wc)

    }
}