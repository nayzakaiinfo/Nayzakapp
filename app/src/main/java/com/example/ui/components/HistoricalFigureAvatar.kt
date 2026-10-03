package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.data.HistoricalFigure
import kotlin.math.cos
import kotlin.math.sin

enum class AvatarState {
    IDLE,
    CORRECT,
    WRONG
}

@Composable
fun HistoricalFigureAvatar(
    figure: HistoricalFigure,
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    state: AvatarState = AvatarState.IDLE,
    showBorderGlow: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "avatar_pulse")
    val bobbingOffset by infiniteTransition.animateFloat(
        initialValue = if (state == AvatarState.CORRECT) -4f else 0f,
        targetValue = if (state == AvatarState.CORRECT) 4f else if (state == AvatarState.WRONG) 2f else -2f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (state == AvatarState.CORRECT) 400 else 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bobbing"
    )

    val borderColor = when (state) {
        AvatarState.CORRECT -> Color(0xFF10B981)
        AvatarState.WRONG -> Color(0xFFEF4444)
        AvatarState.IDLE -> figure.color
    }

    val glowColor = when (state) {
        AvatarState.CORRECT -> Color(0x6610B981)
        AvatarState.WRONG -> Color(0x66EF4444)
        AvatarState.IDLE -> figure.color.copy(alpha = 0.35f)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .then(
                if (showBorderGlow) {
                    Modifier
                        .shadow(12.dp, CircleShape, spotColor = glowColor, ambientColor = glowColor)
                        .border(3.dp, Brush.radialGradient(listOf(borderColor, borderColor.copy(alpha = 0.6f))), CircleShape)
                } else {
                    Modifier.border(1.5.dp, borderColor.copy(alpha = 0.5f), CircleShape)
                }
            )
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF242744),
                        Color(0xFF141426),
                        Color(0xFF0D0C1A)
                    )
                )
            )
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val cx = w / 2f
            val cy = w / 2f + bobbingOffset

            when (figure) {
                // 1. Philosophy & Wisdom
                HistoricalFigure.SOCRATES -> drawSocrates(cx, cy, w, state)
                HistoricalFigure.PLATO -> drawPlato(cx, cy, w, state)
                HistoricalFigure.ARISTOTLE -> drawAristotle(cx, cy, w, state)
                HistoricalFigure.DESCARTES -> drawDescartes(cx, cy, w, state)
                HistoricalFigure.AVICENNA -> drawAvicenna(cx, cy, w, state)
                HistoricalFigure.CONFUCIUS -> drawConfucius(cx, cy, w, state)
                HistoricalFigure.SENECA -> drawSeneca(cx, cy, w, state)
                HistoricalFigure.MARCUS_AURELIUS -> drawMarcusAurelius(cx, cy, w, state)

                // 2. Science & Discovery
                HistoricalFigure.EINSTEIN -> drawEinstein(cx, cy, w, state)
                HistoricalFigure.NEWTON -> drawNewton(cx, cy, w, state)
                HistoricalFigure.GALILEO -> drawGalileo(cx, cy, w, state)
                HistoricalFigure.MARIE_CURIE -> drawMarieCurie(cx, cy, w, state)
                HistoricalFigure.AL_KHWARIZMI -> drawAlKhwarizmi(cx, cy, w, state)
                HistoricalFigure.IBN_AL_HAYTHAM -> drawIbnAlHaytham(cx, cy, w, state)
                HistoricalFigure.TESLA -> drawTesla(cx, cy, w, state)
                HistoricalFigure.EDISON -> drawEdison(cx, cy, w, state)

                // 3. Literature & Arts
                HistoricalFigure.SHAKESPEARE -> drawShakespeare(cx, cy, w, state)
                HistoricalFigure.AL_MUTANABBI -> drawAlMutanabbi(cx, cy, w, state)
                HistoricalFigure.NAGUIB_MAHFOUZ -> drawNaguibMahfouz(cx, cy, w, state)
                HistoricalFigure.KHALIL_GIBRAN -> drawKhalilGibran(cx, cy, w, state)
                HistoricalFigure.DOSTOEVSKY -> drawDostoevsky(cx, cy, w, state)
                HistoricalFigure.DA_VINCI -> drawDaVinci(cx, cy, w, state)
                HistoricalFigure.BEETHOVEN -> drawBeethoven(cx, cy, w, state)

                // 4. History & Leadership
                HistoricalFigure.NAPOLEON -> drawNapoleon(cx, cy, w, state)
                HistoricalFigure.ALEXANDER_GREAT -> drawAlexanderGreat(cx, cy, w, state)
                HistoricalFigure.JULIUS_CAESAR -> drawJuliusCaesar(cx, cy, w, state)
                HistoricalFigure.SALADIN -> drawSaladin(cx, cy, w, state)
                HistoricalFigure.SUN_TZU -> drawSunTzu(cx, cy, w, state)

                // 5. Psychology & Mind
                HistoricalFigure.FREUD -> drawFreud(cx, cy, w, state)
                HistoricalFigure.CARL_JUNG -> drawCarlJung(cx, cy, w, state)
                HistoricalFigure.IBN_KHALDUN -> drawIbnKhaldun(cx, cy, w, state)
                HistoricalFigure.MASLOW -> drawMaslow(cx, cy, w, state)
            }

            // Overlay reaction accessories
            if (state == AvatarState.CORRECT) {
                drawCelebrationSparks(cx, cy, w)
            } else if (state == AvatarState.WRONG) {
                drawShockMarkers(cx, cy, w)
            }
        }
    }
}

// ==========================================
// 1. Philosophy & Wisdom Avatars
// ==========================================

// Socrates: Laurel wreath, big curly white beard, white toga with gold sash
private fun DrawScope.drawSocrates(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawTogaBody(cx, cy, size, Color(0xFFF1F5F9), Color(0xFFC79A3B))
    drawCircle(Color(0xFFF9D7B8), size * 0.25f, Offset(cx, cy - size * 0.04f))
    drawLaurelCrown(cx, cy - size * 0.22f, size, Color(0xFF4CAF50), Color(0xFFFFD54F))
    drawAvatarEyes(cx, cy - size * 0.08f, size, state)
    drawGreekBeard(cx, cy + size * 0.06f, size, Color(0xFFEDE8E3))
}

