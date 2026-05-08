package com.my.proj.tripai.recommendation.controller;

import com.my.proj.tripai.global.api.ApiResponse;
import com.my.proj.tripai.recommendation.dto.RecommendationCreateRequest;
import com.my.proj.tripai.recommendation.dto.RecommendationResponse;
import com.my.proj.tripai.recommendation.service.RecommendationService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping
    public ResponseEntity<ApiResponse<RecommendationResponse>> createRecommendation(
            @Valid @RequestBody RecommendationCreateRequest request
    ) {
        RecommendationResponse response = recommendationService.createRecommendation(request);
        return ResponseEntity.created(URI.create("/api/recommendations/" + response.id()))
                .body(ApiResponse.created("추천 결과가 생성되었습니다.", response));
    }

    @GetMapping
    public ApiResponse<List<RecommendationResponse>> getRecommendations() {
        return ApiResponse.ok("추천 결과 목록 조회에 성공했습니다.", recommendationService.getRecommendations());
    }

    @GetMapping("/{id}")
    public ApiResponse<RecommendationResponse> getRecommendation(@PathVariable Long id) {
        return ApiResponse.ok("추천 결과 조회에 성공했습니다.", recommendationService.getRecommendation(id));
    }
}
