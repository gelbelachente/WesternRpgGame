package de.gelbelachente.desertrpggame.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Setting(@PrimaryKey val key : String,val value : String)