// Plato: Classical Greek toga with cyan trim, noble philosopher white hair & beard
private fun DrawScope.drawPlato(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawTogaBody(cx, cy, size, Color(0xFFE2E8F0), Color(0xFF0284C7))
    // High bald forehead with silver wavy side hair
    drawCircle(Color(0xFFE2E8F0), size * 0.26f, Offset(cx - size * 0.16f, cy - size * 0.08f))
    drawCircle(Color(0xFFE2E8F0), size * 0.26f, Offset(cx + size * 0.16f, cy - size * 0.08f))
    drawCircle(Color(0xFFFDE0C5), size * 0.25f, Offset(cx, cy - size * 0.04f))
    drawAvatarEyes(cx, cy - size * 0.08f, size, state)
    // Wise long silver beard
    drawLongPhilosopherBeard(cx, cy + size * 0.04f, size, Color(0xFFE2E8F0))
}

// Aristotle: Darker trimmed curly hair & beard, purple teacher toga
private fun DrawScope.drawAristotle(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawTogaBody(cx, cy, size, Color(0xFFF8FAFC), Color(0xFF7C3AED))
    // Curly dark brown hair
    drawCircle(Color(0xFF451A03), size * 0.27f, Offset(cx, cy - size * 0.08f))
    drawCircle(Color(0xFFFBD7B8), size * 0.24f, Offset(cx, cy - size * 0.04f))
    drawAvatarEyes(cx, cy - size * 0.08f, size, state)
    // Neat trimmed brown beard
    drawTrimmedBeard(cx, cy + size * 0.06f, size, Color(0xFF451A03))
}

// Descartes: Black 17th-century jacket, wavy black hair, goatee, white lace collar
private fun DrawScope.drawDescartes(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawSuitBody(cx, cy, size, Color(0xFF1E293B), Color.White, Color(0xFF38BDF8))
    // White broad lace collar
    drawCircle(Color.White, size * 0.18f, Offset(cx, cy + size * 0.18f))
    // Long wavy black locks
    drawCircle(Color(0xFF0F172A), size * 0.28f, Offset(cx - size * 0.16f, cy - size * 0.02f))
    drawCircle(Color(0xFF0F172A), size * 0.28f, Offset(cx + size * 0.16f, cy - size * 0.02f))
    drawCircle(Color(0xFFFDE0C5), size * 0.24f, Offset(cx, cy - size * 0.04f))
    drawAvatarEyes(cx, cy - size * 0.08f, size, state)
    // Pointy mustache & French goatee
    drawFrenchMustache(cx, cy + size * 0.04f, size, Color(0xFF0F172A))
    drawCircle(Color(0xFF0F172A), size * 0.04f, Offset(cx, cy + size * 0.14f))
}

// Avicenna (Ibn Sina): Silk white & gold physician turban, neat black beard, emerald collar
private fun DrawScope.drawAvicenna(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawTogaBody(cx, cy, size, Color(0xFF065F46), Color(0xFFF59E0B))
    drawCircle(Color(0xFFF9D7B8), size * 0.24f, Offset(cx, cy - size * 0.02f))
    // Silk Persian Scholar Turban
    drawTurban(cx, cy - size * 0.18f, size, Color(0xFFF8FAFC), Color(0xFFF59E0B), Color(0xFF10B981))
    drawAvatarEyes(cx, cy - size * 0.05f, size, state)
    drawTrimmedBeard(cx, cy + size * 0.06f, size, Color(0xFF1C1917))
}

// Confucius: Black traditional Chinese official cap with red tassel, drooping gray mustache & goatee
private fun DrawScope.drawConfucius(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawTogaBody(cx, cy, size, Color(0xFF881337), Color(0xFFD97706))
    drawCircle(Color(0xFFFDE0C5), size * 0.24f, Offset(cx, cy - size * 0.02f))
    // Chinese official scholar hat
    val hat = Path().apply {
        moveTo(cx - size * 0.22f, cy - size * 0.16f)
        lineTo(cx + size * 0.22f, cy - size * 0.16f)
        lineTo(cx + size * 0.16f, cy - size * 0.32f)
        lineTo(cx - size * 0.16f, cy - size * 0.32f)
        close()
    }
    drawPath(hat, Color(0xFF0F172A))
    drawCircle(Color(0xFFEF4444), size * 0.035f, Offset(cx, cy - size * 0.33f))
    drawAvatarEyes(cx, cy - size * 0.06f, size, state)
    // Long drooping Chinese mustache & wispy goatee
    drawDroopingMustache(cx, cy + size * 0.04f, size, Color(0xFF94A3B8))
    drawLongPhilosopherBeard(cx, cy + size * 0.08f, size * 0.8f, Color(0xFFCBD5E1))
}

// Seneca: Roman patrician toga, short receded grey hair, wise stoic gaze
private fun DrawScope.drawSeneca(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawTogaBody(cx, cy, size, Color(0xFFF1F5F9), Color(0xFFDC2626))
    drawCircle(Color(0xFFCBD5E1), size * 0.26f, Offset(cx, cy - size * 0.06f))
    drawCircle(Color(0xFFF9D7B8), size * 0.24f, Offset(cx, cy - size * 0.03f))
    drawAvatarEyes(cx, cy - size * 0.07f, size, state)
    drawGentleMouth(cx, cy + size * 0.05f, size)
}

// Marcus Aurelius: Imperial purple cloak, golden laurel crown, curly philosopher Roman beard
private fun DrawScope.drawMarcusAurelius(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawTogaBody(cx, cy, size, Color(0xFF581C87), Color(0xFFFACC15))
    drawCircle(Color(0xFF78350F), size * 0.27f, Offset(cx, cy - size * 0.06f))
    drawCircle(Color(0xFFF9D7B8), size * 0.24f, Offset(cx, cy - size * 0.03f))
    drawLaurelCrown(cx, cy - size * 0.2f, size, Color(0xFFEAB308), Color(0xFFFEF08A))
    drawAvatarEyes(cx, cy - size * 0.07f, size, state)
    drawGreekBeard(cx, cy + size * 0.06f, size, Color(0xFF78350F))
}

// ==========================================
// 2. Science & Discovery Avatars
// ==========================================

