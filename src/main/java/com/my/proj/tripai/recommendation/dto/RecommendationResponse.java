package com.my.proj.tripai.recommendation.dto;

import com.my.proj.tripai.recommendation.domain.Recommendation;
import java.time.LocalDateTime;

public record RecommendationResponse(
        Long id,
        String destination,
        String companionType,
        String budgetLevel,
        String travelStyle,
        String season,
        String promptSummary,
        String reason,
        LocalDateTime createdAt
) {
    public static RecommendationResponse from(Recommendation recommendation) {
        return new RecommendationResponse(
                recommendation.getId(),
                recommendation.getDestination(),
                recommendation.getCompanionType(),
                recommendation.getBudgetLevel(),
                recommendation.getTravelStyle(),
                recommendation.getSeason(),
                recommendation.getPromptSummary(),
                recommendation.getReason(),
                recommendation.getCreatedAt()
        );
    }
}
