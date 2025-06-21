package dev.jxmen.diet_coach.domain.food

/**
 * 성분 분석 결과
 */
data class NutritionAnalysisResult(
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
    val fat: Double
)
