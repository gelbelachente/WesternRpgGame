package de.gelbelachente.desertrpggame.rw

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.gamedesertrpg.databinding.InventoryGridBinding
import de.gelbelachente.desertrpggame.game.model.GameModel
import de.gelbelachente.desertrpggame.game.DataObjects.Material

class InventoryRecyclerView(val ctx : Context, val list2 : MutableMap<Material,Int>,
                            val onForceRedraw : (String,Boolean) -> (Unit), val focusable : Boolean = true) : RecyclerView.Adapter<InventoryRecyclerView.InvViewHolder>() {

    class InvViewHolder(binding : InventoryGridBinding) : RecyclerView.ViewHolder(binding.root){
        val amount = binding.amount
        val icon = binding.itemIcon
        val layout = binding.invLayout
        val nameHolder = binding.nameHolder
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvViewHolder {
        val binding = InventoryGridBinding.inflate(LayoutInflater.from( parent.context),parent,false)
        return InvViewHolder(binding)
    }
    var list = list2
    var items = list.keys.filter { list[it]!! > 0 }.toMutableList()

    fun reset(list : MutableMap<Material,Int>){
        this.list = list
        this.items = list.keys.filter { list[it]!! > 0 }.toMutableList()
    }

    var name = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: InvViewHolder, position: Int) {
        holder.amount.text = list[items[position]!!]!!.toString()
        holder.icon.setImageResource(items[position].drawable)
        holder.nameHolder.text = items[position].name
        holder.layout.setOnClickListener {
                onForceRedraw(name, false)
            if(name == items[position].name && GameModel.get().player.offHand != null){
                GameModel.get().player.offHand = null
            }else {
                GameModel.get().player.offHand = items[position]
                onForceRedraw(items[position].name, focusable)
                name = items[position].name
            }
        }
        holder.icon.setOnClickListener {
            onForceRedraw(name, false)
            if(name == items[position].name && GameModel.get().player.offHand != null){
                GameModel.get().player.offHand = null
            }else {
                GameModel.get().player.offHand = items[position]
                onForceRedraw(items[position].name, focusable)
                name = items[position].name
            }
        }
    }




    override fun getItemCount(): Int {
        return items.size
    }
}