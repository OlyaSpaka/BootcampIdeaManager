package com.example.demo.dto;


public record BookmarkRequestDTO(
        Integer ideaId,
        Integer userId,
        Boolean bookmarked
) {
}