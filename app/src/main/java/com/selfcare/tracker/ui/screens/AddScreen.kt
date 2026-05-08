package com.selfcare.tracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.selfcare.tracker.ui.LocalTranslations
import com.selfcare.tracker.ui.components.*
import com.selfcare.tracker.ui.theme.LocalAppColors
import com.selfcare.tracker.ui.viewmodel.TimerViewModel

@Composable
fun AddScreen(
    viewModel: TimerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val t = LocalTranslations.current
    val colors = LocalAppColors.current

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            viewModel.resetSaveSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabRow(
            selectedTabIndex = if (uiState.mode == "timer") 0 else 1,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = uiState.mode == "timer",
                onClick = { viewModel.setMode("timer") },
                text = { Text(t.timer) }
            )
            Tab(
                selected = uiState.mode == "manual",
                onClick = { viewModel.setMode("manual") },
                text = { Text(t.manual) }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (uiState.mode == "timer") {
            TimerDisplay(
                seconds = uiState.elapsedSeconds,
                isRunning = uiState.isRunning
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (!uiState.isRunning && uiState.elapsedSeconds > 0) {
                Button(
                    onClick = { viewModel.saveRecord() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.success)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(t.saveRecord)
                }
            } else {
                Button(
                    onClick = { if (uiState.isRunning) viewModel.stopTimer() else viewModel.startTimer() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.isRunning) colors.danger else colors.primary
                    )
                ) {
                    Icon(
                        if (uiState.isRunning) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (uiState.isRunning) t.stopTimer else t.startTimer)
                }
            }
        } else {
            ManualInput(
                minutes = uiState.manualMinutes,
                seconds = uiState.manualSeconds,
                onMinutesChange = { viewModel.setManualMinutes(it) },
                onSecondsChange = { viewModel.setManualSeconds(it) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.saveRecord() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colors.success)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(t.saveRecord)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        MediumSelector(
            media = uiState.media,
            selectedId = uiState.selectedMediumId,
            onSelect = { viewModel.setMedium(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        FetishSelector(
            fetishes = uiState.fetishes,
            selectedIds = uiState.selectedFetishIds,
            onToggle = { viewModel.toggleFetish(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.notes,
            onValueChange = { viewModel.setNotes(it) },
            label = { Text(t.notes) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        SatisfactionSlider(
            value = uiState.satisfaction,
            onValueChange = { viewModel.setSatisfaction(it) }
        )
    }
}

@Composable
private fun TimerDisplay(seconds: Int, isRunning: Boolean) {
    val colors = LocalAppColors.current
    val minutes = seconds / 60
    val secs = seconds % 60

    Box(
        modifier = Modifier
            .size(200.dp)
            .background(colors.primary.copy(alpha = 0.1f), RoundedCornerShape(100.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = String.format("%02d:%02d", minutes, secs),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = if (isRunning) colors.primary else colors.textPrimary
        )
    }
}

@Composable
private fun ManualInput(
    minutes: Int,
    seconds: Int,
    onMinutesChange: (Int) -> Unit,
    onSecondsChange: (Int) -> Unit
) {
    val colors = LocalAppColors.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = if (minutes == 0) "" else minutes.toString(),
            onValueChange = { onMinutesChange(it.toIntOrNull() ?: 0) },
            label = { Text("分钟") },
            modifier = Modifier.width(100.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(":", fontSize = 32.sp, color = colors.textPrimary)

        Spacer(modifier = Modifier.width(16.dp))

        OutlinedTextField(
            value = if (seconds == 0) "" else seconds.toString(),
            onValueChange = { onSecondsChange((it.toIntOrNull() ?: 0).coerceIn(0, 59)) },
            label = { Text("秒") },
            modifier = Modifier.width(100.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
    }
}