// Einstein: Iconic frizzy wild white hair shock, mustache, playful tongue
private fun DrawScope.drawEinstein(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawSuitBody(cx, cy, size, Color(0xFF78350F), Color(0xFFFDE68A), Color(0xFF9A3412))
    // Wild frizzy hair shock
    val hairBubbles = listOf(
        Offset(cx - size * 0.28f, cy - size * 0.16f) to size * 0.15f,
        Offset(cx - size * 0.36f, cy) to size * 0.14f,
        Offset(cx, cy - size * 0.28f) to size * 0.17f,
        Offset(cx + size * 0.28f, cy - size * 0.16f) to size * 0.15f,
        Offset(cx + size * 0.36f, cy) to size * 0.14f
    )
    for ((center, radius) in hairBubbles) {
        drawCircle(color = Color(0xFFEDE8E3), radius = radius, center = center)
    }
    drawCircle(Color(0xFFFDE0C5), size * 0.24f, Offset(cx, cy - size * 0.02f))
    drawAvatarEyes(cx, cy - size * 0.06f, size, state)
    drawBushyMustache(cx, cy + size * 0.06f, size, Color(0xFFEDE8E3))

    if (state != AvatarState.WRONG) {
        val tongue = Path().apply {
            moveTo(cx - size * 0.04f, cy + size * 0.12f)
            lineTo(cx + size * 0.04f, cy + size * 0.12f)
            quadraticTo(cx + size * 0.04f, cy + size * 0.22f, cx, cy + size * 0.22f)
            quadraticTo(cx - size * 0.04f, cy + size * 0.22f, cx - size * 0.04f, cy + size * 0.12f)
            close()
        }
        drawPath(tongue, Color(0xFFFF5252))
    }
}

// Newton: Cascading curly grey periwig, blue coat, hovering shiny red apple
private fun DrawScope.drawNewton(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawSuitBody(cx, cy, size, Color(0xFF1E3A8A), Color.White, Color(0xFF38BDF8))
    // 17th Century aristocratic curly wig
    val wigColor = Color(0xFFE2E8F0)
    drawCircle(wigColor, size * 0.26f, Offset(cx - size * 0.18f, cy - size * 0.04f))
    drawCircle(wigColor, size * 0.26f, Offset(cx + size * 0.18f, cy - size * 0.04f))
    drawCircle(wigColor, size * 0.24f, Offset(cx, cy - size * 0.16f))
    drawCircle(Color(0xFFFDE0C5), size * 0.23f, Offset(cx, cy - size * 0.02f))
    drawAvatarEyes(cx, cy - size * 0.06f, size, state)
    drawGentleMouth(cx, cy + size * 0.06f, size)

    // Red Apple Hovering overhead
    val appleX = cx + size * 0.26f
    val appleY = cy - size * 0.32f
    val appleR = size * 0.09f
    drawCircle(Color(0xFFE11D48), appleR, Offset(appleX, appleY))
    drawCircle(Color(0xFF22C55E), size * 0.035f, Offset(appleX + appleR * 0.2f, appleY - appleR * 1.1f))
}

// Galileo: 17th-century white lace ruff collar, pointed grey beard & mustache, telescope glint
private fun DrawScope.drawGalileo(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawSuitBody(cx, cy, size, Color(0xFF1E293B), Color.White, Color(0xFF0284C7))
    // White pleated collar
    drawCircle(Color.White, size * 0.18f, Offset(cx, cy + size * 0.18f))
    drawCircle(Color(0xFF94A3B8), size * 0.26f, Offset(cx, cy - size * 0.06f))
    drawCircle(Color(0xFFFDE0C5), size * 0.24f, Offset(cx, cy - size * 0.03f))
    drawAvatarEyes(cx, cy - size * 0.07f, size, state)
    drawTrimmedBeard(cx, cy + size * 0.05f, size, Color(0xFFCBD5E1))
    // Golden telescope star glint
    drawCircle(Color(0xFFFDE047), size * 0.04f, Offset(cx + size * 0.32f, cy - size * 0.22f))
}

// Marie Curie: Victorian high-neck black dress, neat wavy dark hair bun, green glowing flask
private fun DrawScope.drawMarieCurie(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawSuitBody(cx, cy, size, Color(0xFF0F172A), Color(0xFFE2E8F0), Color(0xFFA78BFA))
    // Victorian rolled hair bun
    drawCircle(Color(0xFF475569), size * 0.16f, Offset(cx, cy - size * 0.26f))
    drawCircle(Color(0xFF334155), size * 0.26f, Offset(cx, cy - size * 0.06f))
    drawCircle(Color(0xFFFDE0C5), size * 0.23f, Offset(cx, cy - size * 0.02f))
    drawAvatarEyes(cx, cy - size * 0.06f, size, state)
    drawGentleMouth(cx, cy + size * 0.06f, size)

    // Glowing green radium flask accessory
    val flaskX = cx + size * 0.3f
    val flaskY = cy + size * 0.08f
    drawCircle(Color(0xFF10B981), size * 0.065f, Offset(flaskX, flaskY))
    drawCircle(Color(0xFF6EE7B7), size * 0.035f, Offset(flaskX, flaskY - size * 0.02f))
}

// Al-Khwarizmi: Baghdad green and gold silk turban with scholar feather, dark beard, math robe
private fun DrawScope.drawAlKhwarizmi(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawTogaBody(cx, cy, size, Color(0xFF047857), Color(0xFFF59E0B))
    drawCircle(Color(0xFFF9D7B8), size * 0.24f, Offset(cx, cy - size * 0.02f))
    drawTurban(cx, cy - size * 0.18f, size, Color(0xFF065F46), Color(0xFFFDE047), Color(0xFF38BDF8))
    drawAvatarEyes(cx, cy - size * 0.05f, size, state)
    drawTrimmedBeard(cx, cy + size * 0.06f, size, Color(0xFF1E1B18))
}

// Ibn al-Haytham: Scholar crimson & gold turban, optician lens glint, grey beard
private fun DrawScope.drawIbnAlHaytham(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawTogaBody(cx, cy, size, Color(0xFF881337), Color(0xFFF59E0B))
    drawCircle(Color(0xFFF9D7B8), size * 0.24f, Offset(cx, cy - size * 0.02f))
    drawTurban(cx, cy - size * 0.18f, size, Color(0xFF991B1B), Color(0xFFFEF08A), Color(0xFF2DD4BF))
    drawAvatarEyes(cx, cy - size * 0.05f, size, state)
    drawTrimmedBeard(cx, cy + size * 0.06f, size, Color(0xFF64748B))
    // Optical lens circle
    drawCircle(Color(0xFF2DD4BF), size * 0.05f, Offset(cx + size * 0.28f, cy - size * 0.2f), style = Stroke(width = size * 0.02f))
}

