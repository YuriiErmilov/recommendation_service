package com.bank.recommendationService.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Ответ со списком динамических правил")
public record DynamicRuleListResponse(

        @ArraySchema(
                schema = @Schema(
                        implementation = DynamicRuleResponse.class
                ),
                arraySchema = @Schema(
                        description = "Список зарегистрированных динамических правил"
                )
        )
        List<DynamicRuleResponse> data

) {
}