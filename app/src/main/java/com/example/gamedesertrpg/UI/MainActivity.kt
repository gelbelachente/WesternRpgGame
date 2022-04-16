package de.gelbelachente.desertrpggame.UI

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import de.gelbelachente.desertrpggame.Data.WorldApplication
import com.example.gamedesertrpg.SharedStateHolderViewModel
import com.example.gamedesertrpg.databinding.ActivityMainBinding
import de.gelbelachente.desertrpggame.game.model.NotificationManager
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationManager.createChannel(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        var model = SharedStateHolderViewModel.get()
        binding.state = model
        binding.lifecycleOwner = this@MainActivity
        setContentView(binding.root)

        model.loadingProgress.postValue(0)
        model.loadingProgress.removeObservers(this)

        GlobalScope.launch(Dispatchers.IO) {
             model.load(this@MainActivity.application as WorldApplication)
        }

        val menu = Intent(this@MainActivity, MenuActivity::class.java)
        model.loadingProgress.observe(this@MainActivity) {
            if (it >= 100) {
                if (menu.resolveActivity(packageManager) != null) {
                    model.loadingProgress.postValue(0)
                    model.loadingProgress.removeObservers(this)
                    startActivity(menu)
                }
            }
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }
}