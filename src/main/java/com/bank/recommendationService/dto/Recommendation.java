package com.bank.recommendationService.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Банковская рекомендация")
public record Recommendation(

        @Schema(
                description = "Идентификатор продукта",
                example = "147f6a0f-3b91-413b-ab99-87f081d60d5a"
        )
        UUID id,

        @Schema(
                description = "Название рекомендации",
                example = "Invest 500"
        )
        String name,

        @Schema(
                description = "Описание рекомендации",
                example = "Откройте индивидуальный инвестиционный счет..."
        )
        String text
) {
}
