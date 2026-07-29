package com.bank.recommendationService.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(description = "Ответ с рекомендациями пользователя")
public record RecommendationResponse(

        @Schema(
                description = "UUID пользователя",
                example = "cd515076-5d8a-44be-930e-8d4fcb79f42d"
        )
        UUID user_id,

        @Schema(description = "Список рекомендаций")
        List<Recommendation> recommendations
) {
}
