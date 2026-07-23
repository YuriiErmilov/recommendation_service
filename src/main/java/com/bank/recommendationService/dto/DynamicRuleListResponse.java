package com.bank.recommendationService.dto;

import java.util.List;

public class DynamicRuleListResponse {

    private List<DynamicRuleResponse> data;

    public DynamicRuleListResponse(List<DynamicRuleResponse> data) {
        this.data = data;
    }

    public List<DynamicRuleResponse> getData() {
        return data;
    }
}