package com.example.subify.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.subify.theme.NeonCyan
import com.example.subify.theme.NeonPurple

@Composable
fun NeonCategoryChart(
    data: Map<String, Double>,
    modifier: Modifier = Modifier
) {
    val categories = data.keys.toList()
    val values = data.values.toList()
    val maxValue = (values.maxOrNull() ?: 1.0).coerceAtLeast(1.0)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        val width = size.width
        val height = size.height
        val paddingBottom = 40.dp.toPx()
        val paddingTop = 30.dp.toPx()
        val chartHeight = height - paddingBottom - paddingTop

        val barCount = categories.size.coerceAtLeast(1)
        val spaceBetween = 20.dp.toPx()
        val totalSpacing = spaceBetween * (barCount + 1)
        val barWidth = (width - totalSpacing) / barCount

        val textPaint = Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 10.sp.toPx()
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        val labelPaint = Paint().apply {
            color = android.graphics.Color.parseColor("#8F94A5")
            textSize = 10.sp.toPx()
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        for (i in 0 until barCount) {
            val category = categories.getOrNull(i) ?: ""
            val value = values.getOrNull(i) ?: 0.0
            val percentage = value / maxValue
            val barHeight = (chartHeight * percentage).toFloat().coerceAtLeast(10f)

            val x = spaceBetween + i * (barWidth + spaceBetween) + barWidth / 2
            val left = x - barWidth / 2
            val right = x + barWidth / 2
            val bottom = height - paddingBottom
            val top = bottom - barHeight

            val gradient = Brush.verticalGradient(
                colors = listOf(NeonPurple, NeonCyan)
            )

            // Draw shadow/glow behind the bar
            val glowColor = NeonPurple.copy(alpha = 0.2f)
            drawRoundRect(
                color = glowColor,
                topLeft = Offset(left - 4.dp.toPx(), top - 4.dp.toPx()),
                size = Size(barWidth + 8.dp.toPx(), barHeight + 4.dp.toPx()),
                cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
            )

            // Draw main bar
            drawRoundRect(
                brush = gradient,
                topLeft = Offset(left, top),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
            )

            // Draw value text at top
            val formattedValue = String.format("%.0f", value)
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(
                    formattedValue,
                    x,
                    top - 8.dp.toPx(),
                    textPaint
                )
            }

            // Draw label at bottom
            val truncatedLabel = if (category.length > 8) category.take(6) + ".." else category
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(
                    truncatedLabel,
                    x,
                    height - 12.dp.toPx(),
                    labelPaint
                )
            }
        }
    }
}
