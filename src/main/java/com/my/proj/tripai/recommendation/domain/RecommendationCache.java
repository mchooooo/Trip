package com.my.proj.tripai.recommendation.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendationCache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cacheType;

    private String cacheKey;

    private String destination;

    private String reason;

    private LocalDateTime expiresAt;

    private LocalDateTime createdAt;

    @Builder
    private RecommendationCache(
            String cacheType,
            String cacheKey,
            String destination,
            String reason,
            LocalDateTime expiresAt,
            LocalDateTime createdAt
    ) {
        this.cacheType = cacheType;
        this.cacheKey = cacheKey;
        this.destination = destination;
        this.reason = reason;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }
}
