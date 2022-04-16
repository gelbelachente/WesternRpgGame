package de.gelbelachente.desertrpggame.game.MetaData

import com.example.gamedesertrpg.game.MetaData.PlantGenerator
import com.example.gamedesertrpg.game.MetaData.PlantMetaData
import de.gelbelachente.desertrpggame.game.Constants
import java.util.*

open class Generator {


    companion object{

        fun fromString(str: String, mt: MetaData?) : Generator?{
            val x = Constants.GENERATOR_GENERAL
            val y = Constants.GENERATOR_LIST
            val tok = StringTokenizer(str,x)
            val type = tok.nextToken()
            println(str + mt.toString())
            return when(type){
                 "facility" -> {
                     val lastTimeChecked = tok.nextToken().toLong()
                     val compensations = tok.nextToken().split(y).filter { it != "" }.map { it.toLong() }.toMutableList()
                     FacilityGenerator(mt as FacilityMetaData,lastTimeChecked,compensations)
                 }
                "plant" -> {
                    val lastTimeChecked = tok.nextToken().toLong()
                    val compensations = tok.nextToken().split(y).filter { it != "" }.map { it.toLong() }.toMutableList()
                    PlantGenerator(mt as PlantMetaData,lastTimeChecked,compensations)
                }
                else -> null
            }
        }

        fun get(mt : MetaData?) : Generator?{
            if(mt != null && mt is FacilityMetaData){
                return FacilityGenerator(mt)
            }
            if(mt != null && mt is PlantMetaData){
                return PlantGenerator(mt)
            }
            return null
        }

    }
}