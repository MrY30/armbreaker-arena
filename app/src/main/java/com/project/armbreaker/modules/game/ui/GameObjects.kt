package com.project.armbreaker.modules.game.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.project.armbreaker.R
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
    val backColors = listOf(
        Color(0xFFD91A1A),
        Color(0xFFB41C1C),
        Color(0xFF8F1D1E),
        Color(0xFF6B1F20),
    )

    val foreColors = listOf(
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

//Restart Icon
public val Restart: ImageVector
    get() {
        if (_Restart != null) {
            return _Restart!!
        }
        _Restart = ImageVector.Builder(
            name = "DebugRestart",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(12.75f, 8f)
                arcToRelative(4.5f, 4.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, -8.61f, 1.834f)
                lineToRelative(-1.391f, 0.565f)
                arcTo(6.001f, 6.001f, 0f, isMoreThanHalf = false, isPositiveArc = false, 14.25f, 8f)
                arcTo(6f, 6f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.5f, 4.334f)
                verticalLineTo(2.5f)
                horizontalLineTo(2f)
                verticalLineToRelative(4f)
                lineToRelative(0.75f, 0.75f)
                horizontalLineToRelative(3.5f)
                verticalLineToRelative(-1.5f)
                horizontalLineTo(4.352f)
                arcTo(4.5f, 4.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 12.75f, 8f)
                close()
            }
        }.build()
        return _Restart!!
    }
private var _Restart: ImageVector? = null

//Exit Icon
public val Exit: ImageVector
    get() {
        if (_Exit != null) {
            return _Exit!!
        }
        _Exit = ImageVector.Builder(
            name = "Exit",
            defaultWidth = 15.dp,
            defaultHeight = 15.dp,
            viewportWidth = 15f,
            viewportHeight = 15f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(3f, 1f)
                curveTo(2.4477f, 1f, 2f, 1.4477f, 2f, 2f)
                verticalLineTo(13f)
                curveTo(2f, 13.5523f, 2.4477f, 14f, 3f, 14f)
                horizontalLineTo(10.5f)
                curveTo(10.7761f, 14f, 11f, 13.7761f, 11f, 13.5f)
                curveTo(11f, 13.2239f, 10.7761f, 13f, 10.5f, 13f)
                horizontalLineTo(3f)
                verticalLineTo(2f)
                lineTo(10.5f, 2f)
                curveTo(10.7761f, 2f, 11f, 1.7761f, 11f, 1.5f)
                curveTo(11f, 1.2239f, 10.7761f, 1f, 10.5f, 1f)
                horizontalLineTo(3f)
                close()
                moveTo(12.6036f, 4.89645f)
                curveTo(12.4083f, 4.7012f, 12.0917f, 4.7012f, 11.8964f, 4.8964f)
                curveTo(11.7012f, 5.0917f, 11.7012f, 5.4083f, 11.8964f, 5.6036f)
                lineTo(13.2929f, 7f)
                horizontalLineTo(6.5f)
                curveTo(6.2239f, 7f, 6f, 7.2239f, 6f, 7.5f)
                curveTo(6f, 7.7761f, 6.2239f, 8f, 6.5f, 8f)
                horizontalLineTo(13.2929f)
                lineTo(11.8964f, 9.39645f)
                curveTo(11.7012f, 9.5917f, 11.7012f, 9.9083f, 11.8964f, 10.1036f)
                curveTo(12.0917f, 10.2988f, 12.4083f, 10.2988f, 12.6036f, 10.1036f)
                lineTo(14.8536f, 7.85355f)
                curveTo(15.0488f, 7.6583f, 15.0488f, 7.3417f, 14.8536f, 7.1464f)
                lineTo(12.6036f, 4.89645f)
                close()
            }
        }.build()
        return _Exit!!
    }

private var _Exit: ImageVector? = null

@Composable
fun GameButton(
    buttonImage: Painter = painterResource(R.drawable.green_button),
    iconPainter: Painter? = null, // For drawable icons
    iconVector: ImageVector? = null, // For Material Icons
    size:Int = 80,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Background image
        Image(
            painter = buttonImage,
            contentDescription = "Button Background",
            modifier = Modifier.fillMaxSize()
        )

        // Conditionally display an icon if either painter or vector is provided
        when {
            iconPainter != null -> Icon(
                painter = iconPainter,
                contentDescription = "Button Icon",
                modifier = Modifier
                    .size((size * 0.5).dp)
                    .offset(y = (-10).dp)
            )
            iconVector != null -> Icon(
                imageVector = iconVector,
                contentDescription = "Button Icon",
                modifier = Modifier
                    .size((size * 0.5).dp)
                    .offset(y = (-10).dp)
            )
        }
    }
}

//@Preview(showBackground = false)
//@Composable
//fun ObjectPreview(){
//    GameButton (iconVector = Exit){
//
//    }
//}

