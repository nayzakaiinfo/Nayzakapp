package com.example.data

import android.content.Context
import org.json.JSONArray

data class QuoteItem(
    val id: Int,
    val quote: String,
    val author: String,
    val figureId: String,
    val category: String,
    val hint: String = "",
    val funFact: String = ""
) {
    val figure: HistoricalFigure
        get() = HistoricalFigure.fromId(figureId)
}

data class QuizQuestion(
    val quoteItem: QuoteItem,
    val options: List<HistoricalFigure>,
    val correctFigure: HistoricalFigure
)

object QuoteRepository {
    private var cachedQuotes: List<QuoteItem>? = null

    fun loadQuotes(context: Context): List<QuoteItem> {
        cachedQuotes?.let { return it }

        return try {
            val jsonString = context.assets.open("quotes.json").bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)
            val list = mutableListOf<QuoteItem>()

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    QuoteItem(
                        id = obj.optInt("id", i + 1),
                        quote = obj.optString("quote", ""),
                        author = obj.optString("author", ""),
                        figureId = obj.optString("figureId", "socrates"),
                        category = obj.optString("category", "حكمة عامة"),
                        hint = obj.optString("hint", ""),
                        funFact = obj.optString("funFact", "")
                    )
                )
            }
            cachedQuotes = list
            list
        } catch (e: Exception) {
            val fallback = getFallbackQuotes()
            cachedQuotes = fallback
            fallback
        }
    }

    fun generateRound(context: Context, questionCount: Int = 10): List<QuizQuestion> {
        val allQuotes = loadQuotes(context)
        val selectedQuotes = allQuotes.shuffled().take(questionCount)

        return selectedQuotes.map { quoteItem ->
            val correctFigure = quoteItem.figure
            // Pick 3 distinct wrong figures from the remaining 5
            val otherFigures = HistoricalFigure.entries
                .filter { it != correctFigure }
                .shuffled()
                .take(3)

            val options = (otherFigures + correctFigure).shuffled()
            QuizQuestion(
                quoteItem = quoteItem,
                options = options,
                correctFigure = correctFigure
            )
        }
    }

    private fun getFallbackQuotes(): List<QuoteItem> = listOf(
        QuoteItem(1, "الحياة التي لا تُختبر لا تستحق أن تُعاش.", "سقراط", "socrates", "فلسفة وحكمة", "فيلسوف أثيني", "مؤسس الفلسفة الغربية"),
        QuoteItem(2, "كل ما أعرفه هو أنني لا أعرف شيئاً.", "سقراط", "socrates", "معرفة وفلسفة", "أستاذ أفلاطون", "اعترافه بالجهل كان سر حكمته"),
        QuoteItem(3, "أنا أفكّر، إذن أنا موجود.", "ديكارت", "descartes", "فلسفة الوجود", "مبتكر الكوجيتو", "أثبت وجود الذات بالتفكير"),
        QuoteItem(4, "الشك هو بداية الحكمة.", "ديكارت", "descartes", "منطق وفلسفة", "عالم رياضيات وفلسفة", "اخترع الإحداثيات الديكارتية"),
        QuoteItem(5, "كلمة مستحيل ليست في قاموسي، بل توجد فقط في معجم الحمقى.", "نابليون", "napoleon", "عزيمة وإرادة", "إمبراطور فرنسا", "انتصر في أوسترليتز"),
        QuoteItem(6, "القائد هو تاجر الأمل.", "نابليون", "napoleon", "قيادة وإلهام", "قائد تاريخي", "توج نفسه إمبراطوراً عام 1804"),
        QuoteItem(7, "المشاعر المكبوتة لا تموت أبداً، بل تُدفن حية ثم تظهر لاحقاً بطرق أكثر قبحاً.", "فرويد", "freud", "علم النفس", "مؤسس التحليل النفسي", "ابتكر أريكة التحليل الشهيرة"),
        QuoteItem(8, "الأحلام هي الطريق الملكي إلى معرفة العقل الباطن.", "فرويد", "freud", "عالم الأحلام", "صاحب كتاب تفسير الأحلام", "درس العقل اللاواعي"),
        QuoteItem(9, "الخيال أكثر أهمية من المعرفة، فالمعرفة محدودة، بينما الخيال يطوف العالم كله.", "أينشتاين", "einstein", "إبداع وفيزياء", "صاحب النسبية", "حاز نوبل عام 1921"),
        QuoteItem(10, "الجنون هو أن تفعل الشيء نفسه مراراً وتكراراً وتتوقع نتائج مختلفة.", "أينشتاين", "einstein", "منطق وتفكير", "عبقري القرن العشرين", "صاحب معادلة الطاقة"),
        QuoteItem(11, "لكل فعل رد فعل مساوٍ له في المقدار ومضاد له في الاتجاه.", "نيوتن", "newton", "فيزياء وحركة", "مكتشف الجاذبية", "القانون الثالث للحركة"),
        QuoteItem(12, "ما نعرفه قطرة، وما نجهله محيط كامل.", "نيوتن", "newton", "علوم وتواضع", "سقطت عليه التفاحة", "رئيس الجمعية الملكية البريطانية")
    )
}
