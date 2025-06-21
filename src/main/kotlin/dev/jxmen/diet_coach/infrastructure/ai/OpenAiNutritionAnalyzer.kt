package dev.jxmen.diet_coach.infrastructure.ai

import dev.jxmen.diet_coach.domain.food.FoodImage
import dev.jxmen.diet_coach.domain.food.NutritionAnalysisResult
import dev.jxmen.diet_coach.domain.food.service.NutritionAnalyzer
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.content.Media
import org.springframework.core.io.ByteArrayResource
import org.springframework.stereotype.Component
import org.springframework.util.MimeTypeUtils


@Component
class OpenAiNutritionAnalyzer(
    private val chatModel: ChatModel,
) : NutritionAnalyzer {

    override fun analyze(foodImage: FoodImage): NutritionAnalysisResult {
        val userMessage = UserMessage.builder()
            .text(
                """
                이 이미지에 있는 음식의 영양 성분을 분석해주세요.
                다음 형식으로 응답해주세요:

                칼로리(kcal): [숫자만]
                탄수화물(g): [숫자만]
                단백질(g): [숫자만]
                지방(g): [숫자만]

                숫자 외의 다른 설명은 하지 말아주세요.
                """.trimIndent(),
            )
            .media(listOf(Media(
                MimeTypeUtils.parseMimeType(foodImage.contentType.str),
                ByteArrayResource(foodImage.content)))
            )
            .build()
        val prompt = Prompt(userMessage)
        val response = chatModel.call(prompt)

        val lines = response.result.output.text!!.trim().lines()

        return NutritionAnalysisResult(
            calories = extractNumber(lines[0]),
            carbohydrate = extractNumber(lines[1]),
            protein = extractNumber(lines[2]),
            fat = extractNumber(lines[3])
        )
    }

    private fun extractNumber(line: String): Double {
        return line.split(":")[1].trim().toDouble()
    }
}
