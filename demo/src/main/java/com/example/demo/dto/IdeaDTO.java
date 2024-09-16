package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class IdeaDTO {
    Integer id;
    String title;
    String description;
    String keyFeatures;
    String references;
    Date createdAt;
    String pictures;
    Integer competitionId;
    Integer userId;
    Set<Integer> categoryIds;

    public IdeaDTO() {
    }

    public IdeaDTO(String title, String description, String keyFeatures, String references, Date createdAt, String pictures) {
        this.title = title;
        this.description = description;
        this.keyFeatures = keyFeatures;
        this.references = references;
        this.createdAt = createdAt;
        this.pictures = pictures;
    }

    public IdeaDTO(String title, Date createdAt) {
        this.title = title;
        this.createdAt = createdAt;
    }
}