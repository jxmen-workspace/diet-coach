package dev.jxmen.diet_coach.domain.food.service

import dev.jxmen.diet_coach.domain.food.FoodImage
import dev.jxmen.diet_coach.domain.food.NutritionAnalysisResult

interface NutritionAnalyzer {
    fun analyze(foodImage: FoodImage): NutritionAnalysisResult
}
