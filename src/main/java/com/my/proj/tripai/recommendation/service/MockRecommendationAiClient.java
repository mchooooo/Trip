package com.my.proj.tripai.recommendation.service;

import com.my.proj.tripai.recommendation.dto.RecommendationCreateRequest;
import org.springframework.util.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.ai.provider", havingValue = "mock", matchIfMissing = true)
// 로컬 개발과 테스트에서 OpenAI 호출 없이 추천 흐름을 검증하기 위한 규칙 기반 구현체
public class MockRecommendationAiClient implements RecommendationAiClient {

    @Override
    public RecommendationDraft generateRecommendation(RecommendationCreateRequest request) {
        String travelStyle = normalizeOptionalValue(request.travelStyle());

        String destination = switch (travelStyle.toLowerCase()) {
            case "휴양", "힐링" -> "공주";
            case "도시", "맛집", "쇼핑" -> "서울";
            case "자연", "풍경", "트래킹" -> "제주";
            default -> "부산";
        };

        String reason = "%s는 %s 여행에 잘 어울리는 곳입니다."
                .formatted(destination, travelStyle);

        return new RecommendationDraft(destination, reason);
    }

    private String normalizeOptionalValue(String value) {
        return StringUtils.hasText(value) ? value.trim() : "미입력";
    }
}
