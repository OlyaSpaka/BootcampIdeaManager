package com.example.demo.services;

import com.example.demo.models.Competition;
import com.example.demo.repositories.CompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CompetitionService {

    private final CompetitionRepository competitionRepository;

    @Autowired
    public CompetitionService(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
    }

    public void addCompetition(Competition competition) {
        competitionRepository.save(competition);
    }

    public void deleteCompetition(Integer id) {
        boolean exists = competitionRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Competition with Id " + id + " does not exist");
        } else {
            competitionRepository.deleteById(id);
        }
    }

    @Transactional
    public void updateCompetitionContent(Integer id,
                                         String description,
                                         String name) {
        Competition competition = competitionRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "Competition with Id " + id + " does not exist."));
        if (name != null && !name.isEmpty()) {
            competition.setName(name);
        }

        if (description != null && !description.isEmpty()) {
            competition.setDescription(description);
        }
    }

    @Transactional
    public void updateCompetitionDate(Integer id,
                                         Date start,
                                         Date end) {
        Competition competition = competitionRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "Competition with Id " + id + " does not exist."));
        if (start != null) {
            competition.setStartDate(start);
        }

        if (end != null) {
            competition.setEndDate(end);
        }
    }
}
