package dev.jxmen.diet_coach.common.dto

import org.springframework.boot.logging.LogLevel
import org.springframework.http.HttpStatus

enum class ErrorType(val status: HttpStatus, val code: ErrorCode, val message: String, val logLevel: LogLevel) {
    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "An unexpected error has occurred.", LogLevel.ERROR),

    // I: Image 관련 에러
    REQUIRED_IMAGE_MISSING(HttpStatus.BAD_REQUEST, ErrorCode.I001, "이미지는 반드시 있어야 합니다.", LogLevel.WARN),
    UNSUPPORTED_IMAGE_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ErrorCode.I002, "지원하지 않는 이미지 형식입니다.", LogLevel.WARN),
    UNPROCESSABLE_FOOD_IMAGE(HttpStatus.UNPROCESSABLE_ENTITY, ErrorCode.I003, "처리할 수 없는 음식 이미지입니다.", LogLevel.WARN),
}
