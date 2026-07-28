package com.bank.recommendationService.controller;


import com.bank.recommendationService.dto.Recommendation;
import com.bank.recommendationService.dto.RecommendationResponse;
import com.bank.recommendationService.recommendationRepository.RecommendationRepository;
import com.bank.recommendationService.service.RecommendationService;
import org.springframework.http.ResponseEntity;
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
    private final RecommendationRepository recommendationRepository;

    public RecommendationController(RecommendationService recommendationService,  RecommendationRepository recommendationRepository) {
        this.recommendationService = recommendationService;
        this.recommendationRepository = recommendationRepository;
    }

    @GetMapping("/{userId}")

    public RecommendationResponse getRecommendation(@PathVariable UUID userId) {
        List<Recommendation> recommendations = recommendationService.getRecommendation(userId);
        return new RecommendationResponse(
                userId,
                recommendations
        );
    }
    @GetMapping("/user/{username}")
    public ResponseEntity<String> findUserByUsername(
            @PathVariable String username
    ) {
        return recommendationRepository.findUserIdByUsername(username)
                .map(userId -> ResponseEntity.ok(userId.toString()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
