package com.bank.recommendationService.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Информация о текущей сборке приложения")
public record ServiceInfoResponse(

        @Schema(
                description = "Название приложения",
                example = "recommendationService"
        )
        String name,

        @Schema(
                description = "Версия приложения",
                example = "0.0.1-SNAPSHOT"
        )
        String version

) {
}
