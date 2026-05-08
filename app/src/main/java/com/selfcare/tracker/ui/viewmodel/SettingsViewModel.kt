package com.selfcare.tracker.ui.viewmodel

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selfcare.tracker.data.model.*
import com.selfcare.tracker.data.repository.FetishRepository
import com.selfcare.tracker.data.repository.MediumRepository
import com.selfcare.tracker.data.repository.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "settings")

data class SettingsUiState(
    val lang: String = "zh",
    val theme: String = "light",
    val colorTheme: String = "default",
    val medium: List<Medium> = emptyList(),
    val fetishes: List<Fetish> = emptyList(),
    val statsConfig: StatsConfig = StatsConfig(),
    val uiConfig: UIConfig = UIConfig(),
    val exportData: ExportData? = null,
    val importSuccess: Boolean = false,
    val exportSuccess: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val recordRepository: RecordRepository,
    private val mediumRepository: MediumRepository,
    private val fetishRepository: FetishRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val langKey = stringPreferencesKey("lang")
    private val themeKey = stringPreferencesKey("theme")
    private val colorThemeKey = stringPreferencesKey("colorTheme")
    private val statsConfigKey = stringPreferencesKey("statsConfig")
    private val uiConfigKey = stringPreferencesKey("uiConfig")

    init {
        loadSettings()
        loadData()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            context.dataStore.data.collect { prefs ->
                _uiState.update { state ->
                    state.copy(
                        lang = prefs[langKey] ?: "zh",
                        theme = prefs[themeKey] ?: "light",
                        colorTheme = prefs[colorThemeKey] ?: "default"
                    )
                }
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            mediumRepository.getAllMedia().collect { medium ->
                _uiState.update { it.copy(medium = medium) }
            }
        }
        viewModelScope.launch {
            fetishRepository.getAllFetishes().collect { fetishes ->
                _uiState.update { it.copy(fetishes = fetishes) }
            }
        }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            context.dataStore.edit { prefs ->
                prefs[langKey] = lang
            }
            _uiState.update { it.copy(lang = lang) }
        }
    }

    fun setTheme(theme: String) {
        viewModelScope.launch {
            context.dataStore.edit { prefs ->
                prefs[themeKey] = theme
            }
            _uiState.update { it.copy(theme = theme) }
        }
    }

    fun setColorTheme(colorTheme: String) {
        viewModelScope.launch {
            context.dataStore.edit { prefs ->
                prefs[colorThemeKey] = colorTheme
            }
            _uiState.update { it.copy(colorTheme = colorTheme) }
        }
    }

    fun addMedium(name: String) {
        viewModelScope.launch {
            mediumRepository.insertMedium(Medium(name = name))
        }
    }

    fun deleteMedium(medium: Medium) {
        viewModelScope.launch {
            mediumRepository.deleteMedium(medium)
        }
    }

    fun addFetish(name: String) {
        viewModelScope.launch {
            fetishRepository.insertFetish(Fetish(name = name))
        }
    }

    fun deleteFetish(fetish: Fetish) {
        viewModelScope.launch {
            fetishRepository.deleteFetish(fetish)
        }
    }

    suspend fun exportAllData(): ExportData {
        val records = mutableListOf<Record>()
        recordRepository.getAllRecords().first().let { records.addAll(it) }
        val media = mutableListOf<Medium>()
        mediumRepository.getAllMedia().first().let { media.addAll(it) }
        val fetishes = mutableListOf<Fetish>()
        fetishRepository.getAllFetishes().first().let { fetishes.addAll(it) }

        return ExportData(
            records = records,
            media = media,
            fetishes = fetishes,
            settings = AppSettings(
                lang = _uiState.value.lang,
                theme = _uiState.value.theme,
                colorTheme = _uiState.value.colorTheme
            )
        )
    }

    fun importData(data: ExportData) {
        viewModelScope.launch {
            recordRepository.deleteAllRecords()
            mediumRepository.deleteAllMedia()
            fetishRepository.deleteAllFetishes()

            data.records.forEach { recordRepository.insertRecord(it) }
            data.media.forEach { mediumRepository.insertMedium(it) }
            data.fetishes.forEach { fetishRepository.insertFetish(it) }

            data.settings.let { settings ->
                context.dataStore.edit { prefs ->
                    prefs[langKey] = settings.lang
                    prefs[themeKey] = settings.theme
                    prefs[colorThemeKey] = settings.colorTheme
                }
                _uiState.update { it.copy(
                    lang = settings.lang,
                    theme = settings.theme,
                    colorTheme = settings.colorTheme,
                    importSuccess = true
                )}
            }
        }
    }

    fun resetImportSuccess() {
        _uiState.update { it.copy(importSuccess = false) }
    }
}
