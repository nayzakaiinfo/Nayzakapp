package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AnswerReview
import com.example.ui.GameUiState
import com.example.ui.components.NayzakCompactHeader
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.GameCorrect
import com.example.ui.theme.GameWrong
import com.example.ui.theme.LaurelGold
import com.example.ui.theme.LightBlueBorder
import com.example.ui.theme.LightBlueBorderGlow
import com.example.ui.theme.LightBlueButton
import com.example.ui.theme.LightBlueCard
import com.example.ui.theme.LightBlueElevated
import com.example.ui.theme.LightBlueSubtext
import com.example.ui.theme.LightBlueText
import com.example.ui.theme.LightBlueTextDark

@Composable
fun ResultScreen(
    uiState: GameUiState,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit,
    onShareClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showReviewList by remember { mutableStateOf(false) }

    // Hardware back goes home
    BackHandler {
        onHome()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFCA8A04),
                        Color(0xFFEAB308),
                        Color(0xFFD97706)
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NayzakCompactHeader(modifier = Modifier.padding(top = 4.dp))

        Spacer(modifier = Modifier.height(16.dp))

        // Grand Trophy & Score Card (Light Blue Container with Dark Navy Typography)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(24.dp), spotColor = Color(0x330284C7))
                .border(
                    width = 2.dp,
                    color = LightBlueBorder,
                    shape = RoundedCornerShape(24.dp)
                ),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = LightBlueCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Trophy Icon in Golden Ring
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    Color(0xFFFFF176),
                                    Color(0xFFFFB300),
                                    Color(0xFFB45309)
                                )
                            )
                        )
                        .shadow(12.dp, CircleShape, spotColor = LaurelGold)
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "كأس النتيجة",
                        tint = Color.Black,
                        modifier = Modifier.size(46.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Player Title & Rank
                Text(
                    text = uiState.playerTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = LightBlueTextDark,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "انتهت الجولة بمجموع إجابات مميز!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = LightBlueSubtext,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Large Score Badge
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${uiState.correctCount}",
                        fontSize = 52.sp,
                        fontWeight = FontWeight.Black,
                        color = GameCorrect
                    )
                    Text(
                        text = " / 10",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightBlueTextDark,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                Text(
                    text = "مجموع النقاط: ${uiState.score} نقطة 🎯",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = LightBlueButton
                )

                // High score indicator
                if (uiState.score >= uiState.personalBestScore && uiState.score > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(LightBlueElevated)
                            .border(1.5.dp, LightBlueBorder, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "✨ رقم قياسي جديد على هاتفك! ✨",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = LightBlueText
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Performance Stat Cards Grid (Light Blue)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "نسبة الدقة",
                    value = "${uiState.accuracyPercentage}%",
                    icon = Icons.Default.Percent,
                    color = GameCorrect,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "أعلى كومبو",
                    value = "x${uiState.maxStreak}",
                    icon = Icons.Default.LocalFireDepartment,
                    color = Color(0xFFEA580C),
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "متوسط السرعة",
                    value = "${String.format("%.1f", uiState.averageTimeSeconds)} ث",
                    icon = Icons.Default.Speed,
                    color = CyberBlue,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "أسرع جواب",
                    value = if (uiState.fastestTimeSeconds > 0) "${uiState.fastestTimeSeconds} ث" else "-",
                    icon = Icons.Default.Timer,
                    color = LaurelGold,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Share Result to Social Media Button (Vibrant Light Blue)
        Button(
            onClick = {
                onShareClick()
                val shareText = """
                    🏆 حققت ${uiState.score} نقطة في لعبة نيزك NAYZAK!
                    🧠 لقبي: ${uiState.playerTitle}
                    🎯 الإجابات الصحيحة: ${uiState.correctCount}/10
                    🔥 أعلى كومبو: x${uiState.maxStreak}
                    
                    تقدر تغلب نتيجتي؟ العب التحدي الآن!
                """.trimIndent()

                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, "شارك نتيجتك مع أصدقائك")
                context.startActivity(shareIntent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .shadow(12.dp, RoundedCornerShape(16.dp), spotColor = Color(0x660284C7))
                .border(2.dp, LightBlueBorderGlow, RoundedCornerShape(16.dp))
                .testTag("share_result_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightBlueButton,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "شارك نتيجتك وتحدّى أصحابك 🚀",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Play Again Button (Vibrant Light Blue)
        Button(
            onClick = onPlayAgain,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(16.dp, RoundedCornerShape(16.dp), spotColor = Color(0x660284C7))
                .border(2.dp, LightBlueBorderGlow, RoundedCornerShape(16.dp))
                .testTag("play_again_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightBlueButton,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Replay,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "العب جولة تانية (10 أسئلة جديدة)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Return Home Button (Light Blue Card + Navy Text)
        Button(
            onClick = onHome,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(1.8.dp, LightBlueBorder, RoundedCornerShape(16.dp))
                .testTag("home_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightBlueCard,
                contentColor = LightBlueTextDark
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = LightBlueButton,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "العودة للقائمة الرئيسية",
                    style = MaterialTheme.typography.bodyLarge,
                    color = LightBlueTextDark,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Review Answers Accordion Button (Light Blue)
        Button(
            onClick = { showReviewList = !showReviewList },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .border(1.5.dp, LightBlueBorder, RoundedCornerShape(14.dp)),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightBlueCard,
                contentColor = LightBlueTextDark
            )
        ) {
            Text(
                text = if (showReviewList) "إخفاء مراجعة الإجابات ▲" else "مراجعة جميع أسئلة وإجابات الجولة ▼",
                color = LightBlueTextDark,
                fontWeight = FontWeight.Bold
            )
        }

        AnimatedVisibility(visible = showReviewList) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                uiState.reviewList.forEachIndexed { index, review ->
                    ReviewItemCard(index = index + 1, review = review)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.border(1.8.dp, LightBlueBorder, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightBlueCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = LightBlueSubtext,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = LightBlueTextDark
            )
        }
    }
}

@Composable
private fun ReviewItemCard(
    index: Int,
    review: AnswerReview
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.8.dp,
                color = if (review.isCorrect) GameCorrect else GameWrong,
                shape = RoundedCornerShape(14.dp)
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = LightBlueCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (review.isCorrect) GameCorrect else GameWrong)
                    ) {
                        Icon(
                            imageVector = if (review.isCorrect) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "سؤال $index",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = LightBlueTextDark
                    )
                }

                Text(
                    text = "${review.timeSpentSeconds} ثانية",
                    style = MaterialTheme.typography.labelSmall,
                    color = LightBlueSubtext
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "« ${review.quoteItem.quote} »",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = LightBlueTextDark,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "القائل الحقيقي:",
                    style = MaterialTheme.typography.bodySmall,
                    color = LightBlueSubtext
                )
                Text(
                    text = review.quoteItem.author,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = GameCorrect
                )
            }

            if (!review.isCorrect && review.selectedFigure != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "إجابتك:",
                        style = MaterialTheme.typography.bodySmall,
                        color = LightBlueSubtext
                    )
                    Text(
                        text = review.selectedFigure.nameAr,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = GameWrong
                    )
                }
            }
        }
    }
}
