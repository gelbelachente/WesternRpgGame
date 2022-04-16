package de.gelbelachente.desertrpggame.rw

import android.content.Context
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.gelbelachente.desertrpggame.Data.World
import com.example.gamedesertrpg.SharedStateHolderViewModel
import com.example.gamedesertrpg.databinding.WorldBinding
import de.gelbelachente.desertrpggame.game.model.NotificationManager
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class WorldRecyclerView(val ctx : Context, private val onItemClicked: (World) -> Unit)
    : ListAdapter<World, WorldRecyclerView.MyViewHolder>(DiffCallback) {



    class MyViewHolder(binding : WorldBinding) : RecyclerView.ViewHolder(binding.root) {
         val name = binding.name
         val lastPlayed = binding.lastPlayed
         val createdAt = binding.createdAt
        val layout = binding.worldLayout
        val delete = binding.worldDelete
        val dwonload = binding.worldDownload
        fun bind(world: World,ctx: Context) {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            sdf.timeZone = TimeZone.getTimeZone("GMT")
            try {
                val LastTimePlayed : Long = world.lastPlayed.toLong()
                val now = System.currentTimeMillis()
                val created : Long = world.createdAt.toLong()
                val lastPlayedTimeSince = TimeUnit.MILLISECONDS.toDays(now-LastTimePlayed)
                val createdTimeSince = TimeUnit.MILLISECONDS.toDays(now-LastTimePlayed)
                lastPlayed.text = "$lastPlayedTimeSince days ago"
                createdAt.text = "created $createdTimeSince days ago"
            } catch (e: ParseException) {}

            delete.setOnClickListener {
                it.isEnabled = false
                SharedStateHolderViewModel.get().worldDao.removeWorld(world)
            }

            dwonload.setOnClickListener {
                it.isEnabled = false
                val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val txt = world.toString()
                val path = check(dir.absolutePath + "/${world.name}")
                val file = File(path)
                file.writeText(txt)
                file.createNewFile()
                val uri = FileProvider.getUriForFile(ctx, ctx.getApplicationContext().getPackageName() + ".provider", file)
                Log.d("BUGGER",uri.toString())
                NotificationManager.createDownloadNotification(ctx,world.name, uri)
                it.isEnabled = true
            }

            name.text = world.name
        }

        fun check(path : String,operator : Int = 0) : String{
            val files = File(path + operator + ".txt")
            if(files.exists()){
                return check(path,operator+1)
            }
            return path + operator + ".txt"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val viewHolder = MyViewHolder(
            WorldBinding.inflate(
                LayoutInflater.from( parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position),ctx)
    }
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<World>() {
            override fun areItemsTheSame(oldItem: World, newItem: World): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: World, newItem: World): Boolean {
                return oldItem == newItem
            }
        }
    }

}