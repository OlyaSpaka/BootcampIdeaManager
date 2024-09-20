package com.example.demo.dto;

import java.util.Map;

public record UserSelectionPrioritiesDTO(
        Integer userId,
        String submissionTime,
        Map<Integer, Integer> priorities
) {
}