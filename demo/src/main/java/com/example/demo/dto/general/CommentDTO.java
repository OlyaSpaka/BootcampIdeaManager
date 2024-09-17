package com.example.demo.dto.general;

import com.example.demo.dto.output.OutputIdeaDTO;
import com.example.demo.dto.output.OutputUserDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
    Integer id;

    OutputUserDTO user;

    OutputIdeaDTO idea;

    @Size(max = 255, message = "Content must be less than 255 characters")
    @NotEmpty(message = "Text is required")
    @NotBlank(message = "Text is required")
    String content;

    public CommentDTO() {
    }

    public CommentDTO(String content) {
        this.content = content;
    }

    public CommentDTO(Integer id, String content) {
        this.id = id;
        this.content = content;
    }
}
