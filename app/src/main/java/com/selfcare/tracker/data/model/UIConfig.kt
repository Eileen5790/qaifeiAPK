package com.selfcare.tracker.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UIConfig(
    val cornerRadius: String = "medium",
    val shadowIntensity: String = "medium",
    val lightBg: String = "",
    val darkBg: String = "",
    val lightBgImage: String = "",
    val darkBgImage: String = "",
    val addIcon: String = "",
    val recordsIcon: String = "",
    val statsIcon: String = "",
    val settingsIcon: String = "",
    val cardBg: String = "",
    val textPrimary: String = "",
    val textSecondary: String = "",
    val borderColor: String = "",
    val fontSize: String = "medium",
    val spacing: String = "standard",
    val navPosition: String = "bottom",
    val defaultHome: String = "add",
    val navOrder: List<String> = listOf("add", "records", "stats", "settings"),
    val statsSortOrder: String = "desc",
    val statsButtonsPosition: String = "top"
)
