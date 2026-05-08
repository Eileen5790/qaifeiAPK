package com.selfcare.tracker.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ExportData(
    val version: String = "1.0",
    val exportTime: Long = System.currentTimeMillis(),
    val records: List<Record> = emptyList(),
    val media: List<Medium> = emptyList(),
    val fetishes: List<Fetish> = emptyList(),
    val statsConfig: StatsConfig = StatsConfig(),
    val uiConfig: UIConfig = UIConfig(),
    val settings: AppSettings = AppSettings()
)
