package com.selfcare.tracker.data.model

data class StatsResult(
    val totalCount: Int = 0,
    val avgDuration: Int = 0,
    val minDuration: Int = 0,
    val maxDuration: Int = 0,
    val frequency: Float = 0f,
    val longestAbstinence: Int = 0,
    val mediumDistribution: Map<String, Int> = emptyMap(),
    val fetishDistribution: Map<String, Int> = emptyMap(),
    val mostUsedMedium: Pair<String, Int>? = null,
    val mostUsedFetish: Pair<String, Int>? = null,
    val durationTrend: List<Pair<String, Int>> = emptyList(),
    val durationLineData: List<Pair<String, Int>> = emptyList(),
    val compareLastMonth: Float = 0f
)

data class ChartData(
    val label: String,
    val value: Float,
    val color: String = ""
)
