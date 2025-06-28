package dev.jxmen.diet_coach.infrastructure.ai

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.jxmen.diet_coach.domain.food.NutritionAnalysisResult
import dev.jxmen.diet_coach.domain.food.exception.UnprocessableFoodImageException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * AI 응답 JSON 문자열을 NutritionAnalysisResult 객체로 변환하는 유틸리티 클래스
 */
@Component
class AiResponseParser(
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
) {

    companion object {
        private val logger = LoggerFactory.getLogger(AiResponseParser::class.java)
    }

    /**
     * 음식 이미지 응답 JSON 객체를 변환
     *
     * @param str AI 응답 JSON 문자열
     * @return NutritionAnalysisResult 객체
     * @throws UnprocessableFoodImageException 처리할 수 없을 경우
     */
    fun parseFromFoodImage(str: String): NutritionAnalysisResult {
        logger.debug("ai response: $str")

        // JSON Wrapping 제거
        val cleanJson = str.replace("```json", "")
            .replace("```", "")
            .trim()

        return try {
            objectMapper.readValue(cleanJson, NutritionAnalysisResult::class.java)
        } catch (e: JsonParseException) {
            logger.warn("JSON 파싱에 실패했습니다. 데이터: $str", e)
            throw UnprocessableFoodImageException()
        }
    }
}
