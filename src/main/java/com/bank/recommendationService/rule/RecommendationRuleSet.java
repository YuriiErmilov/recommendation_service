package com.bank.recommendationService.rule;

import com.bank.recommendationService.dto.Recommendation;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    Optional<Recommendation> check(UUID userId);
}
