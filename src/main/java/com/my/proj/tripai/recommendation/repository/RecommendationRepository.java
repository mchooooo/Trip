package com.my.proj.tripai.recommendation.repository;

import com.my.proj.tripai.recommendation.domain.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
}
