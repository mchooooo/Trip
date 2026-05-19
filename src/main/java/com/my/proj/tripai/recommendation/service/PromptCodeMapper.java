package com.my.proj.tripai.recommendation.service;

import java.util.Map;
import java.util.Optional;
import org.springframework.util.StringUtils;

final class PromptCodeMapper {

    private static final Map<String, String> COMPANION_CODES = Map.of(
            "혼자", "SOLO",
            "친구", "FRIEND",
            "연인", "COUPLE",
            "가족", "FAMILY"
    );

    private static final Map<String, String> BUDGET_CODES = Map.of(
            "낮음", "LOW",
            "중간", "MID",
            "높음", "HIGH"
    );

    private static final Map<String, String> STYLE_CODES = Map.of(
            "휴양", "REST",
            "맛집", "FOOD",
            "도시", "CITY",
            "자연", "NATURE",
            "힐링", "HEALING"
    );

    private static final Map<String, String> SEASON_CODES = Map.of(
            "봄", "SPRING",
            "여름", "SUMMER",
            "가을", "FALL",
            "겨울", "WINTER"
    );

    private PromptCodeMapper() {
    }

    static Optional<String> toCompanionCode(String value) {
        return toCode(value, COMPANION_CODES);
    }

    static Optional<String> toBudgetCode(String value) {
        return toCode(value, BUDGET_CODES);
    }

    static Optional<String> toStyleCode(String value) {
        return toCode(value, STYLE_CODES);
    }

    static Optional<String> toSeasonCode(String value) {
        return toCode(value, SEASON_CODES);
    }

    private static Optional<String> toCode(String value, Map<String, String> codeMap) {
        if (!StringUtils.hasText(value)) {
            return Optional.empty();
        }

        String normalized = value.trim();
        return Optional.of(codeMap.getOrDefault(normalized, normalized.toUpperCase()));
    }
}
