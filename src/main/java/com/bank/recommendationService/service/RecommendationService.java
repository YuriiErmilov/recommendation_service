package com.bank.recommendationService.service;

import com.bank.recommendationService.dto.Recommendation;
import com.bank.recommendationService.rule.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;

    public RecommendationService(List<RecommendationRuleSet> ruleSets) {
        this.ruleSets = ruleSets;
    }

    public List<Recommendation> getRecommendation(UUID userId) {
        return ruleSets.stream().map(ruleSets -> ruleSets.check(userId))
                .flatMap(optional -> optional.stream()).toList();
    }
}
