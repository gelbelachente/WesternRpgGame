package de.gelbelachente.desertrpggame.game.animator

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.allViews
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.gamedesertrpg.R
import com.example.gamedesertrpg.databinding.ActivityGameBinding
import de.gelbelachente.desertrpggame.game.AcademyOfKnowledge
import de.gelbelachente.desertrpggame.game.DataObjects.Action
import de.gelbelachente.desertrpggame.game.DataObjects.Mob
import de.gelbelachente.desertrpggame.game.model.WorldTickController
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

interface MobAnimator {

    abstract fun getContext(): Context

    abstract fun getActivity(): Activity

    abstract fun getBinding(): ActivityGameBinding

    private val binding get() = getBinding()


    fun controlMobs() {
        WorldTickController.get().observer.add {
            GlobalScope.launch {

                val parent = binding.overlay
                val newMobList = mutableListOf<Mob>()
                try {
                    parent.allViews.filter {
                        it.getTag(R.string.mob_key) != null && binding.map.model.obtainMobs()
                            .none { mob -> mob.id == it.getTag(R.string.mob_key) as Int }
                    }.toList().forEach {
                        binding.map.model.mobs.toMutableList()
                            .find { mob -> mob.id == it.getTag(R.string.mob_key) }?.viewCreated =
                            false
                        getActivity().runOnUiThread {
                            parent.removeView(it)
                        }
                    }
                }catch (e : Exception){}

                for (mob in binding.map.model.obtainMobs()) {
                    var view =
                        parent.allViews.find { it.getTag(R.string.mob_key) != null && (it.getTag(R.string.mob_key) as Int) == mob.id }
                                as ImageView?
                    if (view != null) {
                        Log.d("BUGGER", "edited $mob")
                        val anim = AcademyOfKnowledge.animations[mob.type]!!.get(mob.action)!!
                        val x = mob.posX + binding.map.model.mapSize[0] / 2
                        val y = mob.posY + binding.map.model.mapSize[1] / 2

                        if (mob.live <= 0) {
                            binding.map.model.mobs.remove(mob)
                        }
                        var xy = mob.anim
                        if (mob.anim++ >= anim.size) {
                            mob.anim = 0
                            xy = 0
                            if (mob.action == Action.Attack) {
                                if (binding.map.model.doDamage(mob.id)) {
                                    getActivity().runOnUiThread {
                                        binding.liveText.text = "${binding.map.model.player.live}%"
                                        binding.liveText.invalidate()
                                        parent.removeView(view)
                                        MaterialAlertDialogBuilder(getActivity())
                                            .setBackground(getContext().resources.getDrawable(R.drawable.inventory))
                                            .setIcon(getContext().resources.getDrawable(R.drawable.heart))
                                            .setCancelable(false)
                                            .setMessage("You were killed by " + mob.type.toString() + "\n Your inventory was cleared.")
                                            .setPositiveButton(
                                                "continue",
                                                DialogInterface.OnClickListener { d, i ->
                                                })
                                            .setTitle("Dead")
                                            .show()
                                        binding.map.invalidate()
                                    }
                                    continue
                                }
                                getActivity().runOnUiThread {
                                    binding.liveText.text = "${binding.map.model.player.live}%"
                                    binding.liveText.invalidate()
                                }
                            }
                        }

                        getActivity().runOnUiThread {
                            view.layoutParams = ConstraintLayout.LayoutParams(mob.width, mob.height)
                            view.setImageResource(anim[xy])
                            view.x = x
                            view.y = y
                            view.scaleY = 1f
                            view.rotation = 0f
                            if (mob.rotate) {
                                view.scaleY = -1f
                                view.rotation = 180f
                            }
                            view.invalidate()
                        }
                    } else if (view == null) {
                        Log.d("BUGGER", "pre-deleted!")
                        newMobList.add(mob)
                    }
                }
                for (mob in newMobList) {
                    if (mob.viewCreated) {
                        continue
                    }
                    Log.d("BUGGER", "created")
                    val view = ImageView(getContext())
                    view.setImageResource(mob.type.drawable)
                    view.layoutParams = ViewGroup.LayoutParams(mob.width, mob.height)
                    view.x = mob.posX + binding.map.model.mapSize[0] / 2
                    view.y = mob.posY + binding.map.model.mapSize[1] / 2
                    view.setTag(R.string.mob_key, mob.id)
                    view.scaleType = ImageView.ScaleType.FIT_CENTER
                    mob.viewCreated = true
                    getActivity().runOnUiThread {
                        parent.addView(view)
                        view.invalidate()
                    }
                }
            }
        }
    }


}