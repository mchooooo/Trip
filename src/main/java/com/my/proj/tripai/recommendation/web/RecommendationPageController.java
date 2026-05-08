package com.my.proj.tripai.recommendation.web;

import com.my.proj.tripai.recommendation.dto.RecommendationResponse;
import java.util.ArrayList;
import com.my.proj.tripai.recommendation.service.RecommendationService;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class RecommendationPageController {

    private final RecommendationService recommendationService;

    @GetMapping("/")
    public String home(Model model) {
        if (!model.containsAttribute("recommendationForm")) {
            model.addAttribute("recommendationForm", new RecommendationForm());
        }
        model.addAttribute("recentRecommendations", getRecentRecommendations());
        return "recommendations/home";
    }

    @PostMapping("/recommendations")
    public String createRecommendation(
            @Valid @ModelAttribute("recommendationForm") RecommendationForm recommendationForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("recentRecommendations", getRecentRecommendations());
            return "recommendations/home";
        }

        RecommendationResponse response = recommendationService.createRecommendation(recommendationForm.toRequest());
        redirectAttributes.addFlashAttribute("toastMessage", "추천 결과가 생성되었습니다.");
        return "redirect:/recommendations/" + response.id() + "/result";
    }

    @GetMapping("/recommendations/{id}/result")
    public String recommendationResult(@PathVariable Long id, Model model) {
        model.addAttribute("recommendation", recommendationService.getRecommendation(id));
        return "recommendations/result";
    }

    @GetMapping("/recommendations/history")
    public String recommendationHistory(Model model) {
        model.addAttribute("recommendations", recommendationService.getRecommendations());
        return "recommendations/history";
    }

    private List<RecommendationResponse> getRecentRecommendations() {
        List<RecommendationResponse> recommendations = recommendationService.getRecommendations();
        int fromIndex = Math.max(recommendations.size() - 5, 0);
        List<RecommendationResponse> recentRecommendations =
                new ArrayList<>(recommendations.subList(fromIndex, recommendations.size()));
        Collections.reverse(recentRecommendations);
        return recentRecommendations;
    }
}
