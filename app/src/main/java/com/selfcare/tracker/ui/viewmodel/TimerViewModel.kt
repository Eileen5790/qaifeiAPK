package com.selfcare.tracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selfcare.tracker.data.model.Fetish
import com.selfcare.tracker.data.model.Medium
import com.selfcare.tracker.data.model.Record
import com.selfcare.tracker.data.repository.FetishRepository
import com.selfcare.tracker.data.repository.MediumRepository
import com.selfcare.tracker.data.repository.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TimerUiState(
    val isRunning: Boolean = false,
    val elapsedSeconds: Int = 0,
    val startTimestamp: Long = 0L,
    val selectedMediumId: String = "",
    val selectedFetishIds: List<String> = emptyList(),
    val notes: String = "",
    val satisfaction: Int = 5,
    val mode: String = "timer",
    val manualMinutes: Int = 0,
    val manualSeconds: Int = 0,
    val media: List<Medium> = emptyList(),
    val fetishes: List<Fetish> = emptyList(),
    val saveSuccess: Boolean = false
)

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val recordRepository: RecordRepository,
    private val mediumRepository: MediumRepository,
    private val fetishRepository: FetishRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            mediumRepository.getAllMedia().collect { mediaList ->
                _uiState.update { it.copy(
                    media = mediaList,
                    selectedMediumId = it.selectedMediumId.ifEmpty { mediaList.firstOrNull()?.id?.toString() ?: "" }
                )}
            }
        }
        viewModelScope.launch {
            fetishRepository.getAllFetishes().collect { fetishList ->
                _uiState.update { it.copy(fetishes = fetishList) }
            }
        }
    }

    fun startTimer() {
        val startTime = System.currentTimeMillis()
        _uiState.update { it.copy(isRunning = true, elapsedSeconds = 0, startTimestamp = startTime) }
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _uiState.update { it.copy(elapsedSeconds = it.elapsedSeconds + 1) }
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        _uiState.update { it.copy(isRunning = false) }
    }

    fun setMode(mode: String) {
        _uiState.update { it.copy(mode = mode) }
    }

    fun setMedium(mediumId: String) {
        _uiState.update { it.copy(selectedMediumId = mediumId) }
    }

    fun toggleFetish(fetishId: String) {
        _uiState.update { state ->
            val currentIds = state.selectedFetishIds.toMutableList()
            if (currentIds.contains(fetishId)) {
                currentIds.remove(fetishId)
            } else {
                currentIds.add(fetishId)
            }
            state.copy(selectedFetishIds = currentIds)
        }
    }

    fun setNotes(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun setSatisfaction(satisfaction: Int) {
        _uiState.update { it.copy(satisfaction = satisfaction) }
    }

    fun setManualMinutes(minutes: Int) {
        _uiState.update { it.copy(manualMinutes = minutes) }
    }

    fun setManualSeconds(seconds: Int) {
        _uiState.update { it.copy(manualSeconds = seconds) }
    }

    fun saveRecord() {
        viewModelScope.launch {
            val state = _uiState.value
            val startTime: Long
            val duration: Int

            if (state.mode == "timer") {
                startTime = state.startTimestamp
                duration = state.elapsedSeconds
            } else {
                startTime = System.currentTimeMillis()
                duration = state.manualMinutes * 60 + state.manualSeconds
            }

            if (duration <= 0) return@launch

            val record = Record(
                startTime = startTime,
                duration = duration,
                mediumId = state.selectedMediumId,
                fetishIds = Record.createFetishString(state.selectedFetishIds),
                notes = state.notes,
                satisfaction = state.satisfaction
            )

            recordRepository.insertRecord(record)
            _uiState.update { it.copy(
                isRunning = false,
                elapsedSeconds = 0,
                notes = "",
                satisfaction = 5,
                selectedFetishIds = emptyList(),
                saveSuccess = true
            )}
        }
    }

    fun resetSaveSuccess() {
        _uiState.update { it.copy(saveSuccess = false) }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
