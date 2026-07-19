package com.bank.recommendationService.dto;

import java.util.List;
import java.util.UUID;

public class RecommendationResponse {
    private final UUID user_id;
    private final List<Recommendation> recommendations;

    public RecommendationResponse(UUID user_id, List<Recommendation> recommendations) {
        this.user_id = user_id;
        this.recommendations = recommendations;
    }
    public UUID getUser_id() {
        return user_id;
    }
    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

}