// Tesla: Dapper center-parted dark sleek hair, sharp trim dark mustache, high collar, cyan electric spark
private fun DrawScope.drawTesla(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawSuitBody(cx, cy, size, Color(0xFF0F172A), Color.White, Color(0xFF0F172A))
    // Sleek center-parted dark hair
    drawCircle(Color(0xFF09090B), size * 0.26f, Offset(cx - size * 0.12f, cy - size * 0.1f))
    drawCircle(Color(0xFF09090B), size * 0.26f, Offset(cx + size * 0.12f, cy - size * 0.1f))
    drawCircle(Color(0xFFFDE0C5), size * 0.23f, Offset(cx, cy - size * 0.02f))
    drawAvatarEyes(cx, cy - size * 0.06f, size, state)
    // Sharp trim mustache
    drawThinMustache(cx, cy + size * 0.04f, size, Color(0xFF09090B))
    drawGentleMouth(cx, cy + size * 0.08f, size)

    // Cyan electric sparks
    drawCircle(Color(0xFF00E5FF), size * 0.035f, Offset(cx - size * 0.32f, cy - size * 0.2f))
    drawCircle(Color(0xFF00E5FF), size * 0.035f, Offset(cx + size * 0.32f, cy - size * 0.2f))
}

// Edison: Silver brushed-back hair, dark 1890s vest and bow tie, lightbulb glow hovering
private fun DrawScope.drawEdison(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawSuitBody(cx, cy, size, Color(0xFF334155), Color.White, Color(0xFFDC2626))
    drawCircle(Color(0xFFCBD5E1), size * 0.26f, Offset(cx, cy - size * 0.08f))
    drawCircle(Color(0xFFFDE0C5), size * 0.23f, Offset(cx, cy - size * 0.02f))
    drawAvatarEyes(cx, cy - size * 0.06f, size, state)
    drawGentleMouth(cx, cy + size * 0.06f, size)

    // Golden lightbulb hovering
    val bulbX = cx + size * 0.28f
    val bulbY = cy - size * 0.28f
    drawCircle(Color(0xFFFDE047), size * 0.06f, Offset(bulbX, bulbY))
    drawCircle(Color(0xFFFEF08A), size * 0.03f, Offset(bulbX, bulbY))
}

// ==========================================
// 3. Literature & Arts Avatars
// ==========================================

// Shakespeare: Bald domed forehead, curly brown side hair, pointed goatee, pleated Elizabethan ruff
private fun DrawScope.drawShakespeare(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawSuitBody(cx, cy, size, Color(0xFF1E293B), Color.White, Color(0xFFF59E0B))
    // Broad white pleated Elizabethan ruff collar
    drawCircle(Color(0xFFF8FAFC), size * 0.2f, Offset(cx, cy + size * 0.16f))
    // Curly brown hair at sides
    drawCircle(Color(0xFF451A03), size * 0.16f, Offset(cx - size * 0.22f, cy - size * 0.02f))
    drawCircle(Color(0xFF451A03), size * 0.16f, Offset(cx + size * 0.22f, cy - size * 0.02f))
    // High bald dome head
    drawCircle(Color(0xFFFDE0C5), size * 0.25f, Offset(cx, cy - size * 0.04f))
    drawAvatarEyes(cx, cy - size * 0.06f, size, state)
    // Pointy Elizabethan mustache & goatee
    drawFrenchMustache(cx, cy + size * 0.04f, size, Color(0xFF451A03))
    drawCircle(Color(0xFF451A03), size * 0.035f, Offset(cx, cy + size * 0.13f))
}

// Al-Mutanabbi: Flowing desert keffiyeh and black headband, fierce proud black beard
private fun DrawScope.drawAlMutanabbi(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawTogaBody(cx, cy, size, Color(0xFF78350F), Color(0xFFD97706))
    // Flowing white keffiyeh headdress
    drawCircle(Color(0xFFF8FAFC), size * 0.28f, Offset(cx, cy - size * 0.08f))
    // Black Agal headband
    drawArc(Color(0xFF18181B), 190f, 160f, false, Offset(cx - size * 0.24f, cy - size * 0.26f), Size(size * 0.48f, size * 0.18f), style = Stroke(size * 0.05f))
    drawCircle(Color(0xFFF9D7B8), size * 0.24f, Offset(cx, cy - size * 0.02f))
    drawAvatarEyes(cx, cy - size * 0.06f, size, state)
    drawTrimmedBeard(cx, cy + size * 0.06f, size, Color(0xFF09090B))
}

// Naguib Mahfouz: Iconic round glasses, grey flat cap, Cairo 1960s suit and tie
private fun DrawScope.drawNaguibMahfouz(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawSuitBody(cx, cy, size, Color(0xFF334155), Color.White, Color(0xFFEA580C))
    // Grey wool cap / hair
    drawCircle(Color(0xFF64748B), size * 0.26f, Offset(cx, cy - size * 0.08f))
    drawCircle(Color(0xFFFDE0C5), size * 0.23f, Offset(cx, cy - size * 0.02f))
    // Round dark spectacles
    drawGlasses(cx, cy - size * 0.06f, size, Color(0xFF1E293B))
    drawAvatarEyes(cx, cy - size * 0.06f, size, state)
    drawGentleMouth(cx, cy + size * 0.07f, size)
}

// Khalil Gibran: Dark wavy romantic poet locks, soft mustache, artistic brown coat
private fun DrawScope.drawKhalilGibran(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawSuitBody(cx, cy, size, Color(0xFF451A03), Color(0xFFFDE68A), Color(0xFF0284C7))
    // Wavy dark brown hair
    drawCircle(Color(0xFF1C1917), size * 0.28f, Offset(cx, cy - size * 0.08f))
    drawCircle(Color(0xFFFDE0C5), size * 0.23f, Offset(cx, cy - size * 0.02f))
    drawAvatarEyes(cx, cy - size * 0.06f, size, state)
    drawThinMustache(cx, cy + size * 0.04f, size, Color(0xFF1C1917))
    drawGentleMouth(cx, cy + size * 0.08f, size)
}

