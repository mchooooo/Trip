package com.my.proj.tripai.recommendation.controller;

import com.my.proj.tripai.recommendation.dto.RecommendationResponse;
import com.my.proj.tripai.recommendation.service.RecommendationService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.my.proj.tripai.global.error.GlobalExceptionHandler;
import com.my.proj.tripai.recommendation.exception.RecommendationNotFoundException;

@WebMvcTest(RecommendationController.class)
@Import(GlobalExceptionHandler.class)
class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecommendationService recommendationService;

    @Test
    void getRecommendationsReturnsWrappedSuccessResponse() throws Exception {
        RecommendationResponse response = new RecommendationResponse(
                1L,
                "부산",
                "친구",
                "중간",
                "맛집",
                "가을",
                "부모님 모시고 조용한 곳이면 좋겠어요.",
                "친구와 함께 중간 예산으로 맛집 여행을 가을에 가고 싶어 함",
                "부산은 음식과 바다를 함께 즐기기 좋은 도시입니다.",
                LocalDateTime.of(2026, 5, 8, 19, 0)
        );

        given(recommendationService.getRecommendations()).willReturn(List.of(response));

        mockMvc.perform(get("/api/recommendations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("추천 결과 목록 조회에 성공했습니다."))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].destination").value("부산"));
    }

    @Test
    void createRecommendationReturnsUnifiedValidationErrorResponse() throws Exception {
        mockMvc.perform(post("/api/recommendations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "companionType": "",
                                  "budgetLevel": "",
                                  "travelStyle": "",
                                  "season": "",
                                  "userPrompt": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("요청 값이 올바르지 않습니다."))
                .andExpect(jsonPath("$.path").value("/api/recommendations"))
                .andExpect(jsonPath("$.fieldErrors.length()").value(1))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("userPrompt"));
    }

    @Test
    void getRecommendationReturnsUnifiedNotFoundErrorResponse() throws Exception {
        given(recommendationService.getRecommendation(99L))
                .willThrow(new RecommendationNotFoundException(99L));

        mockMvc.perform(get("/api/recommendations/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("추천 결과를 찾을 수 없습니다. id=99"))
                .andExpect(jsonPath("$.path").value("/api/recommendations/99"))
                .andExpect(jsonPath("$.fieldErrors.length()").value(0));
    }
}
