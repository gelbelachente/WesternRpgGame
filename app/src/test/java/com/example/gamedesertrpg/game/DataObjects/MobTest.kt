package com.example.gamedesertrpg.game.DataObjects

import de.gelbelachente.desertrpggame.game.DataObjects.Action
import de.gelbelachente.desertrpggame.game.DataObjects.Mob
import de.gelbelachente.desertrpggame.game.DataObjects.NPC
import de.gelbelachente.desertrpggame.game.getId
import org.junit.Assert.*

import org.junit.Test

class MobTest {

    @Test
    fun testToString() {
        val mob = Mob(NPC.Dragon,0f,0f,-5.7f,-89.6f,0,0,400, getId(),
            true,1,900,true,Action.MoveLeft)

        val str = mob.toString()
        val mc = Mob.fromString(str)
        assertEquals(mob.attacked,mc.attacked)
        assertEquals(mob.posRealX,mc.posRealX)
        assertEquals(mob.posRealY,mc.posRealY)
        assertEquals(mob.id,mc.id)
        assertEquals(mob.type,mc.type)

    }

}