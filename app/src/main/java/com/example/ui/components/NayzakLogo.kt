package com.example.ui.components

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import com.example.ui.theme.LightBlueBorder
import com.example.ui.theme.LightBlueCard
import com.example.ui.theme.LightBlueText
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.LaurelGold
import com.example.ui.theme.LaurelGoldDark
import com.example.ui.theme.LaurelGoldLight
import com.example.ui.theme.NayzakOrange
import com.example.ui.theme.NayzakOrangeDark
import com.example.ui.theme.NayzakOrangeLight
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SpaceDark
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun NayzakGrandLogo(
    modifier: Modifier = Modifier,
    wreathSize: Dp = 230.dp
) {
    val context = LocalContext.current

    // Check if user provided base64 string
    val base64Bitmap: ImageBitmap? = remember {
        try {
            context.assets.open("logo_base64.txt").bufferedReader().use { reader ->
                val text = reader.readText().trim()
                if (text.isNotEmpty()) {
                    val clean = text.substringAfter("base64,")
                    val bytes = Base64.decode(clean, Base64.DEFAULT)
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    bmp?.asImageBitmap()
                } else null
            }
        } catch (_: Exception) {
            null
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "logo_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.985f,
        targetValue = 1.015f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val flareGlow by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flare"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (base64Bitmap != null) {
            Image(
                bitmap = base64Bitmap,
                contentDescription = "شعار نيزك NAYZAK AI",
                modifier = Modifier
                    .size(wreathSize)
                    .scale(pulseScale)
            )
        } else {
            // Pure Native Replica of Logo matching attached design
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(wreathSize)
                    .scale(pulseScale)
            ) {
                // Background laurel wreath, glowing amber ring, top 5-point star, bottom sparkle
                Canvas(modifier = Modifier.size(wreathSize)) {
                    drawNayzakWreath(flareGlow = flareGlow)
                }

                // Centered "NayzakAI" Lockup across the wreath
                NayzakTextLockup(scale = wreathSize.value / 220f)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Arabic Subtitle Tag: "من قال هذه الجملة؟"
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(LightBlueCard)
                .border(
                    width = 2.dp,
                    color = LightBlueBorder,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 18.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "من قال هذه الجملة؟",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = LightBlueText
                )
                Text(
                    text = "📜",
                    fontSize = 15.sp
                )
            }
        }
    }
}

/**
 * Draws the symmetrical Golden Laurel Wreath, Glowing Amber Ring,
 * Top 5-Pointed Star, and Bottom 4-Pointed Sparkle flare.
 */
private fun DrawScope.drawNayzakWreath(flareGlow: Float) {
    val cx = size.width / 2f
    val cy = size.height / 2f
    val radius = size.minDimension * 0.38f

    // 0. Solid Pitch-Black Circular Background inside its circle
    drawCircle(
        color = Color(0x66000000),
        radius = radius * 1.16f,
        center = Offset(cx, cy)
    )
    drawCircle(
        color = Color(0xFF000000),
        radius = radius * 1.12f,
        center = Offset(cx, cy),
        style = Fill
    )

    // 1. Soft Amber Radial Ambient Glow inside black disc
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0x33FF9100),
                Color(0x1500E5FF),
                Color.Transparent
            ),
            center = Offset(cx, cy),
            radius = radius * 1.1f
        ),
        radius = radius * 1.1f,
        center = Offset(cx, cy)
    )

    // 2. Glowing Amber Circular Ring
    drawCircle(
        color = Color(0x66FF8F00),
        radius = radius,
        center = Offset(cx, cy),
        style = Stroke(width = 5.dp.toPx())
    )
    drawCircle(
        color = Color(0xFFFFB300),
        radius = radius,
        center = Offset(cx, cy),
        style = Stroke(width = 2.5.dp.toPx())
    )
    drawCircle(
        color = Color(0xFFFFF176),
        radius = radius,
        center = Offset(cx, cy),
        style = Stroke(width = 1.dp.toPx())
    )

    // 3. Laurel Leaves (Left Branch and Right Branch)
    val leafCount = 14
    val startAngle = -68.0 // start below top star
    val endAngle = 68.0    // end above bottom spark

    for (i in 0 until leafCount) {
        val frac = i.toFloat() / (leafCount - 1)
        val deg = startAngle + frac * (endAngle - startAngle)
        val radLeft = (deg + 180.0) * (PI / 180.0)
        val radRight = (-deg) * (PI / 180.0)

        // Left leaves
        val lx = cx + radius * cos(radLeft).toFloat()
        val ly = cy + radius * sin(radLeft).toFloat()
        drawLaurelLeafPair(lx, ly, (deg + 180).toFloat(), isLeft = true)

        // Right leaves
        val rx = cx + radius * cos(radRight).toFloat()
        val ry = cy + radius * sin(radRight).toFloat()
        drawLaurelLeafPair(rx, ry, (-deg).toFloat(), isLeft = false)
    }

    // 4. Top Golden 5-Pointed 3D Star at (cx, cy - radius)
    drawTopGoldenStar(cx, cy - radius)

    // 5. Bottom 4-Pointed Radiant Diamond Flare at (cx, cy + radius)
    drawBottomDiamondSpark(cx, cy + radius, flareGlow)
}

