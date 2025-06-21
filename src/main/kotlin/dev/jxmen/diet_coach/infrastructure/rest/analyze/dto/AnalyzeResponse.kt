package dev.jxmen.diet_coach.infrastructure.rest.analyze.dto

data class AnalyzeResponse(
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
)

