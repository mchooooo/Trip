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
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String destination;

    private String companionType;

    private String budgetLevel;

    private String travelStyle;

    private String season;

    private String promptSummary;

    private String reason;

    private LocalDateTime createdAt;

    @Builder
    private Recommendation(
            String destination,
            String companionType,
            String budgetLevel,
            String travelStyle,
            String season,
            String promptSummary,
            String reason,
            LocalDateTime createdAt
    ) {
        this.destination = destination;
        this.companionType = companionType;
        this.budgetLevel = budgetLevel;
        this.travelStyle = travelStyle;
        this.season = season;
        this.promptSummary = promptSummary;
        this.reason = reason;
        this.createdAt = createdAt;
    }
}
