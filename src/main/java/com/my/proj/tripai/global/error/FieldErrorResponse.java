package com.my.proj.tripai.global.error;

public record FieldErrorResponse(
        String field,
        Object rejectedValue,
        String reason
) {
}
