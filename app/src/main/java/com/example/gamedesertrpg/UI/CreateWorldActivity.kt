package de.gelbelachente.desertrpggame.UI

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gamedesertrpg.databinding.ActivityCreateWorldBinding
import de.gelbelachente.desertrpggame.Data.World
import com.example.gamedesertrpg.SharedStateHolderViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class CreateWorldActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCreateWorldBinding
    private val model = SharedStateHolderViewModel.get()
    private var new : Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateWorldBinding.inflate(layoutInflater)
        setContentView(binding.root)

        new = intent.getBooleanExtra("new",true)
        val path = intent.getStringExtra("file_uri")

        binding.submit.setOnClickListener { view ->
            view.isEnabled = true
            val name = binding.inputName.text.trim().toString()
            if(name.isBlank() || model.exists(name)){
                Toast.makeText(this,"This name is either null or already used!",Toast.LENGTH_LONG).show()
                view.isEnabled = true
                return@setOnClickListener
            }
            val intent = Intent(this,SinglePlayerMenuActivity::class.java)

            GlobalScope.launch(Dispatchers.Main) {
                if (new) {
                    model.worldDao.addWorld(World(name))
                } else {
                    val file = File(path)
                    val txt = file.readText()
                    val world = World.fromString(txt)
                    world.name = name
                    SharedStateHolderViewModel.get().worldDao.addWorld(world)
                    Toast.makeText(this@CreateWorldActivity, "${world.name} created", Toast.LENGTH_SHORT).show()
                }
            }
            if(intent.resolveActivity(packageManager) != null){
                startActivity(intent)
            }


            view.isEnabled = true
        }

    }
}