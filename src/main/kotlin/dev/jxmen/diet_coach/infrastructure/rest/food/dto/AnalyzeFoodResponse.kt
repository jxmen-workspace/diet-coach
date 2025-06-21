package dev.jxmen.diet_coach.infrastructure.rest.food.dto

import dev.jxmen.diet_coach.domain.food.NutritionAnalysisResult

data class AnalyzeFoodResponse(
    val calories: Double,

    /**
     * 탄수화물
     */
    val carbohydrate: Double,

    /**
     * 단백질
     */
    val protein: Double,

    /**
     * 지방
     */
    val fat: Double // 지방
) {
    companion object {
        fun from(result: NutritionAnalysisResult): AnalyzeFoodResponse {
           return AnalyzeFoodResponse(
               calories = result.calories,
               carbohydrate = result.carbohydrate,
               protein = result.protein,
               fat = result.fat
           )
        }
    }
}

