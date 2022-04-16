package com.example.gamedesertrpg
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.gelbelachente.desertrpggame.Data.World
import de.gelbelachente.desertrpggame.Data.WorldApplication
import de.gelbelachente.desertrpggame.Data.worldDao
import de.gelbelachente.desertrpggame.game.model.SettingsManager
import kotlinx.coroutines.flow.Flow

class SharedStateHolderViewModel {


     val loadingProgress : MutableLiveData<Int> = MutableLiveData<Int>(0)
     val progress : LiveData<Int> get() = loadingProgress

     lateinit var worldDao: worldDao
    private lateinit var worlds : Flow<List<World>>

     fun load(application: WorldApplication){
         loadingProgress.postValue(0)
         worldDao = application.db!!.worldDao()
         SettingsManager.inject(worldDao)
         worlds = worldDao.getWorlds()
        for(i in 0 until 11){
            Thread.sleep(200)
            loadingProgress.postValue(loadingProgress.value?.plus(10))
        }
    }

    fun getWorlds() : Flow<List<World>>{
        return this.worlds
    }

    fun exists(name : String) : Boolean{
        return worldDao.getWorldsAsPrimitive().any{it.name == name}
    }
    fun getWorld(name : String) : World{
        return worldDao.getWorld(name)
    }



    companion object{

        private val instance = SharedStateHolderViewModel()

        fun get() : SharedStateHolderViewModel {
            return instance
        }
    }


}