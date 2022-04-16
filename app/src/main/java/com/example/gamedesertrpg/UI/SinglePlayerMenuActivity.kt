package de.gelbelachente.desertrpggame.UI

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import de.gelbelachente.desertrpggame.Data.World
import com.example.gamedesertrpg.R
import com.example.gamedesertrpg.SharedStateHolderViewModel
import com.example.gamedesertrpg.databinding.ActivitySinglePlayerMenuBinding
import de.gelbelachente.desertrpggame.game.model.SettingsManager
import de.gelbelachente.desertrpggame.game.toBoolean
import de.gelbelachente.desertrpggame.rw.WorldRecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File


class SinglePlayerMenuActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySinglePlayerMenuBinding
    private val viewModel = SharedStateHolderViewModel.get()

    private lateinit var mp : MediaPlayer
    private var isSound = false

    private lateinit var world : World

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySinglePlayerMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isSound = SettingsManager.get().get("sound")!!.toBoolean()
        val time = intent.getIntExtra("sound_len",0)

        if(isSound) {
            mp = MediaPlayer.create(this@SinglePlayerMenuActivity, R.raw.menu)
            mp.isLooping = true
            mp.seekTo(time)
            mp.start()
        }

        val rw = binding.worlds
        rw.layoutManager = LinearLayoutManager(this)
        val adapter = WorldRecyclerView (this) {
            val intent = Intent(this,GameActivity::class.java)
            intent.putExtra("world_name",it.name)
            if(intent.resolveActivity(packageManager) != null){
                startActivity(intent)
            }
        }
        rw.adapter = adapter
        GlobalScope.launch(Dispatchers.Main) {
            viewModel.getWorlds().collect {
                adapter.submitList(it)
            }
        }
        binding.addWorld.setOnClickListener { view ->
            view.isEnabled = false
            val intent = Intent(this,CreateWorldActivity::class.java)
            if(intent.resolveActivity(packageManager) != null){
                intent.putExtra("new",true)
                startActivity(intent)
            }
            view.isEnabled = true
        }

        binding.worldUpload.setOnClickListener {view ->
            view.isEnabled = false

            getContent.launch("text/*")

            view.isEnabled = true
        }

    }

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if(uri == null)return@registerForActivityResult;
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val path = dir.absolutePath + "/${uri.path!!.substringAfterLast("/")}"
        val file = File(path)
        val txt = file.readText()
        val world = World.fromString(txt)
        if(SharedStateHolderViewModel.get().worldDao.getWorldsAsPrimitive().none{it.name == world.name}){
            GlobalScope.launch {
                SharedStateHolderViewModel.get().worldDao.addWorld(world)
            }
                Toast.makeText(this@SinglePlayerMenuActivity, "${world.name} created", Toast.LENGTH_SHORT).show()
            }else{
            Toast.makeText(this,"A world with this name does already exist!",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,CreateWorldActivity::class.java)
            if(intent.resolveActivity(packageManager) != null){
                intent.putExtra("new",false)
                intent.putExtra("file_uri", path)
                startActivity(intent)
            }
        }
    }


    override fun onStop() {
        super.onStop()
        if(isSound) {
            mp.pause()
        }
    }

    override fun onRestart() {
        super.onRestart()
        if(isSound){
            mp.start()
        }
    }

}