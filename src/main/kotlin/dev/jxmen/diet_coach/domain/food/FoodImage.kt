package dev.jxmen.diet_coach.domain.food

import dev.jxmen.diet_coach.domain.food.exception.EmptyImageException
import dev.jxmen.diet_coach.domain.food.exception.UnsupportedImageTypeException

data class FoodImage private constructor(
    val content: ByteArray,
    val contentType: ContentType,
) {
    companion object {
        fun create(content: ByteArray, contentType: String?): FoodImage {
            if (content.isEmpty()) {
                throw EmptyImageException()
            }

            return FoodImage(content, ContentType.from(contentType))
        }
    }

    enum class ContentType(val str: String) {
        PNG("image/png"),
        JPEG("image/jpeg"),
        GIF("image/gif"),
        WEBP("image/webp");

        companion object {
            fun from(s: String?): ContentType {
                return entries.find { it.str == s }
                    ?: throw UnsupportedImageTypeException(s)
            }
        }
    }

    // NOTE: byteArray을 사용할 경우 동일한 바이트 배열이라도 다른 객체로 취급될 수 있음.
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FoodImage

        if (!content.contentEquals(other.content)) return false
        if (contentType != other.contentType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = content.contentHashCode()
        result = 31 * result + contentType.hashCode()
        return result
    }

}
