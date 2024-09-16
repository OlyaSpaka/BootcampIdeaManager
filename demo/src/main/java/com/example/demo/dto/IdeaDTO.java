package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class IdeaDTO {
    Integer id;

    @NotNull(message = "Title cannot be empty")
    @NotBlank(message = "Title cannot be empty")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    String title;

    @NotEmpty(message = "Description cannot be empty")
    @Size(min = 10, max = 5000, message = "Description must be between 10 and 5000 characters")
    String description;

    @Size(max = 2000, message = "Key features must not exceed 2000 characters")
    String keyFeatures;

    @Size(max = 2000, message = "References must not exceed 2000 characters")
    String referenceLinks;

    @NotNull
//    @PastOrPresent(message = "Creation date must be in the past or present")
    String createdAt;

//    @Pattern(regexp = "https?://.*", message = "Invalid picture URL")
    String pictures;

    @NotNull(message = "Competition ID is required")
    Integer competitionId;

    @NotNull(message = "User ID is required")
    Integer userId;

    @NotNull(message = "At least one category must be selected")
    @NotEmpty(message = "At least one category must be selected")
    Set<Integer> categoryIds;

    public IdeaDTO() {
    }

    public IdeaDTO(String title, String description, String keyFeatures, String referenceLinks, String createdAt, String pictures) {
        this.title = title;
        this.description = description;
        this.keyFeatures = keyFeatures;
        this.referenceLinks = referenceLinks;
        this.createdAt = createdAt;
        this.pictures = pictures;
    }

    public IdeaDTO(String title, String createdAt) {
        this.title = title;
        this.createdAt = createdAt;
    }
}