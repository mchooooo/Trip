package com.my.proj.tripai.recommendation.web;

import com.my.proj.tripai.recommendation.dto.RecommendationCreateRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecommendationForm {

    @NotBlank(message = "동행 유형은 필수입니다.")
    private String companionType;

    @NotBlank(message = "예산 수준은 필수입니다.")
    private String budgetLevel;

    @NotBlank(message = "여행 스타일은 필수입니다.")
    private String travelStyle;

    @NotBlank(message = "여행 계절은 필수입니다.")
    private String season;

    public RecommendationCreateRequest toRequest() {
        return new RecommendationCreateRequest(companionType, budgetLevel, travelStyle, season);
    }
}
