package de.gelbelachente.desertrpggame.rw

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamedesertrpg.databinding.CraftingGridBinding
import de.gelbelachente.desertrpggame.game.AcademyOfKnowledge
import de.gelbelachente.desertrpggame.game.model.GameModel
import de.gelbelachente.desertrpggame.game.DataObjects.Material
import de.gelbelachente.desertrpggame.game.DataObjects.Recipe

class CraftAdapter(val ctx : Context, val list2 : MutableMap<Material,Int>, private val onItemCrafted: () -> Unit) : RecyclerView.Adapter<CraftAdapter.CraftViewHolder>() {

    class CraftViewHolder(binding : CraftingGridBinding) : RecyclerView.ViewHolder(binding.root) {
        val amount = binding.amount
        val icon = binding.result
        val ingredients = binding.ingredients
        val layout = binding.craftLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CraftViewHolder {
        val binding = CraftingGridBinding.inflate(LayoutInflater.from( parent.context),parent,false)
        return CraftViewHolder(binding)
    }

    var list = list2

    fun reset(list : MutableMap<Material,Int>){
        this.list = list
        calculateRecipes()
    }
    var possibleRecipes = mutableListOf<Recipe>()

    fun calculateRecipes(){
        possibleRecipes.clear()
        for (recipe in AcademyOfKnowledge.getRecipes(GameModel.get().player.skills)){
            var avaible = true
            for((idx,r) in recipe.ingredients.withIndex()) {
                avaible = avaible && list[r]!! >= recipe.ingredientAmount[idx]
            }
            if(avaible){
                possibleRecipes.add(recipe)
            }
        }
    }
    init {
        calculateRecipes()
    }

    override fun onBindViewHolder(holder: CraftViewHolder, position: Int) {
        holder.amount.text = possibleRecipes[position].amount.toString()
        holder.icon.setImageResource(possibleRecipes[position].product.drawable)
        val rw = holder.ingredients
        rw.layoutManager = GridLayoutManager(ctx,2)
        val map = possibleRecipes[position].ingredients.zip(possibleRecipes[position].ingredientAmount).toMap()
        rw.adapter = InventoryRecyclerView(ctx,map as MutableMap,{ position, yes ->},false)
        holder.layout.setOnClickListener {
            trigger(position)
        }
        holder.ingredients.setOnClickListener {
            trigger(position)
        }
        holder.icon.setOnClickListener {
            trigger(position)
        }
        holder.amount.setOnClickListener {
            trigger(position)
        }
    }
    fun trigger(position: Int){
        GameModel.get().craft(possibleRecipes[position])
        list = GameModel.get().player.inventory
        calculateRecipes()
        this.notifyDataSetChanged()
        onItemCrafted()
    }

    override fun getItemCount(): Int {
        return possibleRecipes.size
    }
}