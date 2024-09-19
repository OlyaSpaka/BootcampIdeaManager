package com.example.demo.services;

import com.example.demo.models.Competition;
import com.example.demo.repositories.CompetitionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class CompetitionServiceTest {
    @Autowired
    private CompetitionService competitionService;
    @Autowired
    CompetitionRepository competitionRepository;
    private Competition competition;

    @BeforeEach
    void setUp(){
        LocalDate startDate = LocalDate.of(2024,9,1);
        LocalDate endDate = LocalDate.of(2024,10,1);
        competition = competitionRepository.save(new Competition("title","description", startDate, endDate,3));
        competitionRepository.save(competition);
    }
    @Test
    void addCompetition(){
        LocalDate startDate = LocalDate.of(2024,9,1);
        LocalDate endDate = LocalDate.of(2024,10,1);
        Competition newCompetition = new Competition();
        newCompetition.setName("newCompetition");
        newCompetition.setDescription("newDescription");
        newCompetition.setStartDate(startDate);
        newCompetition.setEndDate(endDate);

        competitionService.addCompetition(newCompetition);

        List<Competition> competitions = competitionRepository.findAll();
        assertThat(competitions).hasSize(2);
        assertThat(competitions).extracting(Competition::getName).contains("newCompetition");
    }

    @Test
    void deleteCompetitionWhenExists(){
        LocalDate startDate = LocalDate.of(2024,9,1);
        LocalDate endDate = LocalDate.of(2024,10,1);
        Competition competitionsToDelete = new Competition();
        competitionsToDelete.setName("newCompetition");
        competitionsToDelete.setDescription("newDescription");
        competitionsToDelete.setStartDate(startDate);
        competitionsToDelete.setEndDate(endDate);



        competitionRepository.save(competitionsToDelete);

        competitionService.deleteCompetition(competitionsToDelete.getId());
        List<Competition> competitionsAfter = competitionRepository.findAll();
        assertThat(competitionsAfter).doesNotContain(competitionsToDelete);
    }
    @Test
    void deleteCompetitionWhenNotExists(){
        assertThrows(IllegalStateException.class, () -> competitionService.deleteCompetition(999));
    }

    @AfterEach
    void cleanUp(){
        competitionRepository.deleteAll();

    }
}
