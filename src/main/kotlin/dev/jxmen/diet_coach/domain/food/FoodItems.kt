package dev.jxmen.diet_coach.domain.food

data class Nutrients(
    val calories: Double,
    val carbohydrate: Double,
    val protein: Double,
    val fat: Double
)

data class FoodItem (
    val name: String,
    val grams: Double,
    val nutrients: Nutrients
)

data class FoodItems(
    val summary: String,
    val items: List<FoodItem>
)