// Dostoevsky: Broad intellectual forehead, deep intense eyes, full Russian brown beard
private fun DrawScope.drawDostoevsky(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawSuitBody(cx, cy, size, Color(0xFF1C1917), Color(0xFFE2E8F0), Color(0xFF991B1B))
    // Dark brown side hair, prominent high bald top
    drawCircle(Color(0xFF451A03), size * 0.26f, Offset(cx, cy - size * 0.06f))
    drawCircle(Color(0xFFFDE0C5), size * 0.24f, Offset(cx, cy - size * 0.03f))
    drawAvatarEyes(cx, cy - size * 0.07f, size, state)
    drawLongPhilosopherBeard(cx, cy + size * 0.04f, size * 1.1f, Color(0xFF451A03))
}

// Da Vinci: Long flowing Renaissance white beard, brown artist beret
private fun DrawScope.drawDaVinci(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawTogaBody(cx, cy, size, Color(0xFF78350F), Color(0xFFEAB308))
    // Artist Velvet Beret
    drawCircle(Color(0xFF451A03), size * 0.22f, Offset(cx - size * 0.08f, cy - size * 0.18f))
    drawCircle(Color(0xFFFDE0C5), size * 0.24f, Offset(cx, cy - size * 0.03f))
    drawAvatarEyes(cx, cy - size * 0.06f, size, state)
    drawLongPhilosopherBeard(cx, cy + size * 0.04f, size * 1.25f, Color(0xFFF1F5F9))
}

// Beethoven: Wild dark unruly shock of composer hair, passionate scowl, red cravat scarf
private fun DrawScope.drawBeethoven(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawSuitBody(cx, cy, size, Color(0xFF0F172A), Color.White, Color(0xFFDC2626))
    // Wrapped Red Scarf
    drawCircle(Color(0xFFDC2626), size * 0.16f, Offset(cx, cy + size * 0.17f))
    // Wild shock of dark romantic hair
    val hairBubbles = listOf(
        Offset(cx - size * 0.24f, cy - size * 0.18f) to size * 0.16f,
        Offset(cx + size * 0.24f, cy - size * 0.18f) to size * 0.16f,
        Offset(cx - size * 0.32f, cy - size * 0.02f) to size * 0.14f,
        Offset(cx + size * 0.32f, cy - size * 0.02f) to size * 0.14f,
        Offset(cx, cy - size * 0.26f) to size * 0.16f
    )
    for ((center, radius) in hairBubbles) {
        drawCircle(Color(0xFF1E1B18), radius, center)
    }
    drawCircle(Color(0xFFFDE0C5), size * 0.23f, Offset(cx, cy - size * 0.02f))
    drawAvatarEyes(cx, cy - size * 0.06f, size, state)
    drawGentleMouth(cx, cy + size * 0.07f, size)
}

// ==========================================
// 4. History & Leadership Avatars
// ==========================================

// Napoleon: Bicorne hat with tricolor cockade, military jacket with red lapels and gold epaulets
private fun DrawScope.drawNapoleon(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawMilitaryBody(cx, cy, size, Color(0xFF1E3A8A), Color(0xFFDC2626), Color(0xFFFACC15))
    drawCircle(Color(0xFFFDE0C5), size * 0.24f, Offset(cx, cy + size * 0.02f))
    drawAvatarEyes(cx, cy, size, state)
    drawGentleMouth(cx, cy + size * 0.09f, size)
    drawHatBicorne(cx, cy - size * 0.14f, size, Color(0xFF0F172A), Color(0xFFDC2626))
}

// Alexander the Great: Golden wavy Hellenic locks, golden royal diadem, crimson military cloak
private fun DrawScope.drawAlexanderGreat(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawMilitaryBody(cx, cy, size, Color(0xFF991B1B), Color(0xFFF59E0B), Color(0xFFFEF08A))
    // Sun-lit golden locks
    drawCircle(Color(0xFFF59E0B), size * 0.28f, Offset(cx, cy - size * 0.06f))
    // Golden royal diadem
    drawArc(Color(0xFFFDE047), 190f, 160f, false, Offset(cx - size * 0.22f, cy - size * 0.22f), Size(size * 0.44f, size * 0.16f), style = Stroke(size * 0.04f))
    drawCircle(Color(0xFFF9D7B8), size * 0.24f, Offset(cx, cy - size * 0.02f))
    drawAvatarEyes(cx, cy - size * 0.06f, size, state)
    drawGentleMouth(cx, cy + size * 0.06f, size)
}

// Julius Caesar: Roman golden laurel wreath, noble receding Roman hair, imperial crimson toga
private fun DrawScope.drawJuliusCaesar(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawTogaBody(cx, cy, size, Color(0xFFDC2626), Color(0xFFFACC15))
    drawCircle(Color(0xFF64748B), size * 0.26f, Offset(cx, cy - size * 0.06f))
    drawCircle(Color(0xFFFDE0C5), size * 0.24f, Offset(cx, cy - size * 0.03f))
    drawLaurelCrown(cx, cy - size * 0.2f, size, Color(0xFFEAB308), Color(0xFFFEF08A))
    drawAvatarEyes(cx, cy - size * 0.07f, size, state)
    drawGentleMouth(cx, cy + size * 0.05f, size)
}

// Saladin: Chivalric green warrior turban around steel helmet with gold nasal guard, trim beard
private fun DrawScope.drawSaladin(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawMilitaryBody(cx, cy, size, Color(0xFF065F46), Color(0xFF047857), Color(0xFFFACC15))
    drawCircle(Color(0xFFF9D7B8), size * 0.24f, Offset(cx, cy - size * 0.02f))
    // Knight Turban and Steel Helmet with Gold Nasal Guard
    drawTurban(cx, cy - size * 0.18f, size, Color(0xFF047857), Color(0xFFF59E0B), Color(0xFFE2E8F0))
    drawLine(Color(0xFFF59E0B), Offset(cx, cy - size * 0.18f), Offset(cx, cy - size * 0.02f), strokeWidth = size * 0.03f)
    drawAvatarEyes(cx, cy - size * 0.05f, size, state)
    drawTrimmedBeard(cx, cy + size * 0.06f, size, Color(0xFF1C1917))
}

