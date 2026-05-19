package com.my.proj.tripai.recommendation.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.proj.tripai.recommendation.dto.RecommendationCreateRequest;
import java.util.StringJoiner;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
@Component
@ConditionalOnProperty(name = "app.ai.provider", havingValue = "openai")
// 실제 OpenAI 호출을 담당
public class OpenAiRecommendationClient implements RecommendationAiClient {

    private static final int MAX_PROMPT_LENGTH = 200;

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public OpenAiRecommendationClient(ChatClient.Builder chatClientBuilder, ObjectMapper objectMapper) {
        this.chatClient = chatClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    @Override
    public RecommendationDraft generateRecommendation(RecommendationCreateRequest request) {
        String response = chatClient.prompt()
                .system("""
                        당신은 여행지 추천 도우미입니다.
                        대한민국 여행지를 추천해주세요.
                        코드화된 사용자 조건을 바탕으로 여행지 1곳만 추천하세요.
                        사용자가 남긴 자유 텍스트 요청도 반드시 반영하세요.
                        reason은 1문장, 80자 이내로 작성하세요.
                        반드시 JSON만 응답하세요.
                        형식: {"destination":"", "reason":""}
                        """)
                .user(buildUserPrompt(request))
                .call()
                .content();

        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            return new RecommendationDraft(
                    jsonNode.path("destination").asText(),
                    jsonNode.path("reason").asText()
            );
        } catch (Exception exception) {
            throw new IllegalStateException("OpenAI 응답 파싱에 실패했습니다.", exception);
        }
    }

    // 선택값과 자유 입력을 한 줄씩 직렬화
    static String buildUserPrompt(RecommendationCreateRequest request) {
        StringJoiner joiner = new StringJoiner("\n");
        PromptCodeMapper.toCompanionCode(request.companionType())
                .ifPresent(code -> joiner.add("companion=" + code));
        PromptCodeMapper.toBudgetCode(request.budgetLevel())
                .ifPresent(code -> joiner.add("budget=" + code));
        PromptCodeMapper.toStyleCode(request.travelStyle())
                .ifPresent(code -> joiner.add("style=" + code));
        PromptCodeMapper.toSeasonCode(request.season())
                .ifPresent(code -> joiner.add("season=" + code));
        joiner.add("request=" + trimToMaxLength(request.userPrompt(), MAX_PROMPT_LENGTH));
        return joiner.toString();
    }

    private static String trimToMaxLength(String value, int maxLength) {
        String normalized = value.trim();
        if (normalized.length() <= maxLength) {
            return normalized;
        }
        return normalized.substring(0, maxLength);
    }
}
