package com.selfcare.tracker.data.model

import kotlinx.serialization.Serializable

@Serializable
data class StatsConfig(
    val showThisWeek: Boolean = true,
    val showThisMonth: Boolean = true,
    val showLastMonth: Boolean = true,
    val showThisYear: Boolean = true,
    val weekStartDay: String = "monday",
    val syncTime: Boolean = true,
    val showTotalCount: Boolean = true,
    val showFrequency: Boolean = true,
    val showLongestAbstinence: Boolean = true,
    val showAvgDuration: Boolean = true,
    val showMinDuration: Boolean = true,
    val showMaxDuration: Boolean = true,
    val durationChartCount: Int = 5,
    val showDurationDiff: Boolean = true,
    val timeGroupingWeek: String = "day",
    val timeGroupingMonth: String = "week",
    val timeGroupingYear: String = "month",
    val sortBy: String = "count",
    val chartType: String = "bar",
    val showAllCountAnalysis: Boolean = false,
    val allDataGrouping: String = "year"
)
