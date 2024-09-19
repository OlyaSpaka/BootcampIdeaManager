package com.example.demo.dto.output;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OutputCategoryDTO that = (OutputCategoryDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
