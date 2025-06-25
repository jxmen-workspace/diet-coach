package dev.jxmen.diet_coach.infrastructure.ai

import dev.jxmen.diet_coach.domain.food.FoodImage
import dev.jxmen.diet_coach.domain.food.NutritionAnalysisResult
import dev.jxmen.diet_coach.domain.food.service.NutritionAnalyzer
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.content.Media
import org.springframework.core.io.ByteArrayResource
import org.springframework.stereotype.Component
import org.springframework.util.MimeTypeUtils

@Component
class OpenAiNutritionAnalyzer(
    private val chatModel: ChatModel,
    private val aiResponseParser: AiResponseParser,
) : NutritionAnalyzer {

    companion object {
        private val logger = LoggerFactory.getLogger(OpenAiNutritionAnalyzer::class.java)
    }

    override fun analyze(foodImage: FoodImage): NutritionAnalysisResult {
        val userMessage = UserMessage.builder().text(
            """
                이 이미지에 있는 음식의 영양 성분을 분석해주세요.
                반드시 JSON 형식으로만 응답해주세요:

                {
                  "calories": [숫자],
                  "carbohydrate": [숫자],
                  "protein": [숫자],
                  "fat": [숫자],
                  "summary": "[요약]"
                  "foodItems": [
                      {
                          "name": "[음식명]",
                          "grams": [그램 수],
                          "nutrients": {
                              "calories": [숫자],
                              "carbohydrate": [숫자],
                              "protein": [숫자],
                              "fat": [숫자]
                          }
                      }
                  ]
                }
                """.trimIndent()
        ).media(
            listOf(
                Media(
                    MimeTypeUtils.parseMimeType(foodImage.contentType.str), ByteArrayResource(foodImage.content)
                )
            )
        ).build()

        val response: ChatResponse = chatModel.call(Prompt(userMessage))

        // Response를 JSON 형태로 매핑
        val text = response.result.output.text!!.trim()

        val promptTokens = response.metadata?.usage?.promptTokens ?: 0
        val completionTokens = response.metadata?.usage?.completionTokens ?: 0
        val totalTokens = promptTokens + completionTokens
        logger.info("Token usage - Prompt: $promptTokens, Completion: $completionTokens, Total: $totalTokens")

        return aiResponseParser.parseFromFoodImage(text)
    }
}
