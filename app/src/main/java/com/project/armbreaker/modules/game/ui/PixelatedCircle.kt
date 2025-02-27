package com.project.armbreaker.modules.game.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun PixelatedCircle(
    modifier: Modifier = Modifier,
    circleColor: Color = Color.Red,
    gridSize: Int = 16 // Number of pixels across the width/height
) {
    Canvas(modifier = modifier.size(200.dp)) {
        val pixelSize = size.minDimension / gridSize
        val radius = gridSize / 2f

        for (x in 0 until gridSize) {
            for (y in 0 until gridSize) {
                val dx = x - radius
                val dy = y - radius
                if (dx * dx + dy * dy <= radius * radius) {
                    // Adding shadow effect by varying brightness randomly
                    val brightnessFactor = Random.nextFloat() * 0.3f + 0.7f
                    val shadedColor = circleColor.copy(alpha = brightnessFactor)
                    drawRect(
                        color = shadedColor,
                        topLeft = androidx.compose.ui.geometry.Offset(x * pixelSize, y * pixelSize),
                        size = androidx.compose.ui.geometry.Size(pixelSize, pixelSize)
                    )
                }
            }
        }
    }
}

@Composable
fun ProgressBar(progress: Float, height: Int = 25, modifier: Modifier = Modifier) {
    val foreColors = listOf(
        Color(0xFFFFD700),
        Color(0xFFFFA500),
        Color(0xFFFFFF00),
        Color(0xFFDAA520)
    )

    val backColors = listOf(
        Color(0xFF1D381E),
        Color(0xFF2B7735),
        Color(0xFF66C536),
        Color(0xFFA1CA21)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
            .clip(RoundedCornerShape(50)) // Ensures capsule shape for both background and border
            .background(brush = Brush.horizontalGradient(backColors))
            .border(3.dp, Color.Black, shape = RoundedCornerShape(50)) // Capsule border
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .height(height.dp)
                .clip(RoundedCornerShape(50)) // Ensures capsule shape for foreground
                .background(brush = Brush.horizontalGradient(foreColors))
        )
    }
}




@Preview(showBackground = false)
@Composable
fun PixelatedCirclePreview() {
    ProgressBar(0.7f)
}