package dev.jxmen.diet_coach.infrastructure.rest.food

import dev.jxmen.diet_coach.application.food.usecase.AnalyzeFoodImageUseCase
import dev.jxmen.diet_coach.common.dto.ApiResponse
import dev.jxmen.diet_coach.infrastructure.rest.food.dto.AnalyzeFoodResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class AnalyzeFoodController(
    private val analyzeFoodImageUseCase: AnalyzeFoodImageUseCase
) {

    @PostMapping("/api/v1/foods/analyze")
    fun analyze(@RequestParam("image") image: MultipartFile): ResponseEntity<out ApiResponse<out AnalyzeFoodResponse>?> {
        val result = analyzeFoodImageUseCase.execute(image.bytes, image.contentType)

        return ResponseEntity.status(HttpStatus.OK)
            .body(
                ApiResponse.success(
                    AnalyzeFoodResponse.from(result)
                )
            )
    }
}
