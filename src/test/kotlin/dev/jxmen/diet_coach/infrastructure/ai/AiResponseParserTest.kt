package dev.jxmen.diet_coach.infrastructure.ai

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.jxmen.diet_coach.domain.food.exception.UnprocessableFoodImageException
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class AiResponseParserTest {

    val parser = AiResponseParser(jacksonObjectMapper())

    @Test
    fun testWithInvalidJson() {
        val str = "이미지를 분석할 수 없습니다."

        assertThrows<UnprocessableFoodImageException> { parser.parseFromFoodImage(str) }
    }

    @Test
    fun testWithValidJson() {
        val str = """
           ```json
            {
              "calories": 500,
              "carbohydrate": 60,
              "protein": 20,
              "fat": 15,
              "summary": "이 음식은 볶음밥으로, 주로 쌀, 당근, 계란, 닭고기, 양파 등이 포함되어 있습니다.",
              "foodItems": [
                {
                  "name": "쌀",
                  "grams": 150,
                  "nutrients": {
                    "calories": 200,
                    "carbohydrate": 45,
                    "protein": 4,
                    "fat": 0.5
                  }
                },
                {
                  "name": "닭고기",
                  "grams": 50,
                  "nutrients": {
                    "calories": 100,
                    "carbohydrate": 0,
                    "protein": 20,
                    "fat": 2
                  }
                },
                {
                  "name": "계란",
                  "grams": 50,
                  "nutrients": {
                    "calories": 70,
                    "carbohydrate": 1,
                    "protein": 6,
                    "fat": 5
                  }
                },
                {
                  "name": "당근",
                  "grams": 30,
                  "nutrients": {
                    "calories": 15,
                    "carbohydrate": 4,
                    "protein": 0.5,
                    "fat": 0.1
                  }
                },
                {
                  "name": "양파",
                  "grams": 20,
                  "nutrients": {
                    "calories": 10,
                    "carbohydrate": 2,
                    "protein": 0.3,
                    "fat": 0
                  }
                }
              ]
            }
            ```
            """.trimIndent()


        assertDoesNotThrow {
            val result = parser.parseFromFoodImage(str)

            // NutritionAnalysisResult 상위 항목 테스트
            assertAll(
                { assertEquals(result.calories, 500.0) },
                { assertEquals(result.carbohydrate, 60.0) },
                { assertEquals(result.protein, 20.0) },
                { assertEquals(result.fat, 15.0) },
                { assertEquals(result.summary, "이 음식은 볶음밥으로, 주로 쌀, 당근, 계란, 닭고기, 양파 등이 포함되어 있습니다.") }
            )

            // foodItems 리스트 크기 확인
            assertEquals(5, result.foodItems.size)

            // 각 FoodItem 항목 테스트
            val rice = result.foodItems[0]
            val chicken = result.foodItems[1]
            val egg = result.foodItems[2]
            val carrot = result.foodItems[3]
            val onion = result.foodItems[4]

            assertAll(
                {
                    // 쌀
                    assertEquals("쌀", rice.name)
                    assertEquals(150.0, rice.grams)
                    assertEquals(200.0, rice.nutrients.calories)
                    assertEquals(45.0, rice.nutrients.carbohydrate)
                    assertEquals(4.0, rice.nutrients.protein)
                    assertEquals(0.5, rice.nutrients.fat)
                },
                {
                    // 닭고기
                    assertEquals("닭고기", chicken.name)
                    assertEquals(50.0, chicken.grams)
                    assertEquals(100.0, chicken.nutrients.calories)
                    assertEquals(0.0, chicken.nutrients.carbohydrate)
                    assertEquals(20.0, chicken.nutrients.protein)
                    assertEquals(2.0, chicken.nutrients.fat)
                },
                {
                    // 계란
                    assertEquals("계란", egg.name)
                    assertEquals(50.0, egg.grams)
                    assertEquals(70.0, egg.nutrients.calories)
                    assertEquals(1.0, egg.nutrients.carbohydrate)
                    assertEquals(6.0, egg.nutrients.protein)
                    assertEquals(5.0, egg.nutrients.fat)
                },
                {
                    // 당근
                    assertEquals("당근", carrot.name)
                    assertEquals(30.0, carrot.grams)
                    assertEquals(15.0, carrot.nutrients.calories)
                    assertEquals(4.0, carrot.nutrients.carbohydrate)
                    assertEquals(0.5, carrot.nutrients.protein)
                    assertEquals(0.1, carrot.nutrients.fat)
                },
                {
                    // 양파
                    assertEquals("양파", onion.name)
                    assertEquals(20.0, onion.grams)
                    assertEquals(10.0, onion.nutrients.calories)
                    assertEquals(2.0, onion.nutrients.carbohydrate)
                    assertEquals(0.3, onion.nutrients.protein)
                    assertEquals(0.0, onion.nutrients.fat)
                }
            )
        }
    }

}
