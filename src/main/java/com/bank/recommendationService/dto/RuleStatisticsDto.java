package com.bank.recommendationService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RuleStatisticsDto {

    @JsonProperty("rule_id")
    private final Long ruleId;

    private final Long count;

    public RuleStatisticsDto(
            Long ruleId,
            Long count
    ) {
        this.ruleId = ruleId;
        this.count = count;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public Long getCount() {
        return count;
    }
}
