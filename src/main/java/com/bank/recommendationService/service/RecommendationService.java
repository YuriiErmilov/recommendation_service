package com.bank.recommendationService.service;

import com.bank.recommendationService.dto.Recommendation;
import com.bank.recommendationService.entity.DynamicRuleEntity;
import com.bank.recommendationService.repository.DynamicRuleRepository;
import com.bank.recommendationService.rule.RecommendationRuleSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;

    private final DynamicRuleRepository dynamicRuleRepository;

    private final DynamicRuleEvaluator dynamicRuleEvaluator;

    private final RuleStatisticsService ruleStatisticsService;

    public RecommendationService(
            List<RecommendationRuleSet> ruleSets,
            DynamicRuleRepository dynamicRuleRepository,
            DynamicRuleEvaluator dynamicRuleEvaluator,
            RuleStatisticsService ruleStatisticsService
    ) {
        this.ruleSets = ruleSets;

        this.dynamicRuleRepository =
                dynamicRuleRepository;

        this.dynamicRuleEvaluator =
                dynamicRuleEvaluator;

        this.ruleStatisticsService =
                ruleStatisticsService;
    }

    @Transactional
    public List<Recommendation> getRecommendation(
            UUID userId
    ) {
        List<Recommendation> recommendations =
                new ArrayList<>();

        recommendations.addAll(
                getFixedRecommendations(userId)
        );

        recommendations.addAll(
                getDynamicRecommendations(userId)
        );

        return removeDuplicates(recommendations);
    }

    private List<Recommendation> getFixedRecommendations(
            UUID userId
    ) {
        return ruleSets.stream()
                .map(ruleSet ->
                        ruleSet.check(userId)
                )
                .flatMap(optional ->
                        optional.stream()
                )
                .toList();
    }

    private List<Recommendation> getDynamicRecommendations(
            UUID userId
    ) {
        List<Recommendation> recommendations =
                new ArrayList<>();

        List<DynamicRuleEntity> dynamicRules =
                dynamicRuleRepository.findAll();

        for (DynamicRuleEntity rule : dynamicRules) {
            boolean matches =
                    matchesRule(userId, rule);

            if (matches) {

                ruleStatisticsService.incrementCount(
                        rule.getId()
                );

                recommendations.add(
                        convertToRecommendation(rule)
                );
            }
        }

        return recommendations;
    }

    private boolean matchesRule(
            UUID userId,
            DynamicRuleEntity rule
    ) {

        return rule.getRuleQueries()
                .stream()
                .allMatch(ruleQuery ->
                        dynamicRuleEvaluator.evaluate(
                                userId,
                                ruleQuery
                        )
                );
    }

    private Recommendation convertToRecommendation(
            DynamicRuleEntity rule
    ) {
        return new Recommendation(
                rule.getProductId(),
                rule.getProductName(),
                rule.getProductText()
        );
    }

    private List<Recommendation> removeDuplicates(
            List<Recommendation> recommendations
    ) {
        Map<UUID, Recommendation> uniqueRecommendations =
                new LinkedHashMap<>();

        for (Recommendation recommendation :
                recommendations) {

            uniqueRecommendations.putIfAbsent(
                    recommendation.getId(),
                    recommendation
            );
        }

        return new ArrayList<>(
                uniqueRecommendations.values()
        );
    }
}
