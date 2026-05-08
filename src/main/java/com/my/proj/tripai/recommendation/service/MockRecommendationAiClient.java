package com.my.proj.tripai.recommendation.service;

import com.my.proj.tripai.recommendation.dto.RecommendationCreateRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.ai.provider", havingValue = "mock", matchIfMissing = true)
public class MockRecommendationAiClient implements RecommendationAiClient {

    @Override
    public RecommendationDraft generateRecommendation(RecommendationCreateRequest request) {
        String destination = switch (request.travelStyle().toLowerCase()) {
            case "휴양", "힐링" -> "공주";
            case "도시", "맛집", "쇼핑" -> "서울";
            case "자연", "풍경", "트래킹" -> "제주";
            default -> "부산";
        };

        String summary = "%s와 함께 %s 예산으로 %s 여행을 %s에 가고 싶어 함"
                .formatted(request.companionType(), request.budgetLevel(), request.travelStyle(), request.season());
        String reason = "%s는 %s 여행에 잘 맞고, %s 시기에 이동 부담이 비교적 적어 작은 여행 앱 MVP에 적합한 추천 결과입니다."
                .formatted(destination, request.travelStyle(), request.season());

        return new RecommendationDraft(destination, summary, reason);
    }
}
