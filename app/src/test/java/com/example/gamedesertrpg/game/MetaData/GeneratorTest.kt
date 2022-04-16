package com.example.gamedesertrpg.game.MetaData

import de.gelbelachente.desertrpggame.game.DataObjects.Material
import de.gelbelachente.desertrpggame.game.MetaData.FacilityGenerator
import de.gelbelachente.desertrpggame.game.MetaData.FacilityMetaData
import de.gelbelachente.desertrpggame.game.MetaData.Generator
import org.junit.Assert.*
import org.junit.Test

class GeneratorTest{

    @Test
    fun testToString(){

        val gen = FacilityGenerator(FacilityMetaData(mutableListOf(Material.DarkElveCivilian,Material.BoatBroken),
        mutableListOf(3000,9000)))
        gen.lastTimeCheck = System.currentTimeMillis()-20000
        val out = gen.getOutput()

        assert(out[0] == 6)
        assert(out[1] == 2)
        val str = gen.toString()
        val g = Generator.fromString(str,gen.fac) as FacilityGenerator
        assertEquals(g.fac,gen.fac)
        assertEquals(g.lastTimeCheck,gen.lastTimeCheck)
        assertEquals(g.compensation,gen.compensation)

    }


}