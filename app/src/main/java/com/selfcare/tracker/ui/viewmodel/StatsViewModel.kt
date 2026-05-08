package com.selfcare.tracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selfcare.tracker.data.model.*
import com.selfcare.tracker.data.repository.FetishRepository
import com.selfcare.tracker.data.repository.MediumRepository
import com.selfcare.tracker.data.repository.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class StatsUiState(
    val statsResult: StatsResult = StatsResult(),
    val media: Map<String, String> = emptyMap(),
    val fetishes: Map<String, String> = emptyMap(),
    val dateRange: DateRange = DateRange.ThisMonth,
    val customStartDate: Long = 0L,
    val customEndDate: Long = 0L,
    val isLoading: Boolean = true,
    val statsConfig: StatsConfig = StatsConfig()
)

enum class DateRange { ThisWeek, ThisMonth, LastMonth, ThisYear, Custom }

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val recordRepository: RecordRepository,
    private val mediumRepository: MediumRepository,
    private val fetishRepository: FetishRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                recordRepository.getAllRecords(),
                mediumRepository.getAllMedia(),
                fetishRepository.getAllFetishes()
            ) { records, media, fetishes ->
                Triple(records, media, fetishes)
            }.collect { (records, media, fetishes) ->
                val mediaMap = media.associate { it.id.toString() to it.name }
                val fetishMap = fetishes.associate { it.id.toString() to it.name }
                
                _uiState.update { state ->
                    val filteredRecords = filterRecords(records, state.dateRange, state.customStartDate, state.customEndDate)
                    state.copy(
                        statsResult = calculateStats(filteredRecords, records, mediaMap, fetishMap),
                        media = mediaMap,
                        fetishes = fetishMap,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun setDateRange(range: DateRange) {
        _uiState.update { it.copy(dateRange = range, isLoading = true) }
        loadData()
    }

    fun setCustomDateRange(start: Long, end: Long) {
        _uiState.update { it.copy(
            dateRange = DateRange.Custom,
            customStartDate = start,
            customEndDate = end,
            isLoading = true
        )}
        loadData()
    }

    private fun filterRecords(records: List<Record>, range: DateRange, customStart: Long, customEnd: Long): List<Record> {
        val now = Calendar.getInstance()
        val (start, end) = when (range) {
            DateRange.ThisWeek -> {
                val cal = Calendar.getInstance()
                cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                Pair(cal.timeInMillis, System.currentTimeMillis())
            }
            DateRange.ThisMonth -> {
                val cal = Calendar.getInstance()
                cal.set(Calendar.DAY_OF_MONTH, 1)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                Pair(cal.timeInMillis, System.currentTimeMillis())
            }
            DateRange.LastMonth -> {
                val cal = Calendar.getInstance()
                cal.add(Calendar.MONTH, -1)
                cal.set(Calendar.DAY_OF_MONTH, 1)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                val start = cal.timeInMillis
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
                cal.set(Calendar.HOUR_OF_DAY, 23)
                cal.set(Calendar.MINUTE, 59)
                cal.set(Calendar.SECOND, 59)
                Pair(start, cal.timeInMillis)
            }
            DateRange.ThisYear -> {
                val cal = Calendar.getInstance()
                cal.set(Calendar.DAY_OF_YEAR, 1)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                Pair(cal.timeInMillis, System.currentTimeMillis())
            }
            DateRange.Custom -> Pair(customStart, customEnd)
        }
        return records.filter { it.startTime in start..end }
    }

    private fun calculateStats(
        filteredRecords: List<Record>,
        allRecords: List<Record>,
        mediaMap: Map<String, String>,
        fetishMap: Map<String, String>
    ): StatsResult {
        if (filteredRecords.isEmpty()) return StatsResult()

        val durations = filteredRecords.map { it.duration }
        val totalDuration = durations.sum()
        val avgDuration = if (filteredRecords.isNotEmpty()) totalDuration / filteredRecords.size else 0

        val mediumDistribution = filteredRecords
            .groupBy { it.mediumId }
            .mapValues { it.value.size }
            .mapKeys { mediaMap[it.key] ?: it.key }

        val fetishDistribution = filteredRecords
            .flatMap { it.getFetishList() }
            .groupBy { it }
            .mapValues { it.value.size }
            .mapKeys { fetishMap[it.key] ?: it.key }

        val mostUsedMedium = mediumDistribution.maxByOrNull { it.value }
        val mostUsedFetish = fetishDistribution.maxByOrNull { it.value }

        val sortedRecords = filteredRecords.sortedBy { it.startTime }
        var longestAbstinence = 0
        for (i in 1 until sortedRecords.size) {
            val diff = (sortedRecords[i].startTime - sortedRecords[i-1].startTime) / (1000 * 60 * 60 * 24)
            if (diff > longestAbstinence) longestAbstinence = diff.toInt()
        }

        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)
        calendar.add(Calendar.MONTH, -1)
        val lastMonth = calendar.get(Calendar.MONTH)
        val lastMonthYear = calendar.get(Calendar.YEAR)

        val thisMonthCount = allRecords.count {
            val cal = Calendar.getInstance().apply { timeInMillis = it.startTime }
            cal.get(Calendar.MONTH) == currentMonth && cal.get(Calendar.YEAR) == currentYear
        }
        val lastMonthCount = allRecords.count {
            val cal = Calendar.getInstance().apply { timeInMillis = it.startTime }
            cal.get(Calendar.MONTH) == lastMonth && cal.get(Calendar.YEAR) == lastMonthYear
        }

        val compareChange = if (lastMonthCount > 0) {
            ((thisMonthCount - lastMonthCount).toFloat() / lastMonthCount) * 100
        } else 0f

        val weeks = if (filteredRecords.isNotEmpty()) {
            val first = filteredRecords.minByOrNull { it.startTime }!!
            val last = filteredRecords.maxByOrNull { it.startTime }!!
            val diff = last.startTime - first.startTime
            maxOf(1, (diff / (7 * 24 * 60 * 60 * 1000)).toInt() + 1)
        } else 1
        val frequency = filteredRecords.size.toFloat() / weeks

        val durationTrend = filteredRecords
            .groupBy { 
                val cal = Calendar.getInstance().apply { timeInMillis = it.startTime }
                "${cal.get(Calendar.MONTH) + 1}/${cal.get(Calendar.DAY_OF_MONTH)}"
            }
            .mapValues { it.value.sumOf { r -> r.duration } }
            .entries
            .takeLast(7)
            .map { it.key to it.value }

        return StatsResult(
            totalCount = filteredRecords.size,
            avgDuration = avgDuration,
            minDuration = durations.minOrNull() ?: 0,
            maxDuration = durations.maxOrNull() ?: 0,
            frequency = frequency,
            longestAbstinence = longestAbstinence,
            mediumDistribution = mediumDistribution,
            fetishDistribution = fetishDistribution,
            mostUsedMedium = mostUsedMedium?.let { it.key to it.value },
            mostUsedFetish = mostUsedFetish?.let { it.key to it.value },
            durationTrend = durationTrend,
            compareLastMonth = compareChange
        )
    }
}
