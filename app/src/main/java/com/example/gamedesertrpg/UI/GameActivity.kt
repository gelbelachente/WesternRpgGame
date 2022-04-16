package de.gelbelachente.desertrpggame.UI

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamedesertrpg.R
import com.example.gamedesertrpg.SharedStateHolderViewModel
import com.example.gamedesertrpg.databinding.ActivityGameBinding
import de.gelbelachente.desertrpggame.game.DataObjects.Action
import de.gelbelachente.desertrpggame.game.DataObjects.Type
import de.gelbelachente.desertrpggame.game.animator.MobAnimator
import de.gelbelachente.desertrpggame.game.animator.PlayerAnimator
import de.gelbelachente.desertrpggame.game.model.DayTime
import de.gelbelachente.desertrpggame.game.model.MobController
import de.gelbelachente.desertrpggame.game.model.SettingsManager
import de.gelbelachente.desertrpggame.game.model.WorldTickController
import de.gelbelachente.desertrpggame.game.toBoolean
import de.gelbelachente.desertrpggame.rw.CraftAdapter
import de.gelbelachente.desertrpggame.rw.InventoryRecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import kotlin.random.Random

class GameActivity() : AppCompatActivity(), PlayerAnimator, MobAnimator {
    override fun getContext(): Context = this

    override fun getActivity(): Activity = this

    override fun getBinding(): ActivityGameBinding = binding


    private lateinit var binding: ActivityGameBinding

    private val storage = mutableMapOf<Int, Boolean>(0 to true)

    @Synchronized
     override fun getStorageList(): MutableMap<Int, Boolean> {
        return storage.toMutableMap()
    }

    @Synchronized
    override fun setStorage(id: Int, change: Boolean) {
        storage[id] = change
    }

    private lateinit var mp : MediaPlayer
    var anim = "idle"

    private var isSound = true

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        binding.map.model.setWorld(
            SharedStateHolderViewModel.get().getWorld(intent.extras!!.get("world_name") as String)
        )
        isSound = SettingsManager.get().get("sound")!!.toBoolean()

        setContentView(binding.root)

        binding.map.invalidate()
        registerClickListener()
        registerAdapter()
        registerStateObserver()
        MobController.get().start()
        registerMobSpawner()
        GlobalScope.launch {
            show(Action.Idle)
        }
        if(WorldTickController.get().time == DayTime.Night||binding.map.model.getUniverse().type.dark){
           binding.backlayer.setBackgroundResource(R.drawable.dark)
            binding.overlay.alpha = .7f
        }
        binding.liveText.text = binding.map.model.player.live.toString() + "%"

        controlMobs()
        if(isSound) {
            mp = MediaPlayer.create(this@GameActivity, R.raw.background_music)
            mp.isLooping = true
            mp.start()
        }

