package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HistoricalFigure
import com.example.ui.GameUiState
import com.example.ui.components.AnswerReactionDialog
import com.example.ui.components.HistoricalFigureAvatar
import com.example.ui.components.QuizTimerBar
import com.example.ui.theme.GameCorrect
import com.example.ui.theme.GameWrong
import com.example.ui.theme.LightBlueBorder
import com.example.ui.theme.LightBlueBorderGlow
import com.example.ui.theme.LightBlueButton
import com.example.ui.theme.LightBlueCard
import com.example.ui.theme.LightBlueElevated
import com.example.ui.theme.LightBlueSubtext
import com.example.ui.theme.LightBlueText
import com.example.ui.theme.LightBlueTextDark

@Composable
fun QuizScreen(
    uiState: GameUiState,
    onSelectOption: (HistoricalFigure) -> Unit,
    onNextQuestion: () -> Unit,
    onExitGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showHint by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    // Intercept hardware and gesture back press
    BackHandler {
        showExitDialog = true
    }

    val currentQ = uiState.currentQuestion

    Box(
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar: Back, Question Counter, Score & Streak (Light Blue Theme)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { showExitDialog = true },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(LightBlueCard)
                        .border(1.8.dp, LightBlueBorder, CircleShape)
                        .testTag("exit_game_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "الخروج للقائمة الرئيسية",
                        tint = LightBlueButton,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Question Counter Pill (Light Blue + Navy)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(LightBlueCard)
                        .border(1.8.dp, LightBlueBorder, RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "السؤال ${uiState.currentQuestionIndex + 1} من ${uiState.questions.size}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = LightBlueTextDark
                    )
                }

                // Score + Streak Indicator (Light Blue + Navy)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (uiState.streak > 1) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(LightBlueCard)
                                .border(1.5.dp, LightBlueBorder, RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = "سلسلة",
                                    tint = Color(0xFFEA580C),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "x${uiState.streak}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFEA580C)
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(LightBlueCard)
                            .border(1.5.dp, LightBlueBorder, RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${uiState.score} نقطة",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Black,
                            color = LightBlueTextDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Stepped Dot Progress Bar for 10 questions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (i in uiState.questions.indices) {
                    val isPast = i < uiState.currentQuestionIndex
                    val isCurrent = i == uiState.currentQuestionIndex
                    val dotColor = when {
                        isCurrent -> LightBlueButton
                        isPast -> {
                            val review = uiState.reviewList.getOrNull(i)
                            if (review?.isCorrect == true) GameCorrect else GameWrong
                        }
                        else -> Color(0xFFBAE6FD)
                    }

                    Box(
                        modifier = Modifier
                            .height(6.dp)
                            .weight(1f)
                            .padding(horizontal = 2.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(dotColor)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Countdown Timer Progress Bar (15s)
            QuizTimerBar(
                secondsRemaining = uiState.secondsRemaining,
                totalSeconds = 15
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mystery Quote Display Card (Light Blue Container with Dark Navy Typography)
            currentQ?.let { question ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(22.dp), spotColor = Color(0x330284C7))
                        .border(
                            width = 2.dp,
                            color = LightBlueBorder,
                            shape = RoundedCornerShape(22.dp)
                        ),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = LightBlueCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Category Tag & Hint Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(LightBlueElevated)
                                    .border(1.5.dp, LightBlueBorder, RoundedCornerShape(10.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "📌 ${question.quoteItem.category}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = LightBlueText
                                )
                            }

                            if (question.quoteItem.hint.isNotBlank()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(LightBlueElevated)
                                        .border(1.dp, LightBlueBorder, RoundedCornerShape(8.dp))
                                        .clickable { showHint = !showHint }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lightbulb,
                                        contentDescription = "تلميح",
                                        tint = LightBlueButton,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (showHint) "إخفاء التلميح" else "تلميح؟",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = LightBlueText,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Hint display
                        AnimatedVisibility(visible = showHint) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(LightBlueElevated)
                                    .border(1.2.dp, LightBlueBorder, RoundedCornerShape(10.dp))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = "💡 تلميح: ${question.quoteItem.hint}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = LightBlueTextDark,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Large Quote Text (Dark Navy for Maximum Readability on Light Blue)
                        Text(
                            text = "“ ${question.quoteItem.quote} ”",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = LightBlueTextDark,
                            textAlign = TextAlign.Center,
                            lineHeight = 34.sp,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "من قائل هذه العبارة؟",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = LightBlueSubtext
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 4 Answer Options in 2x2 Grid (Light Blue Cards)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val rows = question.options.chunked(2)
                    rows.forEach { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            pair.forEach { figure ->
                                val isSelected = uiState.selectedOption == figure
                                val isCorrect = figure == question.correctFigure

                                AnswerOptionCard(
                                    figure = figure,
                                    isSelected = isSelected,
                                    isCorrect = isCorrect,
                                    isRevealed = uiState.isAnswerRevealed,
                                    onClick = { onSelectOption(figure) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (pair.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Reaction Dialog Popup
        uiState.reactionData?.let { reaction ->
            AnswerReactionDialog(
                figure = reaction.figure,
                quoteItem = reaction.quoteItem,
                isCorrect = reaction.isCorrect,
                isTimeout = reaction.isTimeout,
                reactionText = reaction.reactionText,
                isLastQuestion = uiState.isLastQuestion,
                onNextQuestion = onNextQuestion
            )
        }

        // Exit confirmation dialog (Light Blue)
        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = {
                    Text(
                        text = "الخروج من الجولة؟",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = LightBlueTextDark
                    )
                },
                text = {
                    Text(
                        text = "إذا خرجت الآن رح تخسر نقاط الجولة الحالية وسلسلة الكومبو. متأكد بدك تطلع؟",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LightBlueText
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        showExitDialog = false
                        onExitGame()
                    }) {
                        Text("نعم، اخرج", color = GameWrong, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showExitDialog = false }) {
                        Text("متابعة اللعب", color = LightBlueButton, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = LightBlueCard,
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@Composable
private fun AnswerOptionCard(
    figure: HistoricalFigure,
    isSelected: Boolean,
    isCorrect: Boolean,
    isRevealed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val targetScale = if (isSelected && isRevealed) 1.02f else 1f
    val animatedScale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "card_scale"
    )

    val (borderColor, containerColor) = when {
        isRevealed && isCorrect -> GameCorrect to Color(0xFFD1FAE5)
        isRevealed && isSelected && !isCorrect -> GameWrong to Color(0xFFFEE2E2)
        isSelected -> LightBlueBorder to LightBlueElevated
        else -> LightBlueBorder to LightBlueCard
    }

    Card(
        modifier = modifier
            .scale(animatedScale)
            .height(138.dp)
            .shadow(6.dp, RoundedCornerShape(18.dp), spotColor = Color(0x330284C7))
            .border(
                width = if (isSelected || (isRevealed && isCorrect)) 2.5.dp else 1.8.dp,
                color = borderColor,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(enabled = !isRevealed, onClick = onClick)
            .testTag("option_${figure.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                HistoricalFigureAvatar(
                    figure = figure,
                    size = 52.dp,
                    showBorderGlow = isSelected
                )

                if (isRevealed) {
                    if (isCorrect) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "صحيح",
                            tint = GameCorrect,
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.BottomEnd)
                                .background(Color.White, CircleShape)
                        )
                    } else if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "خاطئ",
                            tint = GameWrong,
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.BottomEnd)
                                .background(Color.White, CircleShape)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = figure.nameAr,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Black,
                color = LightBlueTextDark,
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            Text(
                text = figure.titleAr,
                style = MaterialTheme.typography.labelSmall,
                color = LightBlueSubtext,
                textAlign = TextAlign.Center,
                fontSize = 10.sp,
                maxLines = 1
            )
        }
    }
}
