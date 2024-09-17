package com.example.demo.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputCategoryDTO {
    @NotEmpty(message = "Category name must be filled out.")
    @NotBlank(message = "Category name must be filled out.")
    String name;

    public InputCategoryDTO() {}
    public InputCategoryDTO(String name) {
        this.name = name;
    }
}
