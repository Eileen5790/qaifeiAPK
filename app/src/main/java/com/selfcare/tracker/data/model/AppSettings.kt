package com.selfcare.tracker.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val lang: String = "zh",
    val theme: String = "light",
    val colorTheme: String = "default"
)
