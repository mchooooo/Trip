package com.my.proj.tripai.recommendation.service;

import com.my.proj.tripai.recommendation.domain.Recommendation;
import com.my.proj.tripai.recommendation.dto.RecommendationCreateRequest;
import com.my.proj.tripai.recommendation.dto.RecommendationResponse;
import com.my.proj.tripai.recommendation.exception.RecommendationNotFoundException;
import com.my.proj.tripai.recommendation.repository.RecommendationRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final RecommendationAiClient recommendationAiClient;

    @Transactional
    public RecommendationResponse createRecommendation(RecommendationCreateRequest request) {
        RecommendationDraft draft = recommendationAiClient.generateRecommendation(request);

        Recommendation recommendation = Recommendation.builder()
                .destination(draft.destination())
                .companionType(request.companionType())
                .budgetLevel(request.budgetLevel())
                .travelStyle(request.travelStyle())
                .season(request.season())
                .userPrompt(request.userPrompt())
                .promptSummary(draft.promptSummary())
                .reason(draft.reason())
                .createdAt(LocalDateTime.now())
                .build();

        return RecommendationResponse.from(recommendationRepository.save(recommendation));
    }

    public List<RecommendationResponse> getRecommendations() {
        return recommendationRepository.findAll().stream()
                .map(RecommendationResponse::from)
                .toList();
    }

    public RecommendationResponse getRecommendation(Long id) {
        return recommendationRepository.findById(id)
                .map(RecommendationResponse::from)
                .orElseThrow(() -> new RecommendationNotFoundException(id));
    }
}
