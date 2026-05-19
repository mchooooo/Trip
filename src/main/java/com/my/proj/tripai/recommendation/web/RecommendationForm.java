package com.my.proj.tripai.recommendation.web;

import com.my.proj.tripai.recommendation.dto.RecommendationCreateRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecommendationForm {

    private String companionType;

    private String budgetLevel;

    private String travelStyle;

    private String season;

    @NotBlank(message = "추가 요청은 필수입니다.")
    @Size(max = 200, message = "추가 요청은 200자 이하로 입력해주세요.")
    private String userPrompt;

    public RecommendationCreateRequest toRequest() {
        return new RecommendationCreateRequest(companionType, budgetLevel, travelStyle, season, userPrompt);
    }
}
