package de.gelbelachente.desertrpggame.game.DataObjects

import android.graphics.Bitmap
import com.example.gamedesertrpg.R
import com.example.gamedesertrpg.game.MetaData.DefenseMetaData
import com.example.gamedesertrpg.game.MetaData.FarmMetaData
import com.example.gamedesertrpg.game.MetaData.PlantMetaData
import de.gelbelachente.desertrpggame.game.MetaData.FacilityMetaData
import de.gelbelachente.desertrpggame.game.MetaData.FoodMetaData
import de.gelbelachente.desertrpggame.game.MetaData.MetaData
import de.gelbelachente.desertrpggame.game.MetaData.WeaponMetaData

enum class Material(val drawable : Int, val h : Float, val w : Float, val type: Type, val rareSpawning : Int = 1,
                    val int : Interaction? = null, val metaData: MetaData? = null){
    LandBlank(R.drawable.land_6,1f,1f, Type.tile),
    Skull(R.drawable.decor_6,.2f,.2f, Type.deco),
    Background(R.drawable.bg,1f,1f, Type.tile),
    YellowBlossom(R.drawable.alch_0,.2f,.2f,Type.deco,1000,null,FarmMetaData(1)),
    BlueBlossom(R.drawable.alch_1,.2f,.2f,Type.deco,1000,null,FarmMetaData(1)),
    BlueLeaf(R.drawable.alch_4,.2f,.2f,Type.deco,1000,null,FarmMetaData(1)),
    BigRoot(R.drawable.alch_7,.2f,.2f,Type.deco,1000,null,FarmMetaData(1)),
    CactusSmall(R.drawable.greenery_2,.4f,.4f, Type.deco),
    Bush(R.drawable.greenery_10,.2f,.2f, Type.deco),
    CactusRound(R.drawable.greenery_7,.3f,.3f, Type.deco,1,null,PlantMetaData(
        mutableListOf(YellowBlossom,BlueBlossom,BlueLeaf,BigRoot), mutableListOf(500000,500000,500000,500000)
    )),
    CactusMiddle(R.drawable.greenery_4,.5f,.5f, Type.deco),
    Stone(R.drawable.stones_1,.3f,.3f, Type.deco),
    StoneOld(R.drawable.stones_5,.3f,.3f, Type.deco),
    Minotaur(R.drawable.minotaur_01_idle_000,.8f,1f, Type.player),
    Wood(R.drawable.wood,.15f,.3f, Type.deco),
    BoatBroken(R.drawable.decor_5,.75f,1.5f, Type.deco,15),
    MineralGold(R.drawable.icon27,.2f,.2f, Type.deco,10),
    MineralIron(R.drawable.icon4,.2f,.2f, Type.deco,5),
    MineralAventurine(R.drawable.icon_aventurine,.2f,.2f, Type.deco,10),
    Ham(R.drawable.meat,.3f,.3f, Type.deco,15, Interaction.Eat, FoodMetaData(15)),
    Skin(R.drawable.skin,.3f,.3f,Type.deco,100),
    Bone(R.drawable.bone,.3f,.3f,Type.deco,100),
    Pyramid(R.drawable.decor_1,.5f,.75f, Type.building),
    PyramidSmall(R.drawable.stones_2,.3f,.3f, Type.building),
    Quader(R.drawable.stones_3,.3f,.3f, Type.building),
    Tend(R.drawable.decor_2,.8f,.6f, Type.building,999, Interaction.Sleep),
    LumberYard(R.drawable.wooden_house,.6f,.6f, Type.building,19,null,
        FacilityMetaData(mutableListOf(Wood), mutableListOf(1200000))
    ),
    Pit(R.drawable.mine,.6f,.6f, Type.building,999,null,
        FacilityMetaData(mutableListOf(Stone, MineralIron, MineralGold), mutableListOf(1200000,6000000,9000000))
    ),
    Hammer(R.drawable.hammer,.3f,.3f, Type.Weapon,1000,null, WeaponMetaData(8)),
    Sword(R.drawable.sword,.3f,.3f, Type.Weapon,1000,null, WeaponMetaData(12)),
    LightShear(R.drawable.daggers__6_,.3f,.3f,Type.deco,1000,null,FarmMetaData(1)),
    MediumShear(R.drawable.daggers__7_,.3f,.3f,Type.deco,1000,null,FarmMetaData(2)),
    LightShieldSharp(R.drawable.shield_wood,.5f,.3f,Type.Weapon,1000,null,DefenseMetaData(.2f)),
    MediumShieldSharp(R.drawable.shield_8_3,.6f,.3f,Type.Weapon,2000,null,DefenseMetaData(.3f)),
    HardShieldSharp(R.drawable.shield_8_5,.6f,.3f,Type.Weapon,4000,null,DefenseMetaData(.5f)),
    DarkElveCivilian(R.drawable.character3_face1,.8f,.3f, Type.NPC,80, Interaction.Trade),
    DarkElveLibrarian(R.drawable.character2_face1,.8f,.3f, Type.NPC,80, Interaction.Question),
    StairsToRight(R.drawable.land_8,1f,1f, Type.building);
    companion object {
        val bitmaps: MutableMap<Material, Bitmap> = mutableMapOf<Material, Bitmap>()
    }

}