/**
 * Draws a pair of stylized metallic 3D golden laurel leaves.
 */
private fun DrawScope.drawLaurelLeafPair(x: Float, y: Float, tangentDeg: Float, isLeft: Boolean) {
    val leafLen = 14.dp.toPx()
    val leafWidth = 6.dp.toPx()

    val path = Path().apply {
        reset()
        moveTo(0f, 0f)
        cubicTo(leafWidth, -leafLen * 0.3f, leafWidth, -leafLen * 0.7f, 0f, -leafLen)
        cubicTo(-leafWidth, -leafLen * 0.7f, -leafWidth, -leafLen * 0.3f, 0f, 0f)
        close()
    }

    // Outer Leaf
    drawContext.canvas.save()
    drawContext.canvas.translate(x, y)
    val angle = if (isLeft) tangentDeg - 35f else tangentDeg + 35f
    drawContext.canvas.rotate(angle)
    drawPath(path, color = Color(0xFFFFD54F), style = Fill)
    drawPath(path, color = Color(0xFFB45309), style = Stroke(width = 0.8.dp.toPx()))
    drawContext.canvas.restore()

    // Inner Leaf (slightly darker gold for 3D depth)
    drawContext.canvas.save()
    drawContext.canvas.translate(x, y)
    val angle2 = if (isLeft) tangentDeg - 15f else tangentDeg + 15f
    drawContext.canvas.rotate(angle2)
    drawPath(path, color = Color(0xFFFFB300), style = Fill)
    drawPath(path, color = Color(0xFF78350F), style = Stroke(width = 0.8.dp.toPx()))
    drawContext.canvas.restore()
}

/**
 * Draws a faceted golden 5-point star on top of the wreath.
 */
private fun DrawScope.drawTopGoldenStar(cx: Float, cy: Float) {
    val outerR = 12.dp.toPx()
    val innerR = 5.2.dp.toPx()

    val starPath = Path().apply {
        for (i in 0 until 10) {
            val r = if (i % 2 == 0) outerR else innerR
            val angle = (-90 + i * 36) * (PI / 180.0)
            val px = cx + r * cos(angle).toFloat()
            val py = cy + r * sin(angle).toFloat()
            if (i == 0) moveTo(px, py) else lineTo(px, py)
        }
        close()
    }

    // Base Gold Star
    drawPath(starPath, color = Color(0xFFFFD54F), style = Fill)

    // 3D Bevel Facets
    val facetPath = Path().apply {
        moveTo(cx, cy)
        for (i in 0 until 5) {
            val angle = (-90 + i * 72) * (PI / 180.0)
            lineTo(cx + outerR * cos(angle).toFloat(), cy + outerR * sin(angle).toFloat())
            moveTo(cx, cy)
        }
    }
    drawPath(facetPath, color = Color(0xFFB45309), style = Stroke(width = 1.dp.toPx()))

    // Star Glow
    drawCircle(
        color = Color(0x66FFD54F),
        radius = outerR * 1.3f,
        center = Offset(cx, cy)
    )
}

/**
 * Draws the 4-pointed radiant flare spark at the bottom.
 */
