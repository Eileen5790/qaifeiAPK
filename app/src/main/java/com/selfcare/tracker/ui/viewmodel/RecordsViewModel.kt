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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecordsUiState(
    val records: List<Record> = emptyList(),
    val media: Map<String, String> = emptyMap(),
    val fetishes: Map<String, String> = emptyMap(),
    val isLoading: Boolean = true,
    val editingRecord: Record? = null,
    val showDurationDiff: Boolean = true
)

@HiltViewModel
class RecordsViewModel @Inject constructor(
    private val recordRepository: RecordRepository,
    private val mediumRepository: MediumRepository,
    private val fetishRepository: FetishRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecordsUiState())
    val uiState: StateFlow<RecordsUiState> = _uiState.asStateFlow()

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
                RecordsUiState(
                    records = records,
                    media = media.associate { it.id.toString() to it.name },
                    fetishes = fetishes.associate { it.id.toString() to it.name },
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun deleteRecord(record: Record) {
        viewModelScope.launch {
            recordRepository.deleteRecord(record)
        }
    }

    fun startEdit(record: Record) {
        _uiState.update { it.copy(editingRecord = record) }
    }

    fun cancelEdit() {
        _uiState.update { it.copy(editingRecord = null) }
    }

    fun updateRecord(record: Record) {
        viewModelScope.launch {
            recordRepository.updateRecord(record)
            _uiState.update { it.copy(editingRecord = null) }
        }
    }

    fun getMediumName(id: String): String = _uiState.value.media[id] ?: ""

    fun getFetishNames(ids: List<String>): List<String> = 
        ids.mapNotNull { _uiState.value.fetishes[it] }
}
