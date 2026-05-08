package com.selfcare.tracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.selfcare.tracker.ui.LocalTranslations
import com.selfcare.tracker.ui.components.*
import com.selfcare.tracker.ui.theme.LocalAppColors
import com.selfcare.tracker.ui.viewmodel.DateRange
import com.selfcare.tracker.ui.viewmodel.StatsViewModel

@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val t = LocalTranslations.current
    val colors = LocalAppColors.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        DateRangeSelector(
            selected = uiState.dateRange,
            onSelect = { viewModel.setDateRange(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (uiState.statsConfig.showTotalCount) {
                StatCard(
                    title = t.totalCount,
                    value = "${uiState.statsResult.totalCount}${t.times}",
                    modifier = Modifier.weight(1f)
                )
            }
            if (uiState.statsConfig.showAvgDuration) {
                StatCard(
                    title = t.avgDuration,
                    value = formatDuration(uiState.statsResult.avgDuration),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (uiState.statsConfig.showMinDuration) {
                StatCard(
                    title = t.minDuration,
                    value = formatDuration(uiState.statsResult.minDuration),
                    modifier = Modifier.weight(1f)
                )
            }
            if (uiState.statsConfig.showMaxDuration) {
                StatCard(
                    title = t.maxDuration,
                    value = formatDuration(uiState.statsResult.maxDuration),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (uiState.statsConfig.showFrequency) {
                StatCard(
                    title = t.frequency,
                    value = String.format("%.1f%s", uiState.statsResult.frequency, t.timesPerWeek),
                    modifier = Modifier.weight(1f)
                )
            }
            if (uiState.statsConfig.showLongestAbstinence) {
                StatCard(
                    title = t.longestAbstinence,
                    value = "${uiState.statsResult.longestAbstinence}${t.days}",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.statsResult.compareLastMonth != 0f) {
            val change = uiState.statsResult.compareLastMonth
            val changeText = "${if (change >= 0) t.increase else t.decrease} ${kotlin.math.abs(change).toInt()}%"
            val changeColor = if (change >= 0) Color(0xFF10B981) else Color(0xFFEF4444)
            
            Text(
                text = "${t.vsLastMonth}: $changeText",
                style = MaterialTheme.typography.bodyMedium,
                color = changeColor
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.statsResult.mostUsedMedium != null) {
            Text(
                text = t.mostUsedMedium,
                style = MaterialTheme.typography.titleMedium,
                color = colors.textPrimary
            )
            Text(
                text = "${uiState.statsResult.mostUsedMedium!!.first} (${uiState.statsResult.mostUsedMedium!!.second}${t.times})",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (uiState.statsResult.mediumDistribution.isNotEmpty()) {
            Text(
                text = t.mediumDistribution,
                style = MaterialTheme.typography.titleMedium,
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            PieChart(
                data = uiState.statsResult.mediumDistribution.toList(),
                colors = listOf(
                    Color(0xFF6366F1), Color(0xFF10B981), Color(0xFFF59E0B),
                    Color(0xFFEF4444), Color(0xFF8B5CF6), Color(0xFF06B6D4)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (uiState.statsResult.durationTrend.isNotEmpty()) {
            Text(
                text = t.countTrend,
                style = MaterialTheme.typography.titleMedium,
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            LineChart(
                data = uiState.statsResult.durationTrend
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateRangeSelector(
    selected: DateRange,
    onSelect: (DateRange) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DateRange.entries.forEach { range ->
            val text = when (range) {
                DateRange.ThisWeek -> "本周"
                DateRange.ThisMonth -> "本月"
                DateRange.LastMonth -> "上月"
                DateRange.ThisYear -> "今年"
                DateRange.Custom -> "自定义"
            }
            FilterChip(
                selected = selected == range,
                onClick = { onSelect(range) },
                label = { Text(text, style = MaterialTheme.typography.bodySmall) }
            )
        }
    }
}

private fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return "${minutes}分${secs}秒"
}