private fun DrawScope.drawBottomDiamondSpark(cx: Float, cy: Float, glowScale: Float) {
    val beamLenH = 24.dp.toPx() * glowScale
    val beamLenV = 28.dp.toPx() * glowScale
    val centerR = 6.dp.toPx()

    // Horizontal & Vertical radiant beams
    drawLine(
        brush = Brush.radialGradient(
            colors = listOf(Color.White, Color(0xFFFFB300), Color.Transparent),
            center = Offset(cx, cy),
            radius = beamLenH
        ),
        start = Offset(cx - beamLenH, cy),
        end = Offset(cx + beamLenH, cy),
        strokeWidth = 2.dp.toPx(),
        cap = StrokeCap.Round
    )

    drawLine(
        brush = Brush.radialGradient(
            colors = listOf(Color.White, Color(0xFFFF8F00), Color.Transparent),
            center = Offset(cx, cy),
            radius = beamLenV
        ),
        start = Offset(cx, cy - beamLenV),
        end = Offset(cx, cy + beamLenV),
        strokeWidth = 2.dp.toPx(),
        cap = StrokeCap.Round
    )

    // Diamond center
    val diamondPath = Path().apply {
        moveTo(cx, cy - centerR)
        lineTo(cx + centerR * 0.7f, cy)
        lineTo(cx, cy + centerR)
        lineTo(cx - centerR * 0.7f, cy)
        close()
    }
    drawPath(diamondPath, color = Color(0xFFFFF176), style = Fill)
    drawCircle(color = Color.White, radius = 2.5.dp.toPx(), center = Offset(cx, cy))
}

/**
 * Centered "NayzakAI" Lockup with 3D Depth, Golden-Orange Gradient,
 * and Cyber Electric Blue "AI" Badge with Circuit Line.
 */
@Composable
private fun NayzakTextLockup(scale: Float = 1f) {
    val titleFontSize = (32 * scale).sp
    val aiFontSize = (22 * scale).sp

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        // "Nayzak" with 3D Bevel Layers
        Box(contentAlignment = Alignment.Center) {
            // Layer 1: Deep Drop Shadow (Offset +3, +4)
            Text(
                text = "Nayzak",
                fontSize = titleFontSize,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                color = Color(0xFF140200),
                modifier = Modifier.offset(x = (2.5 * scale).dp, y = (4 * scale).dp)
            )

            // Layer 2: 3D Warm Dark Orange Bevel (Offset +1.5, +2)
            Text(
                text = "Nayzak",
                fontSize = titleFontSize,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                color = Color(0xFF992600),
                modifier = Modifier.offset(x = (1.5 * scale).dp, y = (2 * scale).dp)
            )

            // Layer 3: Vibrant Orange Rim / Stroke Layer
            Text(
                text = "Nayzak",
                fontSize = titleFontSize,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                color = Color(0xFFFF5722),
                modifier = Modifier.offset(y = (0.5 * scale).dp)
            )

            // Layer 4: Front Face with Golden-Yellow to Bright Orange Gradient
            Text(
                text = "Nayzak",
                fontSize = titleFontSize,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                style = TextStyle(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0xFFFFF59D), // Light canary yellow at top
                            Color(0xFFFFB300), // Amber gold
                            Color(0xFFFF6D00)  // Vivid orange at baseline
                        )
                    )
                )
            )
        }

        Spacer(modifier = Modifier.width((4 * scale).dp))

        // "AI" Cybernetic Neon Box
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape((6 * scale).dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF06182B),
                            Color(0xFF020C17)
                        )
                    )
                )
                .border(
                    width = (2 * scale).dp,
                    color = CyberBlue,
                    shape = RoundedCornerShape((6 * scale).dp)
                )
                .shadow((8 * scale).dp, RoundedCornerShape((6 * scale).dp), spotColor = CyberBlue)
                .padding(horizontal = (7 * scale).dp, vertical = (2 * scale).dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "AI",
                    fontSize = aiFontSize,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    style = TextStyle(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color(0xFFE0F7FA),
                                CyberBlue,
                                Color(0xFF0288D1)
                            )
                        )
                    )
                )
            }
        }
    }
}

@Composable
fun NayzakCompactHeader(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        // Small wreath icon
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(38.dp)
        ) {
            Canvas(modifier = Modifier.size(38.dp)) {
                drawNayzakWreath(flareGlow = 1f)
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Nayzak",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            style = TextStyle(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFF59D),
                        Color(0xFFFFB300),
                        Color(0xFFFF6D00)
                    )
                )
            )
        )

        Spacer(modifier = Modifier.width(4.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFF06182B))
                .border(1.2.dp, CyberBlue, RoundedCornerShape(4.dp))
                .padding(horizontal = 4.dp, vertical = 1.dp)
        ) {
            Text(
                text = "AI",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = CyberBlue
            )
        }
    }
}
