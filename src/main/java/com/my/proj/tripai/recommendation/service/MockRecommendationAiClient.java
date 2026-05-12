package com.my.proj.tripai.recommendation.service;

import com.my.proj.tripai.recommendation.dto.RecommendationCreateRequest;
import org.springframework.util.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.ai.provider", havingValue = "mock", matchIfMissing = true)
public class MockRecommendationAiClient implements RecommendationAiClient {

    @Override
    public RecommendationDraft generateRecommendation(RecommendationCreateRequest request) {
        String travelStyle = normalizeOptionalValue(request.travelStyle());
        String companionType = normalizeOptionalValue(request.companionType());
        String budgetLevel = normalizeOptionalValue(request.budgetLevel());
        String season = normalizeOptionalValue(request.season());

        String destination = switch (travelStyle.toLowerCase()) {
            case "휴양", "힐링" -> "공주";
            case "도시", "맛집", "쇼핑" -> "서울";
            case "자연", "풍경", "트래킹" -> "제주";
            default -> "부산";
        };

        String extraRequest = " 추가 요청사항은 \"%s\"입니다.".formatted(request.userPrompt().trim());
        String summary = "%s와 함께 %s 예산으로 %s 여행을 %s에 가고 싶어 함.%s"
                .formatted(
                        companionType,
                        budgetLevel,
                        travelStyle,
                        season,
                        extraRequest
                );
        String reason = "%s는 %s 여행에 잘 맞고, 사용자가 남긴 추가 요청사항인 \"%s\"도 함께 반영하기 좋은 추천지입니다."
                .formatted(destination, travelStyle, request.userPrompt().trim());

        return new RecommendationDraft(destination, summary, reason);
    }

    private String normalizeOptionalValue(String value) {
        return StringUtils.hasText(value) ? value.trim() : "미입력";
    }
}
