package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.HistoricalFigure
import com.example.data.QuoteRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("NAYZAK", appName)
  }

  @Test
  fun `verify quotes repository loads and generates 10 questions per round`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val round = QuoteRepository.generateRound(context, questionCount = 10)
    assertEquals(10, round.size)

    for (q in round) {
      assertNotNull(q.quoteItem.quote)
      assertTrue(q.quoteItem.quote.isNotEmpty())
      assertEquals(4, q.options.size)
      assertTrue(q.options.contains(q.correctFigure))
    }
  }

  @Test
  fun `verify historical figure reactions are non empty`() {
    for (figure in HistoricalFigure.entries) {
      val correctReaction = HistoricalFigure.randomReaction(figure, isCorrect = true)
      val wrongReaction = HistoricalFigure.randomReaction(figure, isCorrect = false)
      assertFalse(correctReaction.isBlank())
      assertFalse(wrongReaction.isBlank())
    }
  }
}
