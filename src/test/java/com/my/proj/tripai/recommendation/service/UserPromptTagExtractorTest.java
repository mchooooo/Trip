package com.my.proj.tripai.recommendation.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserPromptTagExtractorTest {

    @Test
    void extractsSortedTagsFromPrompt() {
        UserPromptTags tags = UserPromptTagExtractor.extract("부모님 모시고 조용히 걷기 편한 자연 여행지 추천");

        assertThat(tags.toCacheKeySegment()).isEqualTo("low_mobility,nature,quiet,senior,walk");
    }

    @Test
    void returnsDashSegmentWhenNoTagsMatch() {
        UserPromptTags tags = UserPromptTagExtractor.extract("추천 부탁해");

        assertThat(tags.toCacheKeySegment()).isEqualTo("-");
    }
}
