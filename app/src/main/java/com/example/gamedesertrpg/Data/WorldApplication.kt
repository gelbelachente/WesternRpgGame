package de.gelbelachente.desertrpggame.Data

import android.app.Application

class WorldApplication : Application() {
val db : Database? by lazy { Database.getDatabase(this) }
}