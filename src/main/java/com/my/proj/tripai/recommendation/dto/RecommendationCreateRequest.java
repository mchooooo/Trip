package com.my.proj.tripai.recommendation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RecommendationCreateRequest(
        String companionType,
        String budgetLevel,
        String travelStyle,
        String season,
        @NotBlank(message = "추가 요청은 필수입니다.")
        @Size(max = 500, message = "추가 요청은 500자 이하로 입력해주세요.")
        String userPrompt
) {
}
