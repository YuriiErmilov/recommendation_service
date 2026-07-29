package com.bank.recommendationService.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Ответ со статистикой динамических правил")
public record RuleStatisticsResponse(

        @ArraySchema(
                schema = @Schema(
                        implementation = RuleStatisticsDto.class
                ),
                arraySchema = @Schema(
                        description = "Статистика срабатывания всех правил"
                )
        )
        List<RuleStatisticsDto> stats

) {
}
