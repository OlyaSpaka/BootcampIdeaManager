package com.example.demo.mapper.implementation;

import com.example.demo.dto.general.CompetitionDTO;
import com.example.demo.models.Competition;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompetitionMapperTest {

    private final CompetitionMapper competitionMapper = new CompetitionMapper();

    @Test
    void testMapCompetitionDTOToCompetition() {
        CompetitionDTO competitionDTO = new CompetitionDTO();
        competitionDTO.setName("Summer Challenge");
        competitionDTO.setDescription("A challenge for the summer season");
        competitionDTO.setStartDate(LocalDate.now());
        competitionDTO.setEndDate(LocalDate.now());
        competitionDTO.setAmountOfWinners(3);

        Competition competition = competitionMapper.map(competitionDTO);

        assertNotNull(competition);
        assertEquals("Summer Challenge", competition.getName());
        assertEquals("A challenge for the summer season", competition.getDescription());
        assertEquals(3, competition.getAmountOfWinners());
    }

    @Test
    void testMapCompetitionToCompetitionDTO() {
        Competition competition = new Competition();
        competition.setId(1);
        competition.setName("Winter Tournament");
        competition.setDescription("A tournament for the winter season");
        competition.setStartDate(LocalDate.now());
        competition.setEndDate(LocalDate.now());
        competition.setAmountOfWinners(5);

        CompetitionDTO competitionDTO = competitionMapper.map(competition);

        assertNotNull(competitionDTO);
        assertEquals(1, competitionDTO.getId());
        assertEquals("Winter Tournament", competitionDTO.getName());
        assertEquals("A tournament for the winter season", competitionDTO.getDescription());
        assertEquals(5, competitionDTO.getAmountOfWinners());
    }

    @Test
    void testMapCompetitionListToCompetitionDTOList() {
        Competition competition1 = new Competition();
        competition1.setId(1);
        competition1.setName("Spring Fest");

        Competition competition2 = new Competition();
        competition2.setId(2);
        competition2.setName("Fall Fest");

        List<Competition> competitions = List.of(competition1, competition2);
        List<CompetitionDTO> dtos = competitionMapper.map(competitions);

        assertEquals(2, dtos.size());
        assertEquals("Spring Fest", dtos.get(0).getName());
        assertEquals("Fall Fest", dtos.get(1).getName());
    }
}
