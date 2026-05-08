package com.my.proj.tripai.recommendation.dto;

import jakarta.validation.constraints.NotBlank;

public record RecommendationCreateRequest(
        @NotBlank(message = "동행 유형은 필수입니다.")
        String companionType,
        @NotBlank(message = "예산 수준은 필수입니다.")
        String budgetLevel,
        @NotBlank(message = "여행 스타일은 필수입니다.")
        String travelStyle,
        @NotBlank(message = "여행 계절은 필수입니다.")
        String season
) {
}
