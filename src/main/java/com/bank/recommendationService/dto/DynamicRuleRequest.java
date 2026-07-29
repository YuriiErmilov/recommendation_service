package com.bank.recommendationService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Schema(description = "Запрос на создание динамического правила")
public class DynamicRuleRequest {

    @Schema(
            description = "Название банковского продукта",
            example = "Premium Debit Card"
    )
    @JsonProperty("product_name")
    private String productName;

    @Schema(
            description = "UUID банковского продукта",
            example = "11111111-1111-1111-1111-111111111111"
    )
    @JsonProperty("product_id")
    private UUID productId;

    @Schema(
            description = "Описание рекомендации",
            example = "Оформите премиальную дебетовую карту."
    )
    @JsonProperty("product_text")
    private String productText;

    @ArraySchema(
            schema = @Schema(implementation = RuleQueryDto.class),
            arraySchema = @Schema(
                    description = "Список условий динамического правила"
            )
    )
    private List<RuleQueryDto> rule = new ArrayList<>();

    public DynamicRuleRequest() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public List<RuleQueryDto> getRule() {
        return rule;
    }

    public void setRule(List<RuleQueryDto> rule) {
        this.rule = rule;
    }
}
