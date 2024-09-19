package com.example.demo.dto;

import java.util.List;

public record GroupAssignmentsDTO(
        String ideaTitle,
        List<String> usernames
) {
}