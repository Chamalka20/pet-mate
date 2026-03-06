package uk.ac.wlv.petmate.components
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun WaveBackground(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFE3E9F5), // Same as border by default
    waveHeight: Dp = 16.dp,
    waveCount: Int = 12
) {
    // Generate random wave patterns that stay consistent during recomposition
    val topWavePattern = remember { List(waveCount) { Random.nextFloat() * 0.6f + 0.7f } }
    val bottomWavePattern = remember { List(waveCount) { Random.nextFloat() * 0.6f + 0.7f } }

    Canvas(modifier = modifier.fillMaxSize()) {
        val waveHeightPx = waveHeight.toPx()

        fun createOutwardWavePath(
            baseY: Float,
            isTop: Boolean,
            wavePattern: List<Float>
        ): Path {
            val path = Path()
            val step = size.width / waveCount

            path.moveTo(0f, baseY)

            for (i in 1..waveCount) {
                val controlX = step * (i - 0.5f)
                val endX = step * i

                val randomAmplitude = wavePattern[i - 1]
                val offset = waveHeightPx * randomAmplitude * if (i % 2 == 0) 1f else -1f
                val direction = if (isTop) -1f else 1f

                path.quadraticTo(
                    controlX,
                    baseY + (offset * direction),
                    endX,
                    baseY
                )
            }

            return path
        }

        // Draw top wave border
        val topWavePath = createOutwardWavePath(0f, true, topWavePattern).apply {
            lineTo(size.width, waveHeightPx)
            lineTo(0f, waveHeightPx)
            close()
        }
        drawPath(topWavePath, backgroundColor)

        // Draw bottom wave border
        val bottomWavePath = createOutwardWavePath(size.height, false, bottomWavePattern).apply {
            lineTo(size.width, size.height - waveHeightPx)
            lineTo(0f, size.height - waveHeightPx)
            close()
        }
        drawPath(bottomWavePath, backgroundColor)

        // Draw main content background
        drawRect(
            color = backgroundColor,
            topLeft = Offset(0f, waveHeightPx),
            size = Size(size.width, size.height - waveHeightPx * 2)
        )
    }
}