package com.selfcare.tracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "fetishes")
data class Fetish(
    @PrimaryKey
    val id: Long = System.currentTimeMillis(),
    val name: String
)
