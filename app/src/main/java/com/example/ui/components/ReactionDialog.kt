package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.HistoricalFigure
import com.example.data.QuoteItem
import com.example.ui.theme.GameCorrect
import com.example.ui.theme.GameWrong
import com.example.ui.theme.LightBlueBorder
import com.example.ui.theme.LightBlueButton
import com.example.ui.theme.LightBlueCard
import com.example.ui.theme.LightBlueElevated
import com.example.ui.theme.LightBlueSubtext
import com.example.ui.theme.LightBlueText
import com.example.ui.theme.LightBlueTextDark

@Composable
fun AnswerReactionDialog(
    figure: HistoricalFigure,
    quoteItem: QuoteItem,
    isCorrect: Boolean,
    isTimeout: Boolean,
    reactionText: String,
    isLastQuestion: Boolean,
    onNextQuestion: () -> Unit
) {
    Dialog(
        onDismissRequest = onNextQuestion,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
    ) {
        AnimatedVisibility(
            visible = true,
            enter = scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn()
        ) {
            val statusColor = when {
                isCorrect -> GameCorrect
                isTimeout -> Color(0xFFD97706)
                else -> GameWrong
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .border(
                        width = 2.dp,
                        color = LightBlueBorder,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .shadow(16.dp, RoundedCornerShape(24.dp), spotColor = Color(0x440284C7)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = LightBlueCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header Result Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(statusColor.copy(alpha = 0.15f))
                            .border(1.5.dp, statusColor, RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = when {
                                isCorrect -> "إجابة أسطورية صحيحة! 🎉"
                                isTimeout -> "انتهى الوقت المحدد! ⏰"
                                else -> "إجابة خاطئة! ❌"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = statusColor
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Character Avatar in Celebratory or Shocked State
                    HistoricalFigureAvatar(
                        figure = figure,
                        size = 100.dp,
                        state = if (isCorrect) AvatarState.CORRECT else AvatarState.WRONG,
                        showBorderGlow = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Character Name & Title
                    Text(
                        text = figure.nameAr,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = LightBlueTextDark
                    )
                    Text(
                        text = figure.titleAr,
                        style = MaterialTheme.typography.bodySmall,
                        color = LightBlueSubtext,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Levantine Arabic Speech Bubble (Light Blue Elevated)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(LightBlueElevated)
                            .border(1.5.dp, LightBlueBorder, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "💬 قال ${figure.nameAr}:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = LightBlueSubtext,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "« $reactionText »",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = LightBlueTextDark,
                                textAlign = TextAlign.Center,
                                lineHeight = 24.sp
                            )
                        }
                    }

                    // If wrong/timeout, highlight the real answer
                    if (!isCorrect) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(GameCorrect.copy(alpha = 0.15f))
                                .border(1.dp, GameCorrect, RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = GameCorrect,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "صاحب الاقتباس الحقيقي: ${quoteItem.author}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = GameCorrect
                            )
                        }
                    }

                    // Fun Fact info toggle
                    var showInfo by remember { mutableStateOf(false) }
                    if (quoteItem.funFact.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(LightBlueElevated)
                                .border(1.dp, LightBlueBorder, RoundedCornerShape(10.dp))
                                .clickable { showInfo = !showInfo }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = LightBlueButton,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "معلومة عن هذا الاقتباس",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = LightBlueText,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = if (showInfo) "▲" else "▼",
                                color = LightBlueButton,
                                fontSize = 11.sp
                            )
                        }

                        AnimatedVisibility(visible = showInfo) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(LightBlueElevated)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = quoteItem.funFact,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = LightBlueTextDark,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Next Button (Vibrant Light Blue / Cerulean)
                    Button(
                        onClick = onNextQuestion,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .shadow(12.dp, RoundedCornerShape(16.dp), spotColor = Color(0x660284C7))
                            .testTag("next_question_button"),
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
                            Text(
                                text = if (isLastQuestion) "عرض النتيجة النهائية 🏆" else "السؤال التالي",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
