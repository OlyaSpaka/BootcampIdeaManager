package com.example.demo.dto.general;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CompetitionDTO {
    Integer id;
    String name;
    String description;
    Date startDate;
    Date endDate;
    int amountOfWinners;

    public CompetitionDTO() {
    }

    public CompetitionDTO(String name, String description, Date startDate, Date endDate, int amountOfWinners) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amountOfWinners = amountOfWinners;
    }
}
