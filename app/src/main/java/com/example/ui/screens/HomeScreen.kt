package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HistoricalFigure
import com.example.ui.GameUiState
import com.example.ui.components.HistoricalFigureAvatar
import com.example.ui.components.NayzakGrandLogo
import com.example.ui.theme.GameCorrect
import com.example.ui.theme.LightBlueBorder
import com.example.ui.theme.LightBlueBorderGlow
import com.example.ui.theme.LightBlueButton
import com.example.ui.theme.LightBlueCard
import com.example.ui.theme.LightBlueElevated
import com.example.ui.theme.LightBlueSubtext
import com.example.ui.theme.LightBlueText
import com.example.ui.theme.LightBlueTextDark

@Composable
fun HomeScreen(
    uiState: GameUiState,
    onStartGame: () -> Unit,
    onSelectCategory: (String) -> Unit = {},
    onResetProgress: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showRulesDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showResetConfirmDialog by remember { mutableStateOf(false) }
    var selectedPreviewFigure by remember { mutableStateOf<HistoricalFigure?>(null) }

    val displayedFigures = remember(uiState.selectedCategory) {
        HistoricalFigure.getByCategory(uiState.selectedCategory)
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
        // Logo Hero (Circular Black Badge inside Wreath is strictly preserved!)
        NayzakGrandLogo(
            wreathSize = 220.dp,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Smart Exploration Bank Progress Card (Light Blue Container with Dark Navy Text)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(18.dp), spotColor = Color(0x330284C7))
                .border(2.dp, LightBlueBorder, RoundedCornerShape(18.dp)),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = LightBlueCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = LightBlueButton,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "بنك الأسئلة الذكي (بدون تكرار)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Black,
                            color = LightBlueTextDark
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(LightBlueElevated)
                            .border(1.dp, LightBlueBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${uiState.seenQuotesCount} / ${uiState.totalQuotesCount}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = LightBlueText
                        )
                    }
                }

                val progressFraction = if (uiState.totalQuotesCount > 0) {
                    (uiState.seenQuotesCount.toFloat() / uiState.totalQuotesCount).coerceIn(0f, 1f)
                } else 0f

                LinearProgressIndicator(
                    progress = { progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = GameCorrect,
                    trackColor = LightBlueElevated
                )

                Text(
                    text = "يضمن نظام اللعبة عدم تكرار أي سؤال حتى تختم كامل بنك الاقتباسات!",
                    style = MaterialTheme.typography.labelSmall,
                    color = LightBlueSubtext,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Personal Best Record Banner (Light Blue Card on Yellow Canvas)
        if (uiState.personalBestScore > 0) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(LightBlueCard)
                    .border(2.dp, LightBlueBorder, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "أعلى نتيجة",
                            tint = LightBlueButton,
                            modifier = Modifier.size(26.dp)
                        )
                        Column {
                            Text(
                                text = "أعلى رقم قياسي",
                                style = MaterialTheme.typography.labelSmall,
                                color = LightBlueSubtext,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${uiState.personalBestScore} نقطة (${uiState.personalBestCorrect}/10)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = LightBlueTextDark
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(LightBlueElevated)
                            .border(1.5.dp, LightBlueBorder, CircleShape)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${uiState.totalGamesPlayed} جولات 🎮",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = LightBlueText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Category Selector Section (Light Blue Elements)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(LightBlueCard)
                        .border(1.5.dp, LightBlueBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Category,
                            contentDescription = null,
                            tint = LightBlueButton,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "اختر تصنيف الجولة 🎯",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Black,
                            color = LightBlueTextDark
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(LightBlueElevated)
                        .border(1.5.dp, LightBlueBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = uiState.selectedCategory,
                        style = MaterialTheme.typography.labelSmall,
                        color = LightBlueText,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Categories horizontal chip row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 2.dp)
            ) {
                items(HistoricalFigure.categories) { cat ->
                    val isSelected = uiState.selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) LightBlueButton else LightBlueCard)
                            .border(
                                width = if (isSelected) 2.dp else 1.2.dp,
                                color = LightBlueBorder,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { onSelectCategory(cat) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = cat,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else LightBlueTextDark
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Big Action Start Button (Vibrant Light Blue / Cerulean on Yellow)
        Button(
            onClick = onStartGame,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .shadow(14.dp, RoundedCornerShape(18.dp), spotColor = Color(0x660284C7))
                .border(2.dp, LightBlueBorderGlow, RoundedCornerShape(18.dp))
                .testTag("start_game_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightBlueButton,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(18.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ابدأ جولة جديدة (10 أسئلة)",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Historical Figures Carousel Showcase (Light Blue Cards)
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(LightBlueCard)
                        .border(1.5.dp, LightBlueBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "الشخصيات المتاحة (${displayedFigures.size}) 🎭",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Black,
                        color = LightBlueTextDark
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(LightBlueElevated)
                        .border(1.5.dp, LightBlueBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "اضغط للتعرف 👆",
                        style = MaterialTheme.typography.labelSmall,
                        color = LightBlueText,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(displayedFigures) { figure ->
                    FigureShowcaseCard(
                        figure = figure,
                        isSelected = selectedPreviewFigure == figure,
                        onClick = {
                            selectedPreviewFigure = if (selectedPreviewFigure == figure) null else figure
                        }
                    )
                }
            }

            // Expanded Preview details for clicked figure
            selectedPreviewFigure?.let { figure ->
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, figure.color, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LightBlueCard)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            HistoricalFigureAvatar(figure = figure, size = 52.dp)
                            Column {
                                Text(
                                    text = "${figure.nameAr} - ${figure.titleAr}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = LightBlueTextDark
                                )
                                Text(
                                    text = "${figure.eraAr} | ${figure.categoryAr}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = LightBlueSubtext
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "عينة من تعليقاته: « ${figure.reactionsCorrect.firstOrNull() ?: ""} »",
                            style = MaterialTheme.typography.bodySmall,
                            color = LightBlueText,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Rules Info Accordion (Light Blue Container with Deep Navy Text)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(LightBlueCard)
                .border(2.dp, LightBlueBorder, RoundedCornerShape(14.dp))
                .clickable { showRulesDialog = !showRulesDialog }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.HelpOutline,
                    contentDescription = null,
                    tint = LightBlueButton,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "كيف تلعب وتحقق أعلى نقاط؟",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = LightBlueTextDark
                )
            }
            Text(
                text = if (showRulesDialog) "▲" else "▼",
                color = LightBlueButton,
                fontWeight = FontWeight.Bold
            )
        }

        AnimatedVisibility(visible = showRulesDialog) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(LightBlueCard)
                    .border(1.5.dp, LightBlueBorder, RoundedCornerShape(14.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RuleItem(number = "١", text = "بنك الأسئلة الذكي يتذكر كل ما أجبته ولا يكرر أي سؤال حتى تستكشف كل الاقتباسات.")
                RuleItem(number = "٢", text = "يمكنك اختيار تصنيف محدد (فلسفة، علوم، أدب، قيادة، تفكير) أو لعب جولة منوعة.")
                RuleItem(number = "٣", text = "15 ثانية لكل سؤال.. وكل ما جاوبت أسرع، كل ما ربحت نقاط بونص سرعة!")
                RuleItem(number = "٤", text = "سلسلة الإجابات الصحيحة تمنحك مضاعف كومبو ناري 🔥.")
                RuleItem(number = "٥", text = "بعد كل جواب، رح تسمع رد مضحك من صاحب الشخصية باللهجة الشامية!")

                Spacer(modifier = Modifier.height(4.dp))

                // Reset Progress Action Button
                Button(
                    onClick = { showResetConfirmDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightBlueElevated,
                        contentColor = Color(0xFFDC2626)
                    )
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "إعادة تصفير سجل الأسئلة (بدء الاستكشاف من جديد)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Privacy Policy & Google Play Compliance Footer (Light Blue Pill)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(LightBlueCard)
                .border(1.5.dp, LightBlueBorder, RoundedCornerShape(14.dp))
                .clickable { showPrivacyDialog = true }
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Text(
                text = "🛡️ سياسة الخصوصية ومعلومات التطبيق",
                style = MaterialTheme.typography.labelSmall,
                color = LightBlueText,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Privacy Dialog (Light Blue)
        if (showPrivacyDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showPrivacyDialog = false },
                title = {
                    Text(
                        text = "سياسة الخصوصية - نيزك 🛡️",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = LightBlueTextDark
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "تطبيق نيزك (NAYZAK) يحترم خصوصيتك بالكامل ويلتزم بسياسات Google Play:",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = LightBlueTextDark
                        )
                        Text(
                            text = "• التطبيق يعمل بدون أي تسجيل دخول ولا يطلب أي بيانات شخصية (Zero PII).\n" +
                                   "• لا يتطلب أي أذونات وصول خاصة بالجهاز (Permissions-free).\n" +
                                   "• إحصائيات Google Analytics 4 (GA4): تسجل فقط أحداث اللعبة (بدء الجولة، إنهاء الجولة والنتيجة، النقر على زر المشاركة) بشكل مجهول الهوية بالكامل بدون أي حساب.\n" +
                                   "• محتوى التطبيق ثقافي، فكري، وأدبي بحت وخالٍ تماماً من أي محتوى مسيء.\n" +
                                   "• يتم حفظ تقدم الأسئلة والأرقام القياسية محلياً على جهازك باستخدام قاعدة بيانات Room.\n" +
                                   "• مناسب لجميع الفئات العمرية.\n" +
                                   "• للتواصل والدعم: contact@nayzak.app",
                            style = MaterialTheme.typography.bodySmall,
                            color = LightBlueText,
                            lineHeight = 18.sp
                        )
                    }
                },
                confirmButton = {
                    androidx.compose.material3.TextButton(onClick = { showPrivacyDialog = false }) {
                        Text("حسناً", color = LightBlueButton, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = LightBlueCard,
                shape = RoundedCornerShape(18.dp)
            )
        }

        // Reset Confirm Dialog
        if (showResetConfirmDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showResetConfirmDialog = false },
                title = {
                    Text(
                        text = "تأكيد إعادة التصفير 🔄",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = LightBlueTextDark
                    )
                },
                text = {
                    Text(
                        text = "هل تريد تصفير سجل الأسئلة المستكشفة للبدء من جديد وكأنك تلعب لأول مرة؟",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LightBlueText
                    )
                },
                confirmButton = {
                    androidx.compose.material3.TextButton(
                        onClick = {
                            onResetProgress()
                            showResetConfirmDialog = false
                        }
                    ) {
                        Text("نعم، صفر السجل", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(onClick = { showResetConfirmDialog = false }) {
                        Text("إلغاء", color = LightBlueText)
                    }
                },
                containerColor = LightBlueCard,
                shape = RoundedCornerShape(18.dp)
            )
        }
    }
}

@Composable
private fun FigureShowcaseCard(
    figure: HistoricalFigure,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(112.dp)
            .clickable(onClick = onClick)
            .border(
                width = if (isSelected) 2.5.dp else 1.5.dp,
                color = if (isSelected) figure.color else LightBlueBorder,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) LightBlueElevated else LightBlueCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HistoricalFigureAvatar(
                figure = figure,
                size = 56.dp,
                showBorderGlow = isSelected
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = figure.nameAr,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = LightBlueTextDark,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
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

@Composable
private fun RuleItem(number: String, text: String) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(LightBlueButton)
        ) {
            Text(
                text = number,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = LightBlueTextDark,
            lineHeight = 18.sp
        )
    }
}
