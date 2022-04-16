package de.gelbelachente.desertrpggame.game.DataObjects

import android.graphics.Bitmap
import com.example.gamedesertrpg.R

enum class NPC(val drawable: Int, val h: Float, val w: Float, val spawning : Int = 5, val behaviour: Behaviour, val damage : Int,
               val speed : Int, val drops : MutableMap<Material,Int>, val live : Int){
    Golem(
        R.drawable.golem_01_idle_000,.8f,.6f,20, Behaviour.Agressive,5,
        1, mutableMapOf<Material, Int>().apply {
            put(Material.MineralIron,3); put(Material.Ham,2); put(Material.Stone,2)},30),
    Dragon(R.drawable.idle1,1f,2f,50,Behaviour.Neutral,3,2,
        mutableMapOf<Material, Int>().apply { put(Material.MineralGold,4);
        put(Material.MineralAventurine,5); put(Material.Ham,6); put(Material.Skin,6)},50);



    companion object {
        val bitmaps: MutableMap<NPC, Bitmap> = mutableMapOf<NPC, Bitmap>()
    }
}