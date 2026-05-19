package com.my.proj.tripai.recommendation.repository;

import com.my.proj.tripai.recommendation.domain.RecommendationCache;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationCacheRepository extends JpaRepository<RecommendationCache, Long> {

    Optional<RecommendationCache> findTopByCacheTypeAndCacheKeyAndExpiresAtAfterOrderByCreatedAtDesc(
            String cacheType,
            String cacheKey,
            LocalDateTime now
    );
}
