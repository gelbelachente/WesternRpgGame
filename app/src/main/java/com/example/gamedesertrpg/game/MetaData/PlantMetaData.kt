package com.example.gamedesertrpg.game.MetaData

import de.gelbelachente.desertrpggame.game.DataObjects.Material
import de.gelbelachente.desertrpggame.game.DataObjects.Type
import de.gelbelachente.desertrpggame.game.MetaData.MetaData

class PlantMetaData(val production : MutableList<Material>, val time : MutableList<Long>) : MetaData(Type.deco) {



}