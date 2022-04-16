package de.gelbelachente.desertrpggame.game.model

import android.R.attr.path
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.gamedesertrpg.R
import java.util.*


class NotificationManager {



    companion object{

        fun createChannel(ctx : Context){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    "525047616d65",
                    "DesertRpgGame",
                    NotificationManager.IMPORTANCE_LOW
                )
                channel.enableLights(true)
                channel.lightColor = Color.YELLOW
                channel.description = "DesertRpgGame"
                val manager = ctx.getSystemService(NotificationManager::class.java)
                manager.createNotificationChannel(channel)
            }
        }


        fun createDownloadNotification(ctx: Context, name : String,uri : Uri){

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.data = uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            val chooser = Intent.createChooser(
                intent,"Open World File"
            )
            val pi = PendingIntent.getActivity(ctx,0,chooser,
                PendingIntent.FLAG_CANCEL_CURRENT)

            val notif = NotificationCompat.Builder(ctx,"525047616d65")
                .setSmallIcon(R.drawable.download).setContentTitle("$name successfully downloaded")
                .setContentIntent(pi).build()

            NotificationManagerCompat.from(ctx).notify(Random().nextInt(),notif)


        }




    }

}