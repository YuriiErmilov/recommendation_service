package com.bank.recommendationService.controller;


import com.bank.recommendationService.dto.Recommendation;
import com.bank.recommendationService.dto.RecommendationResponse;
import com.bank.recommendationService.service.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}")

    public RecommendationResponse getRecommendation(@PathVariable UUID userId) {
        List<Recommendation> recommendations = recommendationService.getRecommendation(userId);
        return new RecommendationResponse(
                userId,
                recommendations
        );
    }
}
