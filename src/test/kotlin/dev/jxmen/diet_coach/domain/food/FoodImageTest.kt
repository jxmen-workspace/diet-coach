package dev.jxmen.diet_coach.domain.food

import dev.jxmen.diet_coach.domain.food.exception.EmptyImageException
import dev.jxmen.diet_coach.domain.food.exception.UnsupportedImageTypeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class FoodImageTest {

    @Test
    fun `빈 이미지로 생성 시 예외 발생`() {
        assertThrows<EmptyImageException> {
            FoodImage.create(ByteArray(0), "image/png")
        }
    }

    @Test
    fun `지원하지 않는 이미지 타입으로 생성 시 예외 발생`() {
        assertThrows<UnsupportedImageTypeException> {
            FoodImage.create(ByteArray(1), "image/tiff")
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["image/png", "image/jpeg", "image/gif", "image/webp"])
    fun `지원하는 이미지 형식이고 이미지가 비어있지 않을 경우 예외 발생X`(contentType: String) {
        assertDoesNotThrow {
            FoodImage.create(ByteArray(1), contentType)
        }
    }
}
