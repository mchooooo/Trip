package com.my.proj.tripai.recommendation.service;

import com.my.proj.tripai.recommendation.dto.RecommendationCreateRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RecommendationRequestCacheKeyGeneratorTest {

    @Test
    void generatesSameTextKeyForWhitespaceEquivalentRequests() {
        RecommendationCreateRequest first = new RecommendationCreateRequest(
                "가족",
                "중간",
                "힐링",
                "가을",
                "부모님  모시고\n조용한  곳 추천"
        );
        RecommendationCreateRequest second = new RecommendationCreateRequest(
                " 가족 ",
                "중간",
                "힐링",
                "가을",
                "부모님 모시고 조용한 곳 추천"
        );

        assertThat(RecommendationRequestCacheKeyGenerator.generateTextKey(first))
                .isEqualTo(RecommendationRequestCacheKeyGenerator.generateTextKey(second));
    }

    @Test
    void usesPlaceholderForBlankOptionalValuesInTextKey() {
        RecommendationCreateRequest request = new RecommendationCreateRequest(
                "",
                null,
                "힐링",
                "",
                "조용한 곳 추천"
        );

        assertThat(RecommendationRequestCacheKeyGenerator.generateTextKey(request))
                .isEqualTo("-|-|힐링|-|조용한 곳 추천");
    }
}
