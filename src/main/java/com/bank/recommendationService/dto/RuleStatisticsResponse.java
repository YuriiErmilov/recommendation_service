package com.bank.recommendationService.dto;

import java.util.List;

public class RuleStatisticsResponse {

    private final List<RuleStatisticsDto> stats;

    public RuleStatisticsResponse(
            List<RuleStatisticsDto> stats
    ) {
        this.stats = stats;
    }

    public List<RuleStatisticsDto> getStats() {
        return stats;
    }
}
