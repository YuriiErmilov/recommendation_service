package com.bank.recommendationService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Schema(description = "Созданное динамическое правило")
public class DynamicRuleResponse {

    @Schema(
            description = "Внутренний идентификатор правила",
            example = "5"
    )
    private Long id;

    @Schema(
            description = "Название банковского продукта",
            example = "Premium Debit Card"
    )
    @JsonProperty("product_name")
    private String productName;

    @Schema(
            description = "UUID продукта",
            example = "11111111-1111-1111-1111-111111111111"
    )
    @JsonProperty("product_id")
    private UUID productId;

    @Schema(
            description = "Описание рекомендации"
    )
    @JsonProperty("product_text")
    private String productText;

    @ArraySchema(
            schema = @Schema(implementation = RuleQueryDto.class)
    )
    private List<RuleQueryDto> rule = new ArrayList<>();

    public DynamicRuleResponse() {
    }

    public DynamicRuleResponse(
            Long id,
            String productName,
            UUID productId,
            String productText,
            List<RuleQueryDto> rule
    ) {
        this.id = id;
        this.productName = productName;
        this.productId = productId;
        this.productText = productText;
        this.rule = rule;
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getProductText() {
        return productText;
    }

    public List<RuleQueryDto> getRule() {
        return rule;
    }
}
