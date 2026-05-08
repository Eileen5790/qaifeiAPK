package com.selfcare.tracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.selfcare.tracker.data.model.Fetish
import com.selfcare.tracker.data.model.Medium
import com.selfcare.tracker.ui.LocalTranslations
import com.selfcare.tracker.ui.theme.LocalAppColors
import com.selfcare.tracker.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val t = LocalTranslations.current
    val colors = LocalAppColors.current

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showMediumDialog by remember { mutableStateOf(false) }
    var showFetishDialog by remember { mutableStateOf(false) }
    var newMediumName by remember { mutableStateOf("") }
    var newFetishName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = t.settingsTitle,
            style = MaterialTheme.typography.headlineMedium,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = t.language,
            style = MaterialTheme.typography.titleMedium,
            color = colors.textPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth().clickable { showLanguageDialog = true },
            colors = CardDefaults.cardColors(containerColor = colors.surface)
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(if (uiState.lang == "zh") t.chinese else t.english)
                Icon(Icons.Default.ChevronRight, contentDescription = null)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = t.theme,
            style = MaterialTheme.typography.titleMedium,
            color = colors.textPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth().clickable { showThemeDialog = true },
            colors = CardDefaults.cardColors(containerColor = colors.surface)
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(if (uiState.theme == "light") t.lightMode else t.darkMode)
                Icon(Icons.Default.ChevronRight, contentDescription = null)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = t.mediumManagement,
            style = MaterialTheme.typography.titleMedium,
            color = colors.textPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = colors.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                uiState.medium.forEach { medium ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(medium.name)
                        IconButton(onClick = { viewModel.deleteMedium(medium) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = colors.danger)
                        }
                    }
                }
                OutlinedTextField(
                    value = newMediumName,
                    onValueChange = { newMediumName = it },
                    label = { Text(t.addMedium) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (newMediumName.isNotBlank()) {
                                    viewModel.addMedium(newMediumName)
                                    newMediumName = ""
                                }
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = t.fetishManagement,
            style = MaterialTheme.typography.titleMedium,
            color = colors.textPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = colors.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                uiState.fetishes.forEach { fetish ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(fetish.name)
                        IconButton(onClick = { viewModel.deleteFetish(fetish) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = colors.danger)
                        }
                    }
                }
                OutlinedTextField(
                    value = newFetishName,
                    onValueChange = { newFetishName = it },
                    label = { Text(t.addFetish) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (newFetishName.isNotBlank()) {
                                    viewModel.addFetish(newFetishName)
                                    newFetishName = ""
                                }
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = t.about,
            style = MaterialTheme.typography.titleMedium,
            color = colors.textPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = colors.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(t.version)
                    Text("1.0.0")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(t.appDescription, style = MaterialTheme.typography.bodySmall)
            }
        }
    }

    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text(t.language) },
            text = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable {
                            viewModel.setLanguage("zh")
                            showLanguageDialog = false
                        }.padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = uiState.lang == "zh", onClick = {
                            viewModel.setLanguage("zh")
                            showLanguageDialog = false
                        })
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(t.chinese)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable {
                            viewModel.setLanguage("en")
                            showLanguageDialog = false
                        }.padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = uiState.lang == "en", onClick = {
                            viewModel.setLanguage("en")
                            showLanguageDialog = false
                        })
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(t.english)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text(t.close)
                }
            }
        )
    }

    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text(t.theme) },
            text = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable {
                            viewModel.setTheme("light")
                            showThemeDialog = false
                        }.padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = uiState.theme == "light", onClick = {
                            viewModel.setTheme("light")
                            showThemeDialog = false
                        })
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(t.lightMode)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable {
                            viewModel.setTheme("dark")
                            showThemeDialog = false
                        }.padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = uiState.theme == "dark", onClick = {
                            viewModel.setTheme("dark")
                            showThemeDialog = false
                        })
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(t.darkMode)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text(t.close)
                }
            }
        )
    }
}
