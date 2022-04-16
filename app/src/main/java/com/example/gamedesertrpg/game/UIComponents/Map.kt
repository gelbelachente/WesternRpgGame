package de.gelbelachente.desertrpggame.game.UIComponents

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import de.gelbelachente.desertrpggame.game.DataObjects.Material
import de.gelbelachente.desertrpggame.game.DataObjects.NPC
import de.gelbelachente.desertrpggame.game.model.GameModel
import kotlin.math.roundToInt


class Map @JvmOverloads constructor(val ctx : Context,attrs : AttributeSet? = null,style : Int = 0) : View(ctx,attrs,style) {


    val model : GameModel = GameModel.get()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        model.computeConstants(w.toFloat(), h.toFloat())

        Material.bitmaps.apply {
            for(m in Material.values()){
                var icon = BitmapFactory.decodeResource(
                    ctx.resources,
                    m.drawable)
                icon = Bitmap.createScaledBitmap(icon, (model.tileSize * m.w).roundToInt() , (model.tileSize * m.h).roundToInt(), true);
                put(m,icon)
            }
        }
        NPC.bitmaps.apply {
            for(m in NPC.values()){
                var icon = BitmapFactory.decodeResource(
                    ctx.resources,
                    m.drawable)
                icon = Bitmap.createScaledBitmap(icon, (model.tileSize * m.w).roundToInt() , (model.tileSize * m.h).roundToInt(), true);
                put(m,icon)
            }
        }
    }

    var firstTime = false

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if(!firstTime) {
            model.start()
            firstTime = true
        }
        model.render()

        for(entity in model.getEntities()){
            var icon = Material.bitmaps[entity.type]!!
            canvas?.drawBitmap(icon,entity.posX+width/2,entity.posY + height/2,Paint())
        }


    }


}