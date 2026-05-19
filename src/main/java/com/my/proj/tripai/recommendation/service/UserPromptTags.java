package com.my.proj.tripai.recommendation.service;

import java.util.List;

record UserPromptTags(List<String> values) {

    String toCacheKeySegment() {
        if (values.isEmpty()) {
            return "-";
        }
        return String.join(",", values);
    }
}