// Sun Tzu: Ancient Chinese brass general helmet with crimson tassel plume, stern warrior mustache
private fun DrawScope.drawSunTzu(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawMilitaryBody(cx, cy, size, Color(0xFF78350F), Color(0xFFD97706), Color(0xFFDC2626))
    drawCircle(Color(0xFFFDE0C5), size * 0.24f, Offset(cx, cy - size * 0.02f))
    // Brass General Helmet
    val helmet = Path().apply {
        moveTo(cx - size * 0.22f, cy - size * 0.12f)
        lineTo(cx + size * 0.22f, cy - size * 0.12f)
        lineTo(cx, cy - size * 0.36f)
        close()
    }
    drawPath(helmet, Color(0xFFD97706))
    // Crimson Tassel Plume
    drawCircle(Color(0xFFDC2626), size * 0.05f, Offset(cx, cy - size * 0.38f))
    drawAvatarEyes(cx, cy - size * 0.05f, size, state)
    drawDroopingMustache(cx, cy + size * 0.05f, size, Color(0xFF1C1917))
}

// ==========================================
// 5. Psychology & Mind Avatars
// ==========================================

// Freud: Grey beard, round spectacles, cigar with rising smoke, tweed vest and tie
private fun DrawScope.drawFreud(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawSuitBody(cx, cy, size, Color(0xFF475569), Color.White, Color(0xFF7C3AED))
    drawCircle(Color(0xFF94A3B8), size * 0.26f, Offset(cx, cy - size * 0.06f))
    drawCircle(Color(0xFFFDE0C5), size * 0.23f, Offset(cx, cy - size * 0.02f))
    drawGlasses(cx, cy - size * 0.06f, size, Color(0xFF1E293B))
    drawAvatarEyes(cx, cy - size * 0.06f, size, state)
    drawTrimmedBeard(cx, cy + size * 0.06f, size, Color(0xFFCBD5E1))

    // Cigar with smoke plume
    val cigarX = cx + size * 0.12f
    val cigarY = cy + size * 0.08f
    drawLine(Color(0xFF78350F), Offset(cigarX, cigarY), Offset(cigarX + size * 0.14f, cigarY - size * 0.03f), strokeWidth = size * 0.035f, cap = StrokeCap.Round)
    drawCircle(Color(0xFFEF4444), size * 0.015f, Offset(cigarX + size * 0.14f, cigarY - size * 0.03f))
    drawCircle(Color(0x55E2E8F0), size * 0.04f, Offset(cigarX + size * 0.16f, cigarY - size * 0.1f))
}

// Carl Jung: Round gold spectacles, neat side-parted silver hair, Swiss tweed suit
private fun DrawScope.drawCarlJung(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawSuitBody(cx, cy, size, Color(0xFF334155), Color.White, Color(0xFF4F46E5))
    drawCircle(Color(0xFFCBD5E1), size * 0.26f, Offset(cx, cy - size * 0.08f))
    drawCircle(Color(0xFFFDE0C5), size * 0.23f, Offset(cx, cy - size * 0.02f))
    drawGlasses(cx, cy - size * 0.06f, size, Color(0xFFF59E0B))
    drawAvatarEyes(cx, cy - size * 0.06f, size, state)
    drawGentleMouth(cx, cy + size * 0.07f, size)
}

// Ibn Khaldun: Maghrebi scholar cream turban, dark robe, wise historian gaze & beard
private fun DrawScope.drawIbnKhaldun(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawTogaBody(cx, cy, size, Color(0xFF451A03), Color(0xFFB45309))
    drawCircle(Color(0xFFF9D7B8), size * 0.24f, Offset(cx, cy - size * 0.02f))
    drawTurban(cx, cy - size * 0.18f, size, Color(0xFFF8FAFC), Color(0xFFD97706), Color(0xFF78350F))
    drawAvatarEyes(cx, cy - size * 0.05f, size, state)
    drawTrimmedBeard(cx, cy + size * 0.06f, size, Color(0xFF451A03))
}

// Maslow: Friendly bald top with side hair, round glasses, professor sweater
private fun DrawScope.drawMaslow(cx: Float, cy: Float, size: Float, state: AvatarState) {
    drawSuitBody(cx, cy, size, Color(0xFFB45309), Color(0xFFFEF3C7), Color(0xFF78350F))
    drawCircle(Color(0xFF64748B), size * 0.25f, Offset(cx, cy - size * 0.06f))
    drawCircle(Color(0xFFFDE0C5), size * 0.23f, Offset(cx, cy - size * 0.02f))
    drawGlasses(cx, cy - size * 0.06f, size, Color(0xFF334155))
    drawAvatarEyes(cx, cy - size * 0.06f, size, state)
    drawGentleMouth(cx, cy + size * 0.07f, size)
}

// ==========================================
// Reusable Modular Drawing Primitives
// ==========================================

private fun DrawScope.drawTogaBody(cx: Float, cy: Float, size: Float, togaColor: Color, sashColor: Color) {
    drawArc(
        color = togaColor,
        startAngle = 0f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(cx - size * 0.42f, cy + size * 0.15f),
        size = Size(size * 0.84f, size * 0.65f)
    )
    val sash = Path().apply {
        moveTo(cx - size * 0.35f, cy + size * 0.45f)
        lineTo(cx + size * 0.2f, cy + size * 0.2f)
        lineTo(cx + size * 0.38f, cy + size * 0.32f)
        lineTo(cx - size * 0.15f, cy + size * 0.5f)
        close()
    }
    drawPath(sash, color = sashColor)
}

private fun DrawScope.drawSuitBody(cx: Float, cy: Float, size: Float, coatColor: Color, shirtColor: Color, tieColor: Color) {
    drawArc(
        color = coatColor,
        startAngle = 0f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(cx - size * 0.42f, cy + size * 0.15f),
        size = Size(size * 0.84f, size * 0.65f)
    )
    // Shirt triangle
    val shirt = Path().apply {
        moveTo(cx - size * 0.12f, cy + size * 0.15f)
        lineTo(cx + size * 0.12f, cy + size * 0.15f)
        lineTo(cx, cy + size * 0.4f)
        close()
    }
    drawPath(shirt, color = shirtColor)
    // Tie
    drawLine(color = tieColor, start = Offset(cx, cy + size * 0.18f), end = Offset(cx, cy + size * 0.38f), strokeWidth = size * 0.04f, cap = StrokeCap.Round)
}

