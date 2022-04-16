package de.gelbelachente.desertrpggame.UI

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gamedesertrpg.R
import com.example.gamedesertrpg.databinding.ActivityMultiPlayerMenuBinding

class MultiPlayerMenuActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMultiPlayerMenuBinding
    private lateinit var mp : MediaPlayer
    private var isSound = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiPlayerMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}