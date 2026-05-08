package com.selfcare.tracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun PieChart(
    data: List<Pair<String, Int>>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Box(modifier = modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
            Text("暂无数据", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        return
    }

    val total = data.sumOf { it.second }.toFloat()
    if (total == 0f) {
        Box(modifier = modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
            Text("暂无数据", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        return
    }

    Row(modifier = modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier.size(160.dp)
        ) {
            var startAngle = -90f
            data.forEachIndexed { index, (_, value) ->
                val sweepAngle = (value / total) * 360f
                drawArc(
                    color = colors[index % colors.size],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    size = Size(size.width, size.height)
                )
                startAngle += sweepAngle
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            data.forEachIndexed { index, (label, value) ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(12.dp).background(
                            colors[index % colors.size],
                            RoundedCornerShape(2.dp)
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$label (${(value / total * 100).toInt()}%)",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun BarChart(
    data: List<Pair<String, Int>>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Box(modifier = modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
            Text("暂无数据", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        return
    }

    val maxValue = data.maxOfOrNull { it.second } ?: 1

    Canvas(modifier = modifier.fillMaxWidth().height(200.dp)) {
        val barWidth = size.width / data.size * 0.6f
        val spacing = size.width / data.size * 0.4f

        data.forEachIndexed { index, (_, value) ->
            val barHeight = (value.toFloat() / maxValue) * size.height * 0.8f
            val x = index * (barWidth + spacing) + spacing / 2
            val y = size.height - barHeight

            drawRect(
                color = Color(0xFF6366F1),
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight)
            )
        }
    }

    Row(modifier = modifier.fillMaxWidth().padding(top = 8.dp)) {
        data.forEach { (label, _) ->
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun LineChart(
    data: List<Pair<String, Int>>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Box(modifier = modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
            Text("暂无数据", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        return
    }

    val maxValue = data.maxOfOrNull { it.second }?.toFloat() ?: 1f
    val minValue = data.minOfOrNull { it.second }?.toFloat() ?: 0f
    val range = maxOf(maxValue - minValue, 1f)

    Canvas(modifier = modifier.fillMaxWidth().height(200.dp)) {
        val stepX = size.width / (data.size - 1).coerceAtLeast(1)

        val path = Path()
        data.forEachIndexed { index, (_, value) ->
            val x = index * stepX
            val y = size.height - ((value - minValue) / range) * size.height * 0.8f - size.height * 0.1f

            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(path, Color(0xFF6366F1), style = Stroke(width = 3f))

        data.forEachIndexed { index, (_, value) ->
            val x = index * stepX
            val y = size.height - ((value - minValue) / range) * size.height * 0.8f - size.height * 0.1f
            drawCircle(Color(0xFF4F46E5), radius = 6f, center = Offset(x, y))
        }
    }

    Row(modifier = modifier.fillMaxWidth().padding(top = 8.dp)) {
        data.forEach { (label, _) ->
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
