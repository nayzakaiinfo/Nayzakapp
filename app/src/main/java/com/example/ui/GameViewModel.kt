package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.HistoricalFigure
import com.example.data.QuizQuestion
import com.example.data.QuoteItem
import com.example.data.repository.NayzakGameRepository
import com.example.analytics.GoogleAnalytics4Manager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class Screen {
    HOME,
    PLAYING,
    RESULT
}

data class AnswerReview(
    val quoteItem: QuoteItem,
    val selectedFigure: HistoricalFigure?,
    val correctFigure: HistoricalFigure,
    val isCorrect: Boolean,
    val isTimeout: Boolean,
    val timeSpentSeconds: Int
)

data class ReactionData(
    val figure: HistoricalFigure,
    val quoteItem: QuoteItem,
    val isCorrect: Boolean,
    val isTimeout: Boolean,
    val reactionText: String,
    val pointsAwarded: Int
)

data class GameUiState(
    val currentScreen: Screen = Screen.HOME,
    val questions: List<QuizQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val secondsRemaining: Int = 15,
    val score: Int = 0,
    val streak: Int = 0,
    val maxStreak: Int = 0,
    val selectedOption: HistoricalFigure? = null,
    val isAnswerRevealed: Boolean = false,
    val reactionData: ReactionData? = null,
    val reviewList: List<AnswerReview> = emptyList(),
    val personalBestScore: Int = 0,
    val personalBestCorrect: Int = 0,
    val totalGamesPlayed: Int = 0,
    val selectedCategory: String = "الكل",
    val seenQuotesCount: Int = 0,
    val totalQuotesCount: Int = 0
) {
    val currentQuestion: QuizQuestion?
        get() = questions.getOrNull(currentQuestionIndex)

    val isLastQuestion: Boolean
        get() = currentQuestionIndex >= questions.size - 1

    val correctCount: Int
        get() = reviewList.count { it.isCorrect }

    val wrongCount: Int
        get() = reviewList.count { !it.isCorrect }

    val accuracyPercentage: Int
        get() = if (reviewList.isNotEmpty()) (correctCount * 100) / reviewList.size else 0

    val averageTimeSeconds: Float
        get() = if (reviewList.isNotEmpty()) reviewList.map { it.timeSpentSeconds }.average().toFloat() else 0f

    val fastestTimeSeconds: Int
        get() = reviewList.filter { it.isCorrect }.minOfOrNull { it.timeSpentSeconds } ?: 0

    val playerTitle: String
        get() = when (correctCount) {
            10 -> "🏆 أسطورة الفلاسفة - نيزك الذكاء الخارق!"
            9, 8 -> "🌟 داهية تاريخية - عقلك يوزن بلد!"
            7, 6 -> "💡 مفكّر صاعد - معلوماتك من ذهب!"
            5, 4 -> "📚 طالب حكمة - سقراط بيقلك راجع كتبك!"
            else -> "🍎 نيوتن انصدم - الجاذبية شدت النقط لتحت!"
        }
}

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NayzakGameRepository(application)
    private val prefs = application.getSharedPreferences("nayzak_game_prefs", Context.MODE_PRIVATE)
    private val analyticsManager = GoogleAnalytics4Manager.getInstance(application)

    private val _uiState = MutableStateFlow(
        GameUiState(
            personalBestScore = prefs.getInt("best_score", 0),
            personalBestCorrect = prefs.getInt("best_correct", 0),
            totalGamesPlayed = prefs.getInt("games_played", 0)
        )
    )
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            repository.initializeIfNeeded()
            refreshExplorationProgress()
        }
    }

    private suspend fun refreshExplorationProgress() {
        val progress = repository.getExplorationProgress()
        _uiState.update {
            it.copy(
                seenQuotesCount = progress.seenQuotesCount,
                totalQuotesCount = progress.totalQuotesCount,
                personalBestScore = maxOf(it.personalBestScore, progress.personalBestScore)
            )
        }
    }

    fun selectCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun startNewGame() {
        timerJob?.cancel()
        val category = _uiState.value.selectedCategory

        // Log GA4 Event: game_start
        analyticsManager.logGameStart(category)

        viewModelScope.launch {
            val newQuestions = repository.generateSmartRound(category = category, count = 10)
            val newGamesCount = _uiState.value.totalGamesPlayed + 1
            prefs.edit().putInt("games_played", newGamesCount).apply()

            _uiState.update {
                it.copy(
                    currentScreen = Screen.PLAYING,
                    questions = newQuestions,
                    currentQuestionIndex = 0,
                    secondsRemaining = 15,
                    score = 0,
                    streak = 0,
                    maxStreak = 0,
                    selectedOption = null,
                    isAnswerRevealed = false,
                    reactionData = null,
                    reviewList = emptyList(),
                    totalGamesPlayed = newGamesCount
                )
            }

            startTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.secondsRemaining > 0) {
                delay(1000)
                _uiState.update { it.copy(secondsRemaining = it.secondsRemaining - 1) }
            }
            if (!_uiState.value.isAnswerRevealed) {
                onTimeOut()
            }
        }
    }

    fun selectOption(figure: HistoricalFigure) {
        if (_uiState.value.isAnswerRevealed) return
        timerJob?.cancel()

        val state = _uiState.value
        val question = state.currentQuestion ?: return
        val isCorrect = figure == question.correctFigure
        val timeSpent = 15 - state.secondsRemaining

        val newStreak = if (isCorrect) state.streak + 1 else 0
        val newMaxStreak = maxOf(state.maxStreak, newStreak)

        val points = if (isCorrect) {
            100 + (state.secondsRemaining * 10) + (newStreak * 20)
        } else {
            0
        }

        val reactionFigure = if (isCorrect) question.correctFigure else figure
        val reactionText = HistoricalFigure.randomReaction(reactionFigure, isCorrect = isCorrect, isTimeout = false)

        val review = AnswerReview(
            quoteItem = question.quoteItem,
            selectedFigure = figure,
            correctFigure = question.correctFigure,
            isCorrect = isCorrect,
            isTimeout = false,
            timeSpentSeconds = timeSpent.coerceAtLeast(1)
        )

        // Record stats to Room database
        viewModelScope.launch {
            repository.recordAnswer(question.quoteItem.quote, isCorrect)
        }

        _uiState.update {
            it.copy(
                selectedOption = figure,
                isAnswerRevealed = true,
                score = it.score + points,
                streak = newStreak,
                maxStreak = newMaxStreak,
                reviewList = it.reviewList + review,
                reactionData = ReactionData(
                    figure = reactionFigure,
                    quoteItem = question.quoteItem,
                    isCorrect = isCorrect,
                    isTimeout = false,
                    reactionText = reactionText,
                    pointsAwarded = points
                )
            )
        }
    }

    private fun onTimeOut() {
        val state = _uiState.value
        val question = state.currentQuestion ?: return

        val reactionFigure = question.correctFigure
        val reactionText = HistoricalFigure.randomReaction(reactionFigure, isCorrect = false, isTimeout = true)

        val review = AnswerReview(
            quoteItem = question.quoteItem,
            selectedFigure = null,
            correctFigure = question.correctFigure,
            isCorrect = false,
            isTimeout = true,
            timeSpentSeconds = 15
        )

        viewModelScope.launch {
            repository.recordAnswer(question.quoteItem.quote, false)
        }

        _uiState.update {
            it.copy(
                selectedOption = null,
                isAnswerRevealed = true,
                streak = 0,
                reviewList = it.reviewList + review,
                reactionData = ReactionData(
                    figure = reactionFigure,
                    quoteItem = question.quoteItem,
                    isCorrect = false,
                    isTimeout = true,
                    reactionText = reactionText,
                    pointsAwarded = 0
                )
            )
        }
    }

    fun nextQuestion() {
        val state = _uiState.value
        if (state.isLastQuestion) {
            val currentScore = state.score
            val currentCorrect = state.correctCount
            val bestScore = maxOf(state.personalBestScore, currentScore)
            val bestCorrect = maxOf(state.personalBestCorrect, currentCorrect)

            prefs.edit()
                .putInt("best_score", bestScore)
                .putInt("best_correct", bestCorrect)
                .apply()

            // Record round in Room
            viewModelScope.launch {
                repository.recordGameRound(
                    category = state.selectedCategory,
                    score = currentScore,
                    correctCount = currentCorrect,
                    maxStreak = state.maxStreak
                )
                refreshExplorationProgress()
            }

            // Log GA4 Event: game_complete
            analyticsManager.logGameComplete(
                score = currentScore,
                correctCount = currentCorrect,
                maxStreak = state.maxStreak,
                accuracyPercentage = state.accuracyPercentage
            )

            _uiState.update {
                it.copy(
                    currentScreen = Screen.RESULT,
                    reactionData = null,
                    personalBestScore = bestScore,
                    personalBestCorrect = bestCorrect
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    currentQuestionIndex = it.currentQuestionIndex + 1,
                    secondsRemaining = 15,
                    selectedOption = null,
                    isAnswerRevealed = false,
                    reactionData = null
                )
            }
            startTimer()
        }
    }

    fun navigateToHome() {
        timerJob?.cancel()
        viewModelScope.launch {
            refreshExplorationProgress()
        }
        _uiState.update {
            it.copy(
                currentScreen = Screen.HOME,
                reactionData = null,
                selectedOption = null,
                isAnswerRevealed = false
            )
        }
    }

    fun resetQuestionHistory() {
        viewModelScope.launch {
            repository.resetQuestionHistory()
            refreshExplorationProgress()
        }
    }

    /**
     * Logs the GA4 'share_click' event when player clicks the share button.
     */
    fun logShareClick() {
        val state = _uiState.value
        analyticsManager.logShareClick(score = state.score, playerTitle = state.playerTitle)
    }

    /**
     * Updates the GA4 Measurement ID (format: G-XXXXXXXXXX).
     */
    fun setGa4MeasurementId(id: String, secret: String = "") {
        analyticsManager.setMeasurementId(id, secret)
    }

    fun getGa4MeasurementId(): String = analyticsManager.measurementId

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
