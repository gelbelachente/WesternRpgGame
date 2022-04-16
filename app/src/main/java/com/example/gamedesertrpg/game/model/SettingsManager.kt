package de.gelbelachente.desertrpggame.game.model

import de.gelbelachente.desertrpggame.Data.Setting
import de.gelbelachente.desertrpggame.Data.worldDao

class SettingsManager(val worldDao: worldDao) {


    private val list = mutableMapOf<String,Int>(
        "sound" to 1
    )

    fun update(){
        worldDao.getSettings().forEach {
                list[it.key] = it.value.toInt()
        }
    }

    fun get(key : String) : Int?{
        return list[key]
    }

    fun set(key : String,value : Int){
            worldDao.setSetting(Setting(key,value.toString()))
        list[key] = value
    }

    fun toggle(key : String) : Int?{
        if(list[key] == 0){
            list[key] = 1
        }else{
            list[key] = 0
        }
        worldDao.setSetting(Setting(key,list[key].toString()))
        return list[key]
    }



    companion object{

        private lateinit var instance : SettingsManager

        fun inject(worldDao: worldDao){
            synchronized(this){
                instance = SettingsManager(worldDao)
                instance.update()
            }
        }

        fun get() : SettingsManager {
            return instance
        }


    }

}