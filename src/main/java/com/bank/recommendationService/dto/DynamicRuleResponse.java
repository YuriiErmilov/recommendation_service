package com.bank.recommendationService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DynamicRuleResponse {

    private Long id;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_id")
    private UUID productId;

    @JsonProperty("product_text")
    private String productText;

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
