package com.example.gamedesertrpg.game.DataObjects

import de.gelbelachente.desertrpggame.game.DataObjects.Material

enum class Skill(val cost : List<Material>,val costAmount : List<Int>,
                 val items : List<Material> = listOf(),val level : Int) {

    Tanner(listOf(Material.MineralIron,Material.Skin,Material.MineralAventurine), listOf(8,4,8),
        listOf(),1),
    Alchemist(listOf(Material.Wood,Material.MineralIron,Material.MineralGold), listOf(12,9,6),
        listOf(),3),
    Herbalist(listOf(Material.Wood,Material.MineralIron), listOf(12,8), listOf(Material.LightShear,Material.MediumShear),2);

}