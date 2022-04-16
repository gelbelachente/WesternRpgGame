package com.example.gamedesertrpg.game.DataObjects

import de.gelbelachente.desertrpggame.game.DataObjects.Entity
import de.gelbelachente.desertrpggame.game.DataObjects.Material
import de.gelbelachente.desertrpggame.game.MetaData.FacilityGenerator
import de.gelbelachente.desertrpggame.game.MetaData.FacilityMetaData
import de.gelbelachente.desertrpggame.game.getId
import org.junit.Assert.*

import org.junit.Test

class EntityTest {


    //test to string and back convertion
    @Test
    fun testToString() {
        val e = Entity(Material.Pit,0f,0f,-3.5f,-5.7f,0,0,
            getId(),FacilityGenerator(Material.Pit.metaData!! as FacilityMetaData))

        (e.gen as FacilityGenerator).getOutput()
        val str = e.toString()
        print(str)
        assertEquals(e.type,Entity.fromString(str).type)
        assertEquals((e.gen as FacilityGenerator).fac.production,(Entity.fromString(str).gen as FacilityGenerator).fac.production)
        assertEquals((e.gen as FacilityGenerator).compensation,(Entity.fromString(str).gen as FacilityGenerator).compensation)
        assertEquals((e.gen as FacilityGenerator).lastTimeCheck,(Entity.fromString(str).gen as FacilityGenerator).lastTimeCheck)
        assertEquals(e.posRealX,Entity.fromString(str).posRealX)
        assertEquals(e.posRealY,Entity.fromString(str).posRealY)
        assertEquals(e.id,Entity.fromString(str).id)

    }


}