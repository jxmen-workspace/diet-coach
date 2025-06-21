package dev.jxmen.diet_coach.domain.food.exception

sealed class ImageValidationException(message: String) : RuntimeException(message)

class EmptyImageException : ImageValidationException("이미지가 비어있습니다.")

class UnsupportedImageTypeException(contentType: String?) : ImageValidationException(
    "지원하지 않는 이미지 형식입니다: $contentType"
)
