package dev.jxmen.diet_coach.infrastructure.rest

import dev.jxmen.diet_coach.common.dto.ApiResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class VersionController {
    @Value("\${version}")
    private lateinit var version: String

    @GetMapping("/api/v1/version")
    fun getVersion(): ResponseEntity<ApiResponse<Map<String, String>>> {
        return ResponseEntity.ok(
            ApiResponse.success(
                mapOf("version" to version)
            )
        )
    }
}
