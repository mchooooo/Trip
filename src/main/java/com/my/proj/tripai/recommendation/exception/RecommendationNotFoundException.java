package com.my.proj.tripai.recommendation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RecommendationNotFoundException extends ResponseStatusException {

    public RecommendationNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "추천 결과를 찾을 수 없습니다. id=" + id);
    }
}
