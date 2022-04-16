package de.gelbelachente.desertrpggame.game.animator

import android.app.Activity
import android.content.Context
import com.example.gamedesertrpg.databinding.ActivityGameBinding
import de.gelbelachente.desertrpggame.game.*
import de.gelbelachente.desertrpggame.game.DataObjects.Action


interface PlayerAnimator {

    abstract fun getContext() : Context

    abstract fun getActivity() : Activity

    abstract fun getBinding() : ActivityGameBinding

    private val binding get() = getBinding()

    fun getAnimation() = Animation(
        walking_anim, attacking_anim,null, taunt_animation,null, idle_animation)

    abstract fun getStorageList() : MutableMap<Int,Boolean>

    abstract fun setStorage(id: Int,change : Boolean)



    fun show(action: Action){

        var max = getStorageList().keys.size-1
        setStorage(max,false)
        setStorage(++max,true)
        when(action){
                Action.Idle -> idle(max)
                Action.MoveRight -> getRight(max)
                Action.MoveUp -> getUp(max)
                Action.MoveDown -> getDown(max)
                Action.MoveLeft -> getLeft(max)
                Action.Attack -> attack(max)
                Action.Hurt -> {}
                Action.Taunt -> taunt(max)
                Action.Dying -> {}
            }

    }

    fun attack(id : Int){
        var last = 0
        val anim = getAnimation()
        repeat(anim.attacking!!.size) {
            if(getStorageList()[id]!!) {
                val xy = last++
                getActivity().runOnUiThread {
                    binding.figure.setImageResource(anim.attacking[xy])
                    binding.figure.invalidate()
                }
                Thread.sleep(50)
            }
            }
        idle(id)
    }

    fun idle(id : Int){
        var last = 0
        val anim = getAnimation()
        while (getStorageList()[id]!!){
            if(last >= anim.idle!!.size){
                last = 0
            }
            val xy = last++
            getActivity().runOnUiThread {
                binding.figure.setImageResource(anim.idle[xy])
                binding.figure.invalidate()
            }
            Thread.sleep(100)
        }

    }

    fun taunt(id: Int){
        getActivity().runOnUiThread {
            binding.map.invalidate()
        }
        var last = 0
        val anim = getAnimation()
        while (getStorageList()[id]!!){
            if(last == anim.taunt!!.size){
                last = 0
            }
            val xy = last++
            getActivity().runOnUiThread {
                binding.figure.setImageResource(anim.taunt[xy])
                binding.figure.invalidate()
            }
            Thread.sleep(75)
        }
    }

    fun getUp(id : Int,y : Float = -.03f) {
        var last = 0
        val anim = getAnimation()
        while (getStorageList()[id]!!) {
            if (last == anim.walking!!.size) {
                last = 0
            }
            val pp = binding.map.model.player.playerPos
            binding.map.model.updatePlayer(0f,y)
            val xy = last++
            getActivity().runOnUiThread {
                binding.figure.setImageResource(anim.walking!![xy])
                binding.figure.invalidate()
                binding.map.invalidate()
                binding.xCoord.text = "X: ${pp[0].round(1.0)}"
                binding.yCoord.text = "Y: ${pp[1].round(1.0)}"
            }
            Thread.sleep(50)
        }
        show(Action.Idle)
    }

    fun getDown(id : Int) = getUp(id,.03f)

    fun getRight(id : Int) {
        var last = 0
        val anim = getAnimation()
        binding.figure.scaleY = 1f
        binding.figure.rotation = 0f
        while (getStorageList()[id]!!) {
            if (last == anim.walking!!.size) {
                last = 0
            }
            val pp = binding.map.model.player.playerPos
            binding.map.model.updatePlayer(.03f,0f)
            val xy = last++
            getActivity().runOnUiThread {
                binding.figure.setImageResource(anim.walking!![xy])
                binding.figure.invalidate()
                binding.map.invalidate()
                binding.xCoord.text = "X: ${pp[0].round(1.0)}"
                binding.yCoord.text = "X: ${pp[1].round(1.0)}"
            }
            Thread.sleep(50)
        }
        show(Action.Idle)
    }

    fun getLeft(id : Int) {
        var last = 0
        val anim = getAnimation()
        binding.figure.scaleY = -1f
        binding.figure.rotation = 180f
        while (getStorageList()[id]!!) {
            if (last == anim.walking!!.size) {
                last = 0
            }
            val pp = binding.map.model.player.playerPos
            binding.map.model.updatePlayer(-.03f,0f)
            val xy = last++
        getActivity().runOnUiThread {
            binding.figure.setImageResource(anim.walking!![xy])
            binding.figure.invalidate()
            binding.map.invalidate()
            binding.xCoord.text = "X: ${pp[0].round(1.0)}"
            binding.yCoord.text = "X: ${pp[1].round(1.0)}"
        }
        Thread.sleep(50)
    }
        show(Action.Idle)
    }





}