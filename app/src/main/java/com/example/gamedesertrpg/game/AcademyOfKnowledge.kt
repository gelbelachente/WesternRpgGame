package de.gelbelachente.desertrpggame.game

import com.example.gamedesertrpg.game.DataObjects.Skill
import de.gelbelachente.desertrpggame.game.DataObjects.*
import de.gelbelachente.desertrpggame.game.animator.Animation

class AcademyOfKnowledge {
    companion object{


        fun getRecipes(skills : MutableList<Skill>) : MutableList<Recipe>{
            return recipes
            return recipes.filter {rep ->  Skill.values().filterNot { it in skills  }.any { rep.product in it.items }}.toMutableList()
        }

        val recipes = mutableListOf<Recipe>().apply {
            add(Recipe(Material.Wood,1, mutableListOf(Material.Bush), mutableListOf(1)))
            add(Recipe(Material.Wood,3, mutableListOf(Material.CactusMiddle), mutableListOf(1)))
            add(Recipe(Material.Wood,2, mutableListOf(Material.CactusRound), mutableListOf(1)))
            add(Recipe(Material.Wood,2, mutableListOf(Material.CactusSmall), mutableListOf(1)))
            add(Recipe(Material.Stone,1, mutableListOf(Material.StoneOld), mutableListOf(1)))
            add(Recipe(Material.StairsToRight,1, mutableListOf(Material.Stone), mutableListOf(3)))
            add(Recipe(Material.BoatBroken,1, mutableListOf(Material.Wood), mutableListOf(5)))
            add(
                Recipe(Material.Pyramid,1, mutableListOf(Material.PyramidSmall,Material.Quader),
                mutableListOf(2,3))
            )
            add(Recipe(Material.Quader,1, mutableListOf(Material.Stone), mutableListOf(3)))
            add(Recipe(Material.PyramidSmall,1, mutableListOf(Material.Stone), mutableListOf(2)))
            add(Recipe(Material.Hammer,1, mutableListOf(Material.Wood,Material.Stone), mutableListOf(3,2)))
            add(
                Recipe(Material.Tend,1, mutableListOf(Material.Wood,Material.MineralIron),
                mutableListOf(6,1))
            )
            add(
                Recipe(Material.LumberYard,1, mutableListOf(Material.Wood,Material.MineralIron,
                        Material.Stone), mutableListOf(4,6,6))
            )
            add(Recipe(Material.Pit,1, mutableListOf(Material.Wood,Material.MineralIron,Material.Stone), mutableListOf(4,6,10)))
            add(
                Recipe(Material.Sword,1, mutableListOf(Material.Wood,Material.MineralIron,Material.MineralAventurine),
                mutableListOf(4,6,2))
            )

            add(Recipe(Material.LightShieldSharp,1, mutableListOf(Material.Wood),
                mutableListOf(4)))
            add(Recipe(Material.MediumShieldSharp,1, mutableListOf(Material.Wood,Material.MineralIron),
                mutableListOf(4,2)))
            add(Recipe(Material.HardShieldSharp,1, mutableListOf(Material.Wood,Material.MineralIron),
                mutableListOf(4,6)))

            add(Recipe(Material.LightShear,1, mutableListOf(Material.Wood,Material.MineralIron), mutableListOf(3,5)))
            add(Recipe(Material.MediumShear,1, mutableListOf(Material.Wood,Material.MineralIron), mutableListOf(5,8)))

        }

        val trades = mutableListOf<Trade>().apply {
            add(
                Trade(mutableListOf(Material.MineralGold), mutableListOf(1), mutableListOf(Material.Stone),
                mutableListOf(3))
            )
            add(
                Trade(mutableListOf(Material.MineralIron), mutableListOf(1), mutableListOf(Material.Stone),
                mutableListOf(2))
            )
            add(
                Trade(mutableListOf(Material.Hammer), mutableListOf(1), mutableListOf(Material.Wood,Material.Stone),
                mutableListOf(2,1))
            )
            add(
                Trade(mutableListOf(Material.Tend), mutableListOf(1), mutableListOf(Material.Stone,Material.MineralGold),
                mutableListOf(2,1))
            )
        }

        val questions = mutableListOf<Question>().apply {
            add(Question("Wild Frontier is the best server ever created.",true))
            add(Question("The owner of Wild Frontier is Ovron.",false))
            add(Question("Wild Frontier is a western style rp server.",true))
        }

        val animations = mutableMapOf<NPC, Animation>().apply {
            put(NPC.Golem, Animation(golem_walk,golem_attack,null,null,null,null))
            put(NPC.Dragon, Animation(dragon_walk, dragon_attack,null,null,null,null))
        }

    }
}


