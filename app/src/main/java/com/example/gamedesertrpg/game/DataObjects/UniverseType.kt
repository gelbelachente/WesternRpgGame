package de.gelbelachente.desertrpggame.game.DataObjects

enum class UniverseType(val allowed : MutableMap<Material,Int>, val npc : MutableList<NPC>, val alpha : Float = 1f, val dark: Boolean = false) {
    Open(mutableMapOf<Material,Int>().apply {
        for(m in Material.values()){
            put(m,m.rareSpawning)
        }
    }, mutableListOf(NPC.Golem,NPC.Dragon)),
    Lumberyard(
        mutableMapOf(
            Material.LandBlank to 1, Material.Wood to 2, Material.CactusSmall to 2,
            Material.CactusRound to 2, Material.CactusMiddle to 2),
         mutableListOf()),
    Mine(
        mutableMapOf(
            Material.LandBlank to 1,
            Material.MineralIron to 3,
            Material.Stone to 2,
            Material.MineralGold to 6),
        mutableListOf(NPC.Golem,NPC.Dragon),.7f,true);
}
