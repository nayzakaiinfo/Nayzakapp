package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.ui.GameViewModel
import com.example.ui.Screen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.ResultScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SpaceBackground

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                // Fully Arabic (RTL) Layout Direction
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(SpaceBackground),
                        containerColor = SpaceBackground
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(SpaceBackground)
                                .padding(innerPadding)
                                .safeDrawingPadding(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth()
                                    .widthIn(max = 560.dp)
                            ) {
                                NayzakApp(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NayzakApp(viewModel: GameViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    AnimatedContent(
        targetState = uiState.currentScreen,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "screen_navigation"
    ) { screen ->
        when (screen) {
            Screen.HOME -> {
                HomeScreen(
                    uiState = uiState,
                    onStartGame = { viewModel.startNewGame() },
                    onSelectCategory = { category -> viewModel.selectCategory(category) },
                    onResetProgress = { viewModel.resetQuestionHistory() }
                )
            }
            Screen.PLAYING -> {
                QuizScreen(
                    uiState = uiState,
                    onSelectOption = { figure -> viewModel.selectOption(figure) },
                    onNextQuestion = { viewModel.nextQuestion() },
                    onExitGame = { viewModel.navigateToHome() }
                )
            }
            Screen.RESULT -> {
                ResultScreen(
                    uiState = uiState,
                    onPlayAgain = { viewModel.startNewGame() },
                    onHome = { viewModel.navigateToHome() },
                    onShareClick = { viewModel.logShareClick() }
                )
            }
        }
    }
}
