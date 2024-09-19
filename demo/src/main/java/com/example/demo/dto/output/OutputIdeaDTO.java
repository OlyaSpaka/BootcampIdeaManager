package com.example.demo.dto.output;

import com.example.demo.dto.general.CompetitionDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class OutputIdeaDTO {
    Integer id;
    String title;
    String description;
    String keyFeatures;
    String referenceLinks;
    String createdAt;
    String pictures;
    CompetitionDTO competition;
    OutputUserDTO user;
    Set<OutputCategoryDTO> categories = new HashSet<>();

    public OutputIdeaDTO() {
    }

    public OutputIdeaDTO(String title, String description, String keyFeatures, String referenceLinks, String createdAt, String pictures) {
        this.title = title;
        this.description = description;
        this.keyFeatures = keyFeatures;
        this.referenceLinks = referenceLinks;
        this.createdAt = createdAt;
        this.pictures = pictures;
    }
}