private fun DrawScope.drawMilitaryBody(cx: Float, cy: Float, size: Float, uniformColor: Color, lapelColor: Color, epauletColor: Color) {
    drawArc(
        color = uniformColor,
        startAngle = 0f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(cx - size * 0.44f, cy + size * 0.16f),
        size = Size(size * 0.88f, size * 0.65f)
    )
    // Epaulets
    drawCircle(epauletColor, size * 0.07f, Offset(cx - size * 0.36f, cy + size * 0.25f))
    drawCircle(epauletColor, size * 0.07f, Offset(cx + size * 0.36f, cy + size * 0.25f))
    // Lapels
    val lapel = Path().apply {
        moveTo(cx - size * 0.1f, cy + size * 0.16f)
        lineTo(cx + size * 0.1f, cy + size * 0.16f)
        lineTo(cx, cy + size * 0.42f)
        close()
    }
    drawPath(lapel, color = lapelColor)
}

private fun DrawScope.drawLaurelCrown(cx: Float, cy: Float, size: Float, laurelColor: Color, goldColor: Color) {
    drawArc(
        color = laurelColor,
        startAngle = 190f,
        sweepAngle = 160f,
        useCenter = false,
        topLeft = Offset(cx - size * 0.26f, cy - size * 0.14f),
        size = Size(size * 0.52f, size * 0.32f),
        style = Stroke(width = size * 0.04f, cap = StrokeCap.Round)
    )
    for (i in -3..3) {
        val angle = i * 22f
        val leafX = cx + (size * 0.24f) * sin(Math.toRadians(angle.toDouble())).toFloat()
        val leafY = cy - (size * 0.1f) * cos(Math.toRadians(angle.toDouble())).toFloat()
        drawCircle(color = if (i % 2 == 0) goldColor else laurelColor, radius = size * 0.03f, center = Offset(leafX, leafY))
    }
}

private fun DrawScope.drawTurban(cx: Float, cy: Float, size: Float, mainColor: Color, trimColor: Color, gemColor: Color) {
    drawArc(mainColor, 180f, 180f, true, Offset(cx - size * 0.28f, cy - size * 0.14f), Size(size * 0.56f, size * 0.38f))
    drawArc(trimColor, 180f, 180f, false, Offset(cx - size * 0.28f, cy - size * 0.04f), Size(size * 0.56f, size * 0.24f), style = Stroke(size * 0.04f))
    drawCircle(gemColor, size * 0.04f, Offset(cx, cy + size * 0.04f))
}

private fun DrawScope.drawHatBicorne(cx: Float, cy: Float, size: Float, hatColor: Color, cockadeColor: Color) {
    val hat = Path().apply {
        moveTo(cx - size * 0.46f, cy + size * 0.12f)
        cubicTo(cx - size * 0.35f, cy - size * 0.18f, cx - size * 0.15f, cy - size * 0.22f, cx, cy - size * 0.22f)
        cubicTo(cx + size * 0.15f, cy - size * 0.22f, cx + size * 0.35f, cy - size * 0.18f, cx + size * 0.46f, cy + size * 0.12f)
        cubicTo(cx + size * 0.25f, cy + size * 0.04f, cx - size * 0.25f, cy + size * 0.04f, cx - size * 0.46f, cy + size * 0.12f)
        close()
    }
    drawPath(hat, color = hatColor)
    // Cockade badge
    drawCircle(Color.White, size * 0.04f, Offset(cx, cy - size * 0.08f))
    drawCircle(cockadeColor, size * 0.024f, Offset(cx, cy - size * 0.08f))
}

private fun DrawScope.drawGreekBeard(cx: Float, cy: Float, size: Float, beardColor: Color) {
    val beard = Path().apply {
        moveTo(cx - size * 0.24f, cy - size * 0.08f)
        cubicTo(cx - size * 0.32f, cy + size * 0.14f, cx - size * 0.15f, cy + size * 0.36f, cx, cy + size * 0.36f)
        cubicTo(cx + size * 0.15f, cy + size * 0.36f, cx + size * 0.32f, cy + size * 0.14f, cx + size * 0.24f, cy - size * 0.08f)
        close()
    }
    drawPath(beard, color = beardColor)
}

private fun DrawScope.drawTrimmedBeard(cx: Float, cy: Float, size: Float, beardColor: Color) {
    val beard = Path().apply {
        moveTo(cx - size * 0.2f, cy - size * 0.05f)
        quadraticTo(cx - size * 0.22f, cy + size * 0.15f, cx, cy + size * 0.2f)
        quadraticTo(cx + size * 0.22f, cy + size * 0.15f, cx + size * 0.2f, cy - size * 0.05f)
        close()
    }
    drawPath(beard, color = beardColor)
}

private fun DrawScope.drawLongPhilosopherBeard(cx: Float, cy: Float, size: Float, beardColor: Color) {
    val beard = Path().apply {
        moveTo(cx - size * 0.22f, cy - size * 0.05f)
        cubicTo(cx - size * 0.28f, cy + size * 0.2f, cx - size * 0.1f, cy + size * 0.44f, cx, cy + size * 0.44f)
        cubicTo(cx + size * 0.1f, cy + size * 0.44f, cx + size * 0.28f, cy + size * 0.2f, cx + size * 0.22f, cy - size * 0.05f)
        close()
    }
    drawPath(beard, color = beardColor)
}

private fun DrawScope.drawFrenchMustache(cx: Float, cy: Float, size: Float, color: Color) {
    val leftWing = Path().apply {
        moveTo(cx, cy)
        quadraticTo(cx - size * 0.08f, cy - size * 0.04f, cx - size * 0.16f, cy + size * 0.02f)
        quadraticTo(cx - size * 0.08f, cy + size * 0.03f, cx, cy)
        close()
    }
    val rightWing = Path().apply {
        moveTo(cx, cy)
        quadraticTo(cx + size * 0.08f, cy - size * 0.04f, cx + size * 0.16f, cy + size * 0.02f)
        quadraticTo(cx + size * 0.08f, cy + size * 0.03f, cx, cy)
        close()
    }
    drawPath(leftWing, color)
    drawPath(rightWing, color)
}

