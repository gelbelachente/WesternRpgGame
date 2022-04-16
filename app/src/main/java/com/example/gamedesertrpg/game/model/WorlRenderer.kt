package de.gelbelachente.desertrpggame.game.model

import de.gelbelachente.desertrpggame.game.DataObjects.Entity
import de.gelbelachente.desertrpggame.game.DataObjects.Material
import de.gelbelachente.desertrpggame.game.DataObjects.Type
import de.gelbelachente.desertrpggame.game.DataObjects.UniverseType
import de.gelbelachente.desertrpggame.game.MetaData.Generator
import de.gelbelachente.desertrpggame.game.getId
import java.util.*
import kotlin.math.floor

class WorlRenderer {


    companion object{


        fun checkIfMissing(tiles : MutableList<Entity>, playerPos : MutableList<Float>, width : Int, height : Int) : MutableList<MutableList<Int>>{
            var missing = mutableListOf<MutableList<Int>>()
            for(idx in (floor(playerPos[0])-width).toInt() until (floor(playerPos[0]+1) + width).toInt() ){
                for(idy in (floor(playerPos[1])-height).toInt() until (floor(playerPos[1]+1) + height).toInt() ){
                    if(tiles.none { it.posRealX.toInt() == idx && it.posRealY.toInt() == idy }){
                        missing.add(mutableListOf(idx,idy))
                    }
                }
            }
            return missing
        }

        fun fillMissing(missing : MutableList<MutableList<Int>>,universeType: UniverseType) : MutableList<Entity>{
            val filled = mutableListOf<Entity>()
            for(missingTile in missing){
                filled.add(Entity(getTile(universeType),0f,0f,missingTile[0].toFloat(),missingTile[1].toFloat()))
                val i = Random().nextInt(10).toFloat()/10f-.5f
                val i2 = Random().nextInt(10).toFloat()/10f-.5f
                val type = getDeco(universeType)
                filled.add(
                    Entity(
                        type,0f,0f,missingTile[0].toFloat()+i,missingTile[1].toFloat()+i2,
                        0,0, getId(), Generator.get(type.metaData)
                    ))
                val npc = getNPC(universeType)
                if(npc != null){
                    filled.add(Entity(npc,0f,0f,missingTile[0].toFloat()+i,missingTile[1].toFloat()+i2))
                }
            }
            return filled
        }


         fun getNPC(universeType: UniverseType) : Material?{
            var list = Material.values().filter { it.type == Type.NPC && universeType.allowed.contains(it)}
            for(npc in list){
                if(Random().nextInt(universeType.allowed[npc]!!) <= 1){
                    return npc
                }
            }
             return null
         }

         fun getDeco(universeType: UniverseType) : Material {
            val r = Material.values().filter { it.type == Type.deco && universeType.allowed.contains(it) }.random()
                if(Random().nextInt(universeType.allowed[r]!!) <= 1){
                    return r
                }else{
                    return getDeco(universeType)
                }
        }

        private fun getTile(universeType: UniverseType) : Material {
            return Material.values().filter { it.type == Type.tile && universeType.allowed.contains(it)}.random()
        }

    }


}