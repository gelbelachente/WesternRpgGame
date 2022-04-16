package de.gelbelachente.desertrpggame.game.MetaData

import de.gelbelachente.desertrpggame.game.DataObjects.Material
import de.gelbelachente.desertrpggame.game.DataObjects.Type
import de.gelbelachente.desertrpggame.game.DataObjects.UniverseType


data class FacilityMetaData(val production : MutableList<Material>, val time : MutableList<Long>)
    : MetaData(Type.building){

    fun getUniverseType(type : Material) : UniverseType {
        return when(type){
            Material.LumberYard -> UniverseType.Lumberyard
            Material.Pit -> UniverseType.Mine
            else -> UniverseType.Open
        }
    }
}



