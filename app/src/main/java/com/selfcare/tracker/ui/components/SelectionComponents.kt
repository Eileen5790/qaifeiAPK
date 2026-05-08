package com.selfcare.tracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.selfcare.tracker.data.model.Fetish
import com.selfcare.tracker.data.model.Medium
import com.selfcare.tracker.ui.theme.LocalAppColors

@Composable
fun MediumSelector(
    media: List<Medium>,
    selectedId: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current

    Column(modifier = modifier) {
        Text(
            text = "媒介",
            style = MaterialTheme.typography.labelLarge,
            color = colors.textSecondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(media) { medium ->
                FilterChip(
                    selected = medium.id.toString() == selectedId,
                    onClick = { onSelect(medium.id.toString()) },
                    label = { Text(medium.name) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colors.primary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
    }
}

@Composable
fun FetishSelector(
    fetishes: List<Fetish>,
    selectedIds: List<String>,
    onToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current

    Column(modifier = modifier) {
        Text(
            text = "性癖（可多选）",
            style = MaterialTheme.typography.labelLarge,
            color = colors.textSecondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(fetishes) { fetish ->
                FilterChip(
                    selected = selectedIds.contains(fetish.id.toString()),
                    onClick = { onToggle(fetish.id.toString()) },
                    label = { Text(fetish.name) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colors.secondary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
    }
}

@Composable
fun SatisfactionSlider(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "满意度评分",
                style = MaterialTheme.typography.labelLarge,
                color = colors.textSecondary
            )
            Text(
                text = "$value 分",
                style = MaterialTheme.typography.labelLarge,
                color = colors.primary
            )
        }
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = 0f..10f,
            steps = 9,
            colors = SliderDefaults.colors(
                thumbColor = colors.primary,
                activeTrackColor = colors.primary
            )
        )
    }
}
