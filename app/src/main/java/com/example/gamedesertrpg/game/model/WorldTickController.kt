package de.gelbelachente.desertrpggame.game.model

import android.app.Activity
import com.example.gamedesertrpg.R
import java.util.*


enum class Weather(val color : Int? = null){
    Fog(R.color.gray),
    Sun();

    fun change() : Weather {
        var index = Random().nextInt(Weather.values().size)
        return Weather.values()[index]
    }
}

enum class DayTime(){
    Day(),
    Night();

    fun change() : DayTime {
       return if(this == Day){
                Night
                }else{
                Day
                }
    }
}

class WorldTickController {


   var observer : MutableList<() -> (Unit)> = mutableListOf()
   var weatherObserver : MutableList<(Weather) -> (Unit)> = mutableListOf()
   var dayObserver : MutableList<(DayTime) -> (Unit)> = mutableListOf()

     var weatherChange = 0
     var dayTime = 0
     var weatherBorder = 1000
     var dayLength = 10000
     var time  = DayTime.Day
     var weather = Weather.Sun


    fun infiniteLoop(ctx : Activity){

        while (true){
            weatherChange++
            dayTime++

            if(dayTime >= dayLength){
                time = time.change()
                dayTime = 0
                for(o in dayObserver){
                    o(time)
                }
            }
            if(weatherChange >= weatherChange && Random().nextInt(400) <= 25){
                weather = weather.change()
                weatherChange = 0
                for(o in weatherObserver){
                        //o(weather)
                }
            }


            Thread.sleep(80)
            for(o in observer){
                o()
            }

        }
    }



    companion object{

        private val instance = WorldTickController()
        fun get() : WorldTickController {
            return instance
        }

    }


}