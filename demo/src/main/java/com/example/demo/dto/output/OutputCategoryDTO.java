package com.example.demo.dto.output;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class OutputCategoryDTO {
    Integer id;
    String name;
    Set<OutputIdeaDTO> ideas;

    public OutputCategoryDTO() {
    }
    public OutputCategoryDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    public OutputCategoryDTO(String name) {
        this.name = name;
    }
}