private fun DrawScope.drawBushyMustache(cx: Float, cy: Float, size: Float, color: Color) {
    val mustache = Path().apply {
        moveTo(cx - size * 0.18f, cy)
        cubicTo(cx - size * 0.08f, cy - size * 0.06f, cx, cy - size * 0.02f, cx, cy - size * 0.02f)
        cubicTo(cx, cy - size * 0.02f, cx + size * 0.08f, cy - size * 0.06f, cx + size * 0.18f, cy)
        cubicTo(cx + size * 0.1f, cy + size * 0.08f, cx - size * 0.1f, cy + size * 0.08f, cx - size * 0.18f, cy)
        close()
    }
    drawPath(mustache, color)
}

private fun DrawScope.drawDroopingMustache(cx: Float, cy: Float, size: Float, color: Color) {
    drawLine(color, Offset(cx - size * 0.02f, cy), Offset(cx - size * 0.12f, cy + size * 0.1f), strokeWidth = size * 0.035f, cap = StrokeCap.Round)
    drawLine(color, Offset(cx + size * 0.02f, cy), Offset(cx + size * 0.12f, cy + size * 0.1f), strokeWidth = size * 0.035f, cap = StrokeCap.Round)
}

private fun DrawScope.drawThinMustache(cx: Float, cy: Float, size: Float, color: Color) {
    drawLine(color, Offset(cx - size * 0.12f, cy), Offset(cx + size * 0.12f, cy), strokeWidth = size * 0.03f, cap = StrokeCap.Round)
}

private fun DrawScope.drawGlasses(cx: Float, cy: Float, size: Float, frameColor: Color) {
    val r = size * 0.06f
    val spacing = size * 0.1f
    drawCircle(frameColor, r, Offset(cx - spacing, cy), style = Stroke(size * 0.02f))
    drawCircle(frameColor, r, Offset(cx + spacing, cy), style = Stroke(size * 0.02f))
    drawLine(frameColor, Offset(cx - spacing + r, cy), Offset(cx + spacing - r, cy), strokeWidth = size * 0.02f)
}

private fun DrawScope.drawGentleMouth(cx: Float, cy: Float, size: Float) {
    drawArc(
        color = Color(0xFF9A3412),
        startAngle = 20f,
        sweepAngle = 140f,
        useCenter = false,
        topLeft = Offset(cx - size * 0.06f, cy - size * 0.02f),
        size = Size(size * 0.12f, size * 0.05f),
        style = Stroke(width = size * 0.02f, cap = StrokeCap.Round)
    )
}

private fun DrawScope.drawAvatarEyes(cx: Float, cy: Float, size: Float, state: AvatarState) {
    val eyeSpacing = size * 0.1f
    val leftEyeX = cx - eyeSpacing
    val rightEyeX = cx + eyeSpacing

    when (state) {
        AvatarState.CORRECT -> {
            drawArc(
                color = Color(0xFF1E293B),
                startAngle = 190f,
                sweepAngle = 160f,
                useCenter = false,
                topLeft = Offset(leftEyeX - size * 0.05f, cy - size * 0.03f),
                size = Size(size * 0.1f, size * 0.06f),
                style = Stroke(width = size * 0.03f, cap = StrokeCap.Round)
            )
            drawArc(
                color = Color(0xFF1E293B),
                startAngle = 190f,
                sweepAngle = 160f,
                useCenter = false,
                topLeft = Offset(rightEyeX - size * 0.05f, cy - size * 0.03f),
                size = Size(size * 0.1f, size * 0.06f),
                style = Stroke(width = size * 0.03f, cap = StrokeCap.Round)
            )
        }
        AvatarState.WRONG -> {
            val d = size * 0.035f
            drawLine(Color(0xFF1E293B), Offset(leftEyeX - d, cy - d), Offset(leftEyeX + d, cy + d), strokeWidth = size * 0.03f, cap = StrokeCap.Round)
            drawLine(Color(0xFF1E293B), Offset(leftEyeX - d, cy + d), Offset(leftEyeX + d, cy - d), strokeWidth = size * 0.03f, cap = StrokeCap.Round)
            drawLine(Color(0xFF1E293B), Offset(rightEyeX - d, cy - d), Offset(rightEyeX + d, cy + d), strokeWidth = size * 0.03f, cap = StrokeCap.Round)
            drawLine(Color(0xFF1E293B), Offset(rightEyeX - d, cy + d), Offset(rightEyeX + d, cy - d), strokeWidth = size * 0.03f, cap = StrokeCap.Round)
        }
        AvatarState.IDLE -> {
            drawCircle(color = Color(0xFF0F172A), radius = size * 0.04f, center = Offset(leftEyeX, cy))
            drawCircle(color = Color(0xFF0F172A), radius = size * 0.04f, center = Offset(rightEyeX, cy))
            drawCircle(color = Color.White, radius = size * 0.016f, center = Offset(leftEyeX - size * 0.012f, cy - size * 0.012f))
            drawCircle(color = Color.White, radius = size * 0.016f, center = Offset(rightEyeX - size * 0.012f, cy - size * 0.012f))
        }
    }
}

private fun DrawScope.drawCelebrationSparks(cx: Float, cy: Float, size: Float) {
    val starColor = Color(0xFFFFD54F)
    val sparks = listOf(
        Offset(cx - size * 0.38f, cy - size * 0.32f),
        Offset(cx + size * 0.38f, cy - size * 0.32f),
        Offset(cx + size * 0.42f, cy + size * 0.1f),
        Offset(cx - size * 0.42f, cy + size * 0.1f)
    )
    for (pos in sparks) {
        val s = size * 0.06f
        drawLine(starColor, Offset(pos.x - s, pos.y), Offset(pos.x + s, pos.y), strokeWidth = size * 0.02f)
        drawLine(starColor, Offset(pos.x, pos.y - s), Offset(pos.x, pos.y + s), strokeWidth = size * 0.02f)
    }
}

private fun DrawScope.drawShockMarkers(cx: Float, cy: Float, size: Float) {
    val dropX = cx + size * 0.32f
    val dropY = cy - size * 0.18f
    val dropPath = Path().apply {
        moveTo(dropX, dropY - size * 0.08f)
        quadraticTo(dropX + size * 0.05f, dropY, dropX, dropY + size * 0.05f)
        quadraticTo(dropX - size * 0.05f, dropY, dropX, dropY - size * 0.08f)
        close()
    }
    drawPath(dropPath, color = Color(0xFF00E5FF))
}
