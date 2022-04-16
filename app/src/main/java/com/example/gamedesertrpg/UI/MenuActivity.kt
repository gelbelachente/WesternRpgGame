package de.gelbelachente.desertrpggame.UI

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.gamedesertrpg.R
import com.example.gamedesertrpg.databinding.ActivityMenuBinding
import de.gelbelachente.desertrpggame.game.model.SettingsManager
import de.gelbelachente.desertrpggame.game.toBoolean


class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    private var mp : MediaPlayer? = null
    private var isSound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ActivityCompat.requestPermissions(this,
            listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE).toTypedArray(), 0);

         isSound = SettingsManager.get().get("sound")!!.toBoolean()
        val time = intent.getIntExtra("sound_len",0)

        if(isSound) {
            mp = MediaPlayer.create(this@MenuActivity, R.raw.menu)
            mp?.isLooping = true
            mp?.seekTo(time)
            mp?.start()
        }

        binding.Multiplayer.setOnClickListener { _ ->
            Toast.makeText(this,"Multiplayer not available!",Toast.LENGTH_SHORT).show()
        }

        binding.SinglePlayer.setOnClickListener { view -> run{
            view.isEnabled = false
            val intent = Intent(this,SinglePlayerMenuActivity::class.java)
            if(mp != null) {
                intent.putExtra("sound_len", mp?.currentPosition)
            }
            if(intent.resolveActivity(packageManager) != null){
                startActivity(intent)
            }
            view.isEnabled = true
        }
        }
        binding.settings.setOnClickListener { view ->
            view.isEnabled = false
            val intent = Intent(this,SettingsActivity::class.java)
            if(intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
                view.isEnabled = true
            }
        }


    override fun onStop() {
        super.onStop()
        if(isSound) {
            mp?.pause()
        }
    }

    override fun onRestart() {
        super.onRestart()
        if(isSound){
            mp?.start()
        }
    }



}