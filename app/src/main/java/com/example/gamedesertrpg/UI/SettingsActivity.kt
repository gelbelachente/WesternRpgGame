package de.gelbelachente.desertrpggame.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gamedesertrpg.databinding.ActivitySettingsBinding
import de.gelbelachente.desertrpggame.game.model.SettingsManager
import de.gelbelachente.desertrpggame.game.toText

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsBinding
    private val settings : SettingsManager by lazy { SettingsManager.get() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.soundText.text = settings.get("sound")!!.toText()
        binding.soundText.invalidate()
        binding.soundIcon.setOnClickListener { toggleSound() }
        binding.soundText.setOnClickListener { toggleSound() }
        binding.soundLayout.setOnClickListener { toggleSound() }

    }


    private fun toggleSound(){
        val txt = settings.toggle("sound")!!.toText()

        binding.soundText.text = txt
        binding.soundText.invalidate()
    }

}