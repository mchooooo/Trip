package com.my.proj.tripai.recommendation.service;

import com.my.proj.tripai.recommendation.dto.RecommendationCreateRequest;
import java.util.StringJoiner;
import org.springframework.util.StringUtils;

// 요청을 캐시 조회용 문자열 키로 생성.
final class RecommendationRequestCacheKeyGenerator {

    private RecommendationRequestCacheKeyGenerator() {
    }

    // 같은 문장을 공백 차이만 무시하고 재사용하기 위한 원문 기반 키
    static String generateTextKey(RecommendationCreateRequest request) {
        return new StringJoiner("|")
                .add(normalizeOptionalValue(request.companionType()))
                .add(normalizeOptionalValue(request.budgetLevel()))
                .add(normalizeOptionalValue(request.travelStyle()))
                .add(normalizeOptionalValue(request.season()))
                .add(normalizePrompt(request.userPrompt()))
                .toString();
    }

    // 자유 입력은 태그로 축약한 키.
    static String generateTagKey(RecommendationCreateRequest request) {
        return new StringJoiner("|")
                .add(normalizeOptionalValue(request.companionType()))
                .add(normalizeOptionalValue(request.budgetLevel()))
                .add(normalizeOptionalValue(request.travelStyle()))
                .add(normalizeOptionalValue(request.season()))
                .add(UserPromptTagExtractor.extract(request.userPrompt()).toCacheKeySegment())
                .toString();
    }

    private static String normalizeOptionalValue(String value) {
        if (!StringUtils.hasText(value)) {
            return "-";
        }
        return collapseWhitespace(value.trim());
    }

    private static String normalizePrompt(String value) {
        return collapseWhitespace(value.trim());
    }

    private static String collapseWhitespace(String value) {
        return value.replaceAll("\\s+", " ");
    }
}
