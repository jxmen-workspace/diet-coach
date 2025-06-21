package dev.jxmen.diet_coach.application.food.usecase

import dev.jxmen.diet_coach.domain.food.NutritionAnalysisResult

interface AnalyzeFoodImageUseCase {
    fun execute(image: ByteArray, contentType: String?): NutritionAnalysisResult
}
