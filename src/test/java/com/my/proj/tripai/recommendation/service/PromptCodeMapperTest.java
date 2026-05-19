package com.my.proj.tripai.recommendation.service;

import com.my.proj.tripai.recommendation.dto.RecommendationCreateRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PromptCodeMapperTest {

    @Test
    void mapsKnownLabelsToShortCodes() {
        assertThat(PromptCodeMapper.toCompanionCode("가족")).hasValue("FAMILY");
        assertThat(PromptCodeMapper.toBudgetCode("중간")).hasValue("MID");
        assertThat(PromptCodeMapper.toStyleCode("힐링")).hasValue("HEALING");
        assertThat(PromptCodeMapper.toSeasonCode("가을")).hasValue("FALL");
    }

    @Test
    void returnsEmptyForBlankValues() {
        assertThat(PromptCodeMapper.toCompanionCode(" ")).isEmpty();
        assertThat(PromptCodeMapper.toBudgetCode(null)).isEmpty();
    }

    @Test
    void buildUserPromptOmitsBlankFields() {
        String prompt = OpenAiRecommendationClient.buildUserPrompt(
                new RecommendationCreateRequest(
                        "",
                        "중간",
                        null,
                        "가을",
                        "조용하고 걷기 편한 곳"
                )
        );

        assertThat(prompt).isEqualTo("""
                budget=MID
                season=FALL
                request=조용하고 걷기 편한 곳""");
    }

    @Test
    void buildUserPromptTrimsRequestToTwoHundredCharacters() {
        String longPrompt = "a".repeat(210);

        String prompt = OpenAiRecommendationClient.buildUserPrompt(
                new RecommendationCreateRequest(
                        "가족",
                        "중간",
                        "힐링",
                        "가을",
                        longPrompt
                )
        );

        assertThat(prompt).contains("request=" + "a".repeat(200));
        assertThat(prompt).doesNotContain("request=" + "a".repeat(201));
    }
}
