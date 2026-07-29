package com.bank.recommendationService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Статистика срабатывания динамического правила")
public record RuleStatisticsDto(

        @JsonProperty("rule_id")
        @Schema(
                description = "Идентификатор динамического правила",
                example = "5"
        )
        Long ruleId,

        @Schema(
                description = "Количество успешных срабатываний правила",
                example = "17"
        )
        Long count

) {
}
