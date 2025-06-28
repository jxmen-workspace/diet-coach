package dev.jxmen.diet_coach.infrastructure.rest.common

import dev.jxmen.diet_coach.common.dto.ApiResponse
import dev.jxmen.diet_coach.common.dto.ErrorType
import dev.jxmen.diet_coach.domain.food.exception.EmptyImageException
import dev.jxmen.diet_coach.domain.food.exception.UnprocessableFoodImageException
import dev.jxmen.diet_coach.domain.food.exception.UnsupportedImageTypeException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.logging.LogLevel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler(
    private val logger: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
) {

    @ExceptionHandler(EmptyImageException::class)
    fun handleEmptyImageException(ex: EmptyImageException): ResponseEntity<ApiResponse<Nothing>> {
        return handleError(ErrorType.REQUIRED_IMAGE_MISSING, ex)
    }

    @ExceptionHandler(UnsupportedImageTypeException::class)
    fun handleUnsupportedImageTypeException(ex: UnsupportedImageTypeException): ResponseEntity<ApiResponse<Nothing>> {
        return handleError(ErrorType.UNSUPPORTED_IMAGE_TYPE, ex)
    }

    @ExceptionHandler(UnprocessableFoodImageException::class)
    fun handleUnprocessableFoodImageException(ex: UnprocessableFoodImageException): ResponseEntity<ApiResponse<Nothing>> {
        return handleError(ErrorType.UNPROCESSABLE_FOOD_IMAGE, ex)
    }

    private fun handleError(errorType: ErrorType, ex: Exception): ResponseEntity<ApiResponse<Nothing>> {
        when (errorType.logLevel) {
            LogLevel.ERROR -> logger.error(ex.message)
            LogLevel.WARN -> logger.warn(ex.message)
            LogLevel.INFO -> logger.info(ex.message)
            LogLevel.DEBUG -> logger.debug(ex.message)
            LogLevel.TRACE -> logger.trace(ex.message)
            LogLevel.FATAL -> logger.error("[FATAL] ${ex.message}")
            else -> logger.error(ex.message)
        }

        return ResponseEntity.status(errorType.status)
            .body(ApiResponse.error(errorType))
    }
}
