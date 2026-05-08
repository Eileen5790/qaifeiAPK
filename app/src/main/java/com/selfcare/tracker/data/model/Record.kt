package com.selfcare.tracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "records")
data class Record(
    @PrimaryKey
    val id: Long = System.currentTimeMillis(),
    val startTime: Long,
    val duration: Int,
    val mediumId: String,
    val fetishIds: String = "",
    val notes: String = "",
    val satisfaction: Int = 5
) {
    fun getFetishList(): List<String> =
        if (fetishIds.isBlank()) emptyList()
        else fetishIds.split(",").filter { it.isNotBlank() }

    companion object {
        fun createFetishString(ids: List<String>): String = ids.joinToString(",")
    }
}
