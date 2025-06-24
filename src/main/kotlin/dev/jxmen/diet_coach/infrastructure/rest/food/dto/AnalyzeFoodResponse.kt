package dev.jxmen.diet_coach.infrastructure.rest.food.dto

import dev.jxmen.diet_coach.domain.food.FoodItem
import dev.jxmen.diet_coach.domain.food.FoodItems
import dev.jxmen.diet_coach.domain.food.Nutrients
import dev.jxmen.diet_coach.domain.food.NutritionAnalysisResult

data class FoodItemNutrientsResponse(
    val calories: Double,
    val carbohydrate: Double,
    val protein: Double,
    val fat: Double
) {
    companion object {
        fun from(nutrients: Nutrients) = FoodItemNutrientsResponse(
            calories = nutrients.calories,
            carbohydrate = nutrients.carbohydrate,
            protein = nutrients.protein,
            fat = nutrients.fat
        )
    }
}

data class FoodItemResponse(
    val name: String,
    val grams: Double,
    val nutrients: FoodItemNutrientsResponse
) {
    companion object {
        fun from(foodItem: FoodItem) = FoodItemResponse(
            name = foodItem.name,
            grams = foodItem.grams,
            nutrients = FoodItemNutrientsResponse.from(foodItem.nutrients)
        )
    }
}

data class FoodItemsResponse(
    val summary: String,
    val items: List<FoodItemResponse>
) {
    companion object {
        fun from(foodItems: FoodItems) = FoodItemsResponse(
            summary = foodItems.summary,
            items = foodItems.items.map { FoodItemResponse.from(it) }
        )
    }
}

data class AnalyzeFoodResponse(
    val calories: Double,
    val carbohydrate: Double,
    val protein: Double,
    val fat: Double,
    val foodItems: FoodItemsResponse
) {
    companion object {
        fun from(result: NutritionAnalysisResult): AnalyzeFoodResponse {
            return AnalyzeFoodResponse(
                calories = result.calories,
                carbohydrate = result.carbohydrate,
                protein = result.protein,
                fat = result.fat,
                foodItems = FoodItemsResponse.from(result.foodItems)
            )
        }
    }
}
