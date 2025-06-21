package dev.jxmen.diet_coach.application.food.service

import dev.jxmen.diet_coach.application.food.usecase.AnalyzeFoodImageUseCase
import dev.jxmen.diet_coach.domain.food.FoodImage
import dev.jxmen.diet_coach.domain.food.NutritionAnalysisResult
import dev.jxmen.diet_coach.domain.food.service.NutritionAnalyzer
import org.springframework.stereotype.Service

@Service
class AnalyzeFoodImageService(
    private val nutritionAnalyzer: NutritionAnalyzer
): AnalyzeFoodImageUseCase {

    override fun execute(image: ByteArray, contentType: String?): NutritionAnalysisResult {
        val foodImage = FoodImage.create(image, contentType)

        return nutritionAnalyzer.analyze(foodImage)
    }

}
