package com.example.demo.services;

import com.example.demo.models.Competition;
import com.example.demo.models.VoteType;
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

import java.util.Date;
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
        competition = competitionRepository.save(new Competition("title","description", new Date(), new Date()));
        competitionRepository.save(competition);
    }
    @Test
    void addCompetition(){
        Competition newCompetition = new Competition();
        newCompetition.setName("newCompetition");
        newCompetition.setDescription("newDescription");
        newCompetition.setStartDate(new Date());
        newCompetition.setEndDate(new Date());

        competitionService.addCompetition(newCompetition);

        List<Competition> competitions = competitionRepository.findAll();
        assertThat(competitions).hasSize(2);
        assertThat(competitions).extracting(Competition::getName).contains("newCompetition");
    }

    @Test
    void deleteCompetitionWhenExists(){
        Competition competitionsToDelete = new Competition();
        competitionsToDelete.setName("newCompetition");
        competitionsToDelete.setDescription("newDescription");
        competitionsToDelete.setStartDate(new Date());
        competitionsToDelete.setEndDate(new Date());


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
