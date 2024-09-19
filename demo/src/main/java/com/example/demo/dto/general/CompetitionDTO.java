package com.example.demo.dto.general;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CompetitionDTO {
    Integer id;

    @NotBlank(message = "Name is required")
    String name;

    String description;

    LocalDate startDate;
    LocalDate endDate;

    @Positive
    int amountOfWinners;

    public CompetitionDTO() {
    }

    public CompetitionDTO(String name, String description, LocalDate startDate, LocalDate endDate, int amountOfWinners) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amountOfWinners = amountOfWinners;
    }
}
