package com.my.proj.tripai.recommendation.service;

import com.my.proj.tripai.recommendation.dto.RecommendationCreateRequest;

public interface RecommendationAiClient {

    RecommendationDraft generateRecommendation(RecommendationCreateRequest request);
}
