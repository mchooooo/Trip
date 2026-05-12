package com.my.proj.tripai.recommendation.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.proj.tripai.recommendation.dto.RecommendationCreateRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@ConditionalOnProperty(name = "app.ai.provider", havingValue = "openai")
public class OpenAiRecommendationClient implements RecommendationAiClient {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public OpenAiRecommendationClient(ChatClient.Builder chatClientBuilder, ObjectMapper objectMapper) {
        this.chatClient = chatClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    @Override
    public RecommendationDraft generateRecommendation(RecommendationCreateRequest request) {
        String companionType = normalizeOptionalValue(request.companionType());
        String budgetLevel = normalizeOptionalValue(request.budgetLevel());
        String travelStyle = normalizeOptionalValue(request.travelStyle());
        String season = normalizeOptionalValue(request.season());
        String userPrompt = request.userPrompt().trim();

        String response = chatClient.prompt()
                .system("""
                        당신은 여행지 추천 도우미입니다.
                        대한민국 여행지를 추천해주세요.
                        사용자의 조건을 바탕으로 여행지 1곳만 추천하세요.
                        사용자가 남긴 자유 텍스트 요청도 반드시 반영하세요.
                        반드시 JSON만 응답하세요.
                        형식: {"destination":"", "promptSummary":"", "reason":""}
                        """)
                .user("""
                        동행 유형: %s
                        예산 수준: %s
                        여행 스타일: %s
                        여행 계절: %s
                        추가 요청사항: %s
                        """.formatted(
                        companionType,
                        budgetLevel,
                        travelStyle,
                        season,
                        userPrompt
                ))
                .call()
                .content();

        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            return new RecommendationDraft(
                    jsonNode.path("destination").asText(),
                    jsonNode.path("promptSummary").asText(),
                    jsonNode.path("reason").asText()
            );
        } catch (Exception exception) {
            throw new IllegalStateException("OpenAI 응답 파싱에 실패했습니다.", exception);
        }
    }

    private String normalizeOptionalValue(String value) {
        return StringUtils.hasText(value) ? value.trim() : "미입력";
    }
}
