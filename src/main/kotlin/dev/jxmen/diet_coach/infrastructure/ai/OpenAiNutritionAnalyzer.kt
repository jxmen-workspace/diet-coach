package dev.jxmen.diet_coach.infrastructure.ai

import dev.jxmen.diet_coach.domain.food.FoodImage
import dev.jxmen.diet_coach.domain.food.FoodItem
import dev.jxmen.diet_coach.domain.food.FoodItems
import dev.jxmen.diet_coach.domain.food.Nutrients
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

                전체 영양성분:
                칼로리(kcal): [숫자만]
                탄수화물(g): [숫자만]
                단백질(g): [숫자만]
                지방(g): [숫자만]
                ---
                음식 요약: [한 줄 요약]
                음식 항목:
                - [음식명] [그램수]g
                영양성분:
                칼로리(kcal): [숫자만]
                탄수화물(g): [숫자만]
                단백질(g): [숫자만]
                지방(g): [숫자만]
                """.trimIndent()
            )
            .media(listOf(Media(
                MimeTypeUtils.parseMimeType(foodImage.contentType.str),
                ByteArrayResource(foodImage.content)))
            )
            .build()
        val prompt = Prompt(userMessage)
        val response = chatModel.call(prompt)

        val text = response.result.output.text!!.trim()
        val sections = text.split("---")

        // 전체 영양소 정보 파싱
        val totalNutritionLines = sections[0].trim().lines().drop(1) // "전체 영양성분:" 라인 제외

        // 음식 정보 파싱
        val foodSection = sections[1].trim().lines()
        val summary = foodSection[0].substringAfter("음식 요약: ").trim()

        val foodItems = mutableListOf<FoodItem>()
        var currentIndex = 2 // 음식 항목 시작 라인

        while (currentIndex < foodSection.size) {
            val itemLine = foodSection[currentIndex]
            if (!itemLine.startsWith("- ")) break

            val (name, gramsWithUnit) = itemLine.substring(2).split(" ")
            val grams = gramsWithUnit.removeSuffix("g").toDouble()

            // 각 음식의 영양성분 파싱
            val itemNutrients = extractNutrients(foodSection.subList(currentIndex + 2, currentIndex + 6))

            foodItems.add(FoodItem(
                name = name,
                grams = grams,
                nutrients = itemNutrients
            ))

            currentIndex += 7 // 다음 음식 항목으로 이동
        }

        return NutritionAnalysisResult(
            calories = extractNumber(totalNutritionLines[0]),
            carbohydrate = extractNumber(totalNutritionLines[1]),
            protein = extractNumber(totalNutritionLines[2]),
            fat = extractNumber(totalNutritionLines[3]),
            foodItems = FoodItems(
                summary = summary,
                items = foodItems
            )
        )
    }

    private fun extractNutrients(lines: List<String>): Nutrients {
        return Nutrients(
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
