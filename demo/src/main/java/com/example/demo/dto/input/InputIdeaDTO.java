package com.example.demo.dto.input;

import com.example.demo.dto.general.CompetitionDTO;
import com.example.demo.dto.output.OutputUserDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class InputIdeaDTO {
    @NotNull(message = "Title cannot be empty")
    @NotBlank(message = "Title cannot be empty")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    String title;

    @NotEmpty(message = "Description cannot be empty")
    @NotBlank(message = "Description cannot be empty")
    @Size(max = 5000, message = "Description must be at most 5000 characters")
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

    @NotNull(message = "Competition is required")
    CompetitionDTO competition;

    @NotNull(message = "User is required")
    OutputUserDTO user;

    @NotNull(message = "At least one category must be selected")
    @NotEmpty(message = "At least one category must be selected")
    Set<InputCategoryDTO> categories = new HashSet<>();

    public InputIdeaDTO() {
    }

    public InputIdeaDTO(String title, String description, String keyFeatures, String referenceLinks, String createdAt, String pictures) {
        this.title = title;
        this.description = description;
        this.keyFeatures = keyFeatures;
        this.referenceLinks = referenceLinks;
        this.createdAt = createdAt;
        this.pictures = pictures;
    }
}