        GlobalScope.launch {
                Looper.prepare()
            sleep(200000)
            backup()
        }
    }

   private fun backup(){
        if(!isStopped){
            Toast.makeText(this@GameActivity, "Backup created!", Toast.LENGTH_SHORT).show()
            binding.map.model.stop()
            sleep(200000)
            backup()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    var isStopped = false

    override fun onStop() {
        if(isSound) {
            mp.pause()
            binding.map.model.stop()
        }
        super.onStop()
        }

    override fun onDestroy() {
        isStopped = true
        binding.map.model.stop()
        binding.map.model.destroy()
        super.onDestroy()
    }

    override fun onRestart() {
        super.onRestart()
        if(isSound){
            mp.start()
        }
    }

    fun registerMobSpawner(){

        WorldTickController.get().observer.add {
            GlobalScope.launch {
                if(Random.nextInt(2000) <= 0){
                    binding.map.model.spawn()
                }
            }
        }

    }


    fun registerStateObserver() {
        GlobalScope.launch {
            Looper.prepare()
            WorldTickController.get().infiniteLoop(this@GameActivity)
        }
        WorldTickController.get().dayObserver.add { d ->
            if (d == DayTime.Day) {
                binding.overlay.alpha = 1f
            } else {
                binding.backlayer.setBackground(resources.getDrawable(R.drawable.dark))
                binding.overlay.alpha = .7f
            }
            Toast.makeText(this, d.toString(), Toast.LENGTH_LONG).show()
            binding.backlayer.invalidate()
        }

        WorldTickController.get().weatherObserver.add { w ->
            if (w.color == null) {
                binding.overlay.alpha = 1f
            } else {
                binding.overlay.setBackgroundColor(resources.getColor(w.color))
                binding.overlay.alpha = .7f
            }
            binding.backlayer.invalidate()
        }

    }

    fun registerClickListener() {

        binding.enter.setOnClickListener {
                if(binding.map.model.enter { mes ->
                        Toast.makeText(this, mes, Toast.LENGTH_SHORT).show()
                    }){
                }else{
                    binding.map.invalidate()
                }
            binding.overlay.alpha = binding.map.model.getUniverse().type.alpha
            if(binding.map.model.getUniverse().type.dark){
                binding.backlayer.setBackgroundResource(R.drawable.dark)
            }else
                if(WorldTickController.get().time == DayTime.Night){
                    binding.overlay.alpha = .7f
                    binding.backlayer.setBackgroundResource(R.drawable.dark)
                }
            binding.map.invalidate()
        }

        binding.action.setOnClickListener { view ->

            val isAttacked = binding.map.model.attack()
            if(isAttacked[1]){
                GlobalScope.launch {
                    if (!isAttacked[0]) {
                        show(Action.Attack)
                    }else{
                        show(Action.Taunt)
                    }
                }
                return@setOnClickListener
            }

            if( binding.map.model.startInteraction({ trade, doable ->

                binding.tradeClose.setOnClickListener {
                    binding.trade.visibility = View.GONE
                }

                binding.tradeSubmit.isEnabled = doable
                binding.tradeSubmit.setOnClickListener {
                    if (doable) {
                        binding.map.model.trade(trade)
                        Toast.makeText(this, "Succesfully traded!", Toast.LENGTH_SHORT).show()
                        binding.trade.visibility = View.GONE
                        binding.map.invalidate()
                    } else {
                        Toast.makeText(
                            this,
                            "You don't fulfil the requirements!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                binding.tradeGiven.adapter = InventoryRecyclerView(
                    this, trade.ingredients.zip(
                        trade.ingredientAmount
                    ).toMap().toMutableMap(), { i, b -> }, false
                )
                binding.tradeGiven.layoutManager = GridLayoutManager(this, 2)
                binding.tradeReceived.adapter = InventoryRecyclerView(
                    this, trade.offer.zip(
                        trade.offerAmount
                    ).toMap().toMutableMap(), { i, b -> }, false
                )
                binding.tradeReceived.layoutManager = GridLayoutManager(this, 2)
                binding.tradeReceived.invalidate()
                binding.tradeGiven.invalidate()
                binding.tradeSubmit.invalidate()
                binding.trade.visibility = View.VISIBLE
                binding.trade.bringToFront()
            },
                { done ->
                    if (done) {
                        Toast.makeText(this, "You slept during the Night!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, "You can only sleep at Night!", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                { question, reward ->
                    binding.qaQuestion.text = question.question
                    binding.qaReward.setImageResource(reward.drawable)

                    binding.qa.visibility = View.VISIBLE
                    binding.qa.bringToFront()
                    binding.qaRight.setOnClickListener {
                        binding.qa.visibility = View.GONE
                        if (question.right) {
                            binding.map.model.qaReward(reward)
                            Toast.makeText(this, "You were right!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "You were wrong", Toast.LENGTH_SHORT).show()
                            binding.map.model.qaReward(reward, false)
                        }
                        binding.map.invalidate()
                    }
                    binding.qaWrong.setOnClickListener {
                        binding.qa.visibility = View.GONE
                        if (!question.right) {
                            binding.map.model.qaReward(reward)
                            Toast.makeText(this, "You were right!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "You were wrong", Toast.LENGTH_SHORT).show()
                            binding.map.model.qaReward(reward, false)
                        }
                        binding.map.invalidate()
                    }

                },
                {mes -> Toast.makeText(this,mes,Toast.LENGTH_SHORT).show()
                },
                {
                    binding.liveText.text = binding.map.model.player.live.toString() + "%"
                    binding.liveText.invalidate()
                    if(binding.map.model.player.inventory[binding.map.model.player.offHand!!]!! <= 0){
                        binding.offHand.visibility = View.GONE
                        binding.offHand.invalidate()
                        binding.map.model.player.offHand = null
                    }
                }
            )
            ){
            return@setOnClickListener
            }

            //destroy
            if (binding.map.model.player.offHand == null || binding.map.model.player.offHand!!.type == Type.Weapon) {
                binding.map.model.destroy()
                GlobalScope.launch {
                    show(Action.Attack)
                }
                binding.map.invalidate()
                //build sth.
            } else if (binding.map.model.build(binding.map.model.player.offHand!!)) {
                binding.offHand.visibility = View.GONE
                binding.map.invalidate()
            }
        }


        binding.bag.setOnClickListener {
            Log.d("BUGGER", "clicked!")
            binding.InventoryLayout.visibility = View.VISIBLE
            val list = binding.map.model.player.inventory
            (binding.inventory.adapter as InventoryRecyclerView).reset(list)
            binding.inventory.invalidate()
            (binding.inventory.adapter as InventoryRecyclerView).notifyDataSetChanged()
            (binding.craft.adapter as CraftAdapter).reset(list)
            binding.craft.invalidate()
            (binding.craft.adapter as CraftAdapter).notifyDataSetChanged()

        }

        binding.close.setOnClickListener {
            binding.InventoryLayout.visibility = View.GONE
            if (binding.map.model.player.offHand != null) {
                binding.offHand.setImageResource(binding.map.model.player.offHand!!.drawable)
                binding.offHand.visibility = View.VISIBLE
                binding.offHand.invalidate()
            }
        }

        binding.upMove.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
                when (event!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        binding.map.invalidate()
                        for (idx in 0 until (getStorageList().keys.size - 1)) {
                            setStorage(idx, false)
                        }
                        GlobalScope.launch {
                            show(Action.MoveUp)
                        }

                        return true
                    }
                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                        setStorage(getStorageList().size - 1, false)
                        return false
                    }
                }
                return true
            }
        })

        binding.downMove.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
                when (event!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        binding.map.invalidate()
                        for (idx in 0 until (getStorageList().keys.size - 1)) {
                            setStorage(idx, false)
                        }
                        GlobalScope.launch {
                            show(Action.MoveDown)
                        }
                        return true
                    }
                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                        setStorage(getStorageList().size - 1, false)
                        return false
                    }
                }
                return true
            }
        })
        binding.rightMove.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
                when (event!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        binding.map.invalidate()
                        for (idx in 0 until (getStorageList().keys.size - 1)) {
                            setStorage(idx, false)
                        }
                        GlobalScope.launch {
                            show(Action.MoveRight)
                        }
                        return true
                    }
                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                        setStorage(getStorageList().size - 1, false)
                        return false
                    }
                }
                return true
            }
        })
        binding.leftMove.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
                when (event!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        binding.map.invalidate()
                        for (idx in 0 until (getStorageList().keys.size - 1)) {
                            setStorage(idx, false)
                        }
                        GlobalScope.launch {
                            show(Action.MoveLeft)
                        }
                        return true
                    }
                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                        setStorage(getStorageList().size - 1, false)
                        return false
                    }
                }
                return true
            }
        })


    }

    fun registerAdapter() {
        val rw = binding.inventory
        rw.layoutManager = GridLayoutManager(this, 4)
        rw.adapter = InventoryRecyclerView(this, binding.map.model.player.inventory, { position, yes ->
            val lm = rw.layoutManager as GridLayoutManager
            for (idx in 0 until lm.childCount) {
                val child = lm.getChildAt(idx)!!
                val x = (child as ConstraintLayout)
                val ic = x.findViewById<ImageView>(R.id.itemIcon)
                if (position == x.findViewById<TextView>(R.id.name_holder).text && yes) {
                    child.background = resources.getDrawable(R.drawable.special_stroke)
                    binding.offHand.visibility = View.VISIBLE
                    binding.offHand.setImageResource(binding.map.model.player.offHand!!.drawable)
                } else {
                    child.background = resources.getDrawable(R.drawable.inventory_stroke)
                    binding.offHand.visibility = View.GONE
                }
                ic.invalidate()
                binding.offHand.invalidate()
            }

        })
        val rw2 = binding.craft
        rw2.layoutManager = LinearLayoutManager(this)
        rw2.adapter = CraftAdapter(this, binding.map.model.player.inventory) {
            val list = binding.map.model.player.inventory
            (binding.inventory.adapter as InventoryRecyclerView).reset(list)
            binding.inventory.invalidate()
            (binding.inventory.adapter as InventoryRecyclerView).notifyDataSetChanged()
        }
    }


}

