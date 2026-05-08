package com.selfcare.tracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.selfcare.tracker.data.model.Record
import com.selfcare.tracker.ui.LocalTranslations
import com.selfcare.tracker.ui.components.RecordItem
import com.selfcare.tracker.ui.theme.LocalAppColors
import com.selfcare.tracker.ui.viewmodel.RecordsViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RecordsScreen(
    viewModel: RecordsViewModel = hiltViewModel(),
    onEditRecord: (Record) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val t = LocalTranslations.current
    val colors = LocalAppColors.current

    var recordToDelete by remember { mutableStateOf<Record?>(null) }

    if (uiState.records.isEmpty() && !uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = t.noRecords,
                style = MaterialTheme.typography.bodyLarge,
                color = colors.textSecondary
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(uiState.records) { record ->
            val prevRecord = uiState.records.getOrNull(uiState.records.indexOf(record) + 1)
            val durationDiff = if (prevRecord != null) {
                val diff = record.duration - prevRecord.duration
                val sign = if (diff >= 0) "+" else ""
                val minutes = kotlin.math.abs(diff) / 60
                val seconds = kotlin.math.abs(diff) % 60
                "$sign${minutes}分${seconds}秒"
            } else null

            RecordItem(
                date = formatDateTime(record.startTime),
                duration = formatDuration(record.duration),
                medium = viewModel.getMediumName(record.mediumId),
                fetishes = viewModel.getFetishNames(record.getFetishList()).joinToString(", "),
                durationDiff = durationDiff,
                onEdit = { onEditRecord(record) },
                onDelete = { recordToDelete = record }
            )
        }
    }

    recordToDelete?.let { record ->
        AlertDialog(
            onDismissRequest = { recordToDelete = null },
            title = { Text(t.confirm) },
            text = { Text(t.deleteConfirm) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteRecord(record)
                        recordToDelete = null
                    }
                ) {
                    Text(t.delete, color = colors.danger)
                }
            },
            dismissButton = {
                TextButton(onClick = { recordToDelete = null }) {
                    Text(t.cancel)
                }
            }
        )
    }
}

private fun formatDateTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

private fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return "${minutes}分${secs}秒"
}
