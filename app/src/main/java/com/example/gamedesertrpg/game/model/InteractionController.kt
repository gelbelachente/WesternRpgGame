package de.gelbelachente.desertrpggame.game.model

import android.util.Log
import com.example.gamedesertrpg.game.MetaData.FarmMetaData
import com.example.gamedesertrpg.game.MetaData.PlantGenerator
import com.example.gamedesertrpg.game.MetaData.PlantMetaData
import de.gelbelachente.desertrpggame.game.*
import de.gelbelachente.desertrpggame.game.DataObjects.*
import de.gelbelachente.desertrpggame.game.MetaData.FacilityGenerator
import de.gelbelachente.desertrpggame.game.MetaData.FacilityMetaData
import de.gelbelachente.desertrpggame.game.MetaData.FoodMetaData
import de.gelbelachente.desertrpggame.game.MetaData.Generator

class InteractionController {

    companion object {
        private val model = GameModel.get()
        private fun xRange(range: Float) =
            (model.player.playerPos[0] - range)..(model.player.playerPos[0] + range)

        private fun yRange(range: Float) =
            (model.player.playerPos[1] - range)..(model.player.playerPos[1] + range)


        fun startInteraction(
            func: (Trade, Boolean) -> (Unit),
            slept: (Boolean) -> (Unit),
            question: (Question, Material) -> (Unit),
            toast : (String) -> (Unit),
            eat : () -> (Unit)
        ) : Boolean {
            var list =
                model.drawables.filter { it.posRealX in xRange(.3f) && it.posRealY in yRange(.3f) }

            if(model.player.offHand != null && model.player.offHand!!.int == Interaction.Eat){
                model.player.live += (model.player.offHand!!.metaData as FoodMetaData).reg
                if(model.player.live > 150){
                    model.player.live = 150
                }
                model.player.inventory[model.player.offHand!!] = model.player.inventory[model.player.offHand!!]!! - 1
                if(model.player.inventory[model.player.offHand!!]!! <= 0){
                    model.player.inventory[model.player.offHand!!] = 0
                }
                eat()
                return true
            }

            //trade
            val listTrade = list.filter { it.type.int == Interaction.Trade }.toMutableList()
            if (listTrade.isNotEmpty()) {
                var trade: Trade
                if (model.player.trades.keys.any{listTrade[0].equals(it)}) {
                    trade = model.player.trades[model.player.trades.keys.find{listTrade[0].equals(it)}]!!
                } else {
                    trade = AcademyOfKnowledge.trades.random()
                    model.player.trades[listTrade[0]] = trade
                }
                var doable = true
                for ((idx, ing) in trade.ingredients.withIndex()) {
                    doable =
                        doable && (model.player.inventory.contains(ing) && model.player.inventory[ing]!! >= trade.ingredientAmount[idx])
                }
                func(trade, doable)
                return true
            }

            //sleep
            var listSleep = list.filter { it.type.int != null && it.type.int == Interaction.Sleep }
            if (listSleep.isNotEmpty()) {
                val controller = WorldTickController.get()
                val nec = controller.time != DayTime.Day
                model.player.lastSleepingPoint = model.player.playerPos.toMutableList()
                if (nec) {
                    controller.dayTime = WorldTickController.get().dayLength
                }
                slept(nec)
                return true
            }

            //question & answer
            var listQuestion =
                list.filter { it.type.int != null && it.type.int == Interaction.Question }
            if (listQuestion.isNotEmpty()) {
                val gain = Material.values().filter { it.type in mutableListOf(
                    Type.deco,
                    Type.Weapon
                ) }
                question(
                    AcademyOfKnowledge.questions.random(),
                    gain.random()
                )
                return true
            }

            //collect output plants
            var listPlant = list.filter { it.type.metaData != null && it.type.metaData is PlantMetaData }
            if(listPlant.isNotEmpty()&& model.player.offHand != null && model.player.offHand!!.metaData is FarmMetaData){
                val facility = listPlant[0]
                val md = (facility.gen as PlantGenerator)
                val output = md.getOutput((model.player.offHand!!.metaData as FarmMetaData).level)
                for((idx,op) in output.withIndex()) {
                    model.player.inventory[md.mt.production[idx]] = model.player.inventory[md.mt.production[idx]]!! + op
                }
                toast("You farmed ${output.mapIndexed{idx,it -> "$it ${md.mt.production[idx]}"}.toString()}")
                return true
            }

            //collect output fac
            var listFacilities = list.filter { it.type.metaData != null && it.type.metaData is FacilityMetaData }
            if(listFacilities.isNotEmpty()){
                val facility = listFacilities[0]
                val md = (facility.gen as FacilityGenerator)
                val output = md.getOutput()
                for((idx,op) in output.withIndex()) {
                    model.player.inventory[md.fac.production[idx]] = model.player.inventory[md.fac.production[idx]]!! + op
                }
                    toast("You received ${output.mapIndexed{idx,it -> "$it ${md.fac.production[idx]}"}.toString()}")
                return true
            }



            return false
        }


        fun qaReward(material: Material, won: Boolean = true) {
            val inventory = model.player.inventory
            val playerPos = model.player.playerPos
            val trades = model.player.trades
            if (won) {
                inventory[material] = inventory[material]!! + 1
            }
            var list =
                model.drawables.filter { it.posRealX in xRange(.4f) && it.posRealY in yRange(.4f) }
                    .filter { it.type.int != null && it.type.int == Interaction.Question }
            if (list.isNotEmpty()) {
                model.drawables.remove(list[0])
            }
        }


        fun trade(trade: Trade) {
            val inventory = model.player.inventory
            val playerPos = model.player.playerPos
            val trades = model.player.trades

            var list =
                model.drawables.filter { it.posRealX in xRange(.5f) && it.posRealY in yRange(.5f) }
                    .filter { it.type.type == Type.NPC }.toMutableList()
            for ((idx, ing) in trade.ingredients.withIndex()) {
                inventory[ing] = inventory[ing]!! - trade.ingredientAmount[idx]
            }
            for ((idx, ing) in trade.offer.withIndex()) {
                inventory[ing] = inventory[ing]!! + trade.offerAmount[idx]
            }
            model.player.trades.remove(list[0])
            model.drawables.remove(list[0])
        }

        fun destroy() {
            var list =
                model.drawables.filter { it.posRealX in xRange(.5f) && it.posRealY in yRange(.5f) }
                    .filter { it.type.type == Type.deco || it.type.type == Type.building || it.type.type == Type.Weapon }
                    .toMutableList()
            if (model.player.offHand == null || model.player.offHand!!.type != Type.Weapon) {
                list = list.filter { it.type.type != Type.building }.toMutableList()
            }
            if (list.isNotEmpty()) {
                model.drawables.removeIf { it.id == list[0].id}
                if(!model.player.inventory.keys.contains(list[0].type)){
                    model.player.inventory[list[0].type] = 0
                }
                model.player.inventory[list[0].type] = model.player.inventory[list[0].type]!! + 1
            }
        }

        fun build(m: Material, fee: Boolean = true): Boolean {
            val inventory = model.player.inventory
            val playerPos = model.player.playerPos
            val trades = model.player.trades
            val tileSize = model.tileSize
            model.drawables.add(
                Entity(m, 0f, 0f, playerPos[0], playerPos[1],0,0, getId(),
                    Generator.get(m.metaData)
                ).setHeight(tileSize)
                    .setWidth(tileSize).computePosition(playerPos, tileSize)
            )
            var used = false
            if (fee) {
                inventory[m] = inventory[m]!! - 1
                if (inventory[m]!! <= 0) {
                    inventory[m] = 0
                    used = true
                    model.player.offHand = null
                }
            }
            return used
        }


    }

}