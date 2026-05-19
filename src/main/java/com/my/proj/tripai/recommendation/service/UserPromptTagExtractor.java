package com.my.proj.tripai.recommendation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.util.StringUtils;

// 자유 입력에서 핵심 태그만 추출하는 규칙 기반 추출기다.
final class UserPromptTagExtractor {

    private static final Map<String, List<String>> TAG_DICTIONARY = Map.of(
            "healing", List.of("힐링", "휴식", "여유", "편안", "쉬고"),
            "low_mobility", List.of("걷기 편한", "이동 편한", "무리 없는", "계단 없는", "평지"),
            "nature", List.of("자연", "숲", "바다", "호수", "풍경"),
            "quiet", List.of("조용", "한적", "북적이지", "사람 적은"),
            "senior", List.of("부모님", "어르신", "노부모", "할머니", "할아버지"),
            "walk", List.of("걷기", "산책", "도보", "걸을")
    );

    private UserPromptTagExtractor() {
    }

    // 태그는 정렬된 고정 집합으로 반환해 같은 의미의 입력이 같은 키로 수렴하도록 만든다.
    static UserPromptTags extract(String prompt) {
        if (!StringUtils.hasText(prompt)) {
            return new UserPromptTags(List.of());
        }

        String normalized = normalize(prompt);
        List<String> tags = new ArrayList<>();
        TAG_DICTIONARY.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    if (containsAny(normalized, entry.getValue())) {
                        tags.add(entry.getKey());
                    }
                });

        return new UserPromptTags(List.copyOf(tags));
    }

    private static boolean containsAny(String normalized, List<String> keywords) {
        return keywords.stream().anyMatch(normalized::contains);
    }

    private static String normalize(String prompt) {
        return prompt.trim().replaceAll("\\s+", " ");
    }
}
