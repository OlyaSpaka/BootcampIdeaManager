package com.example.demo.services;

import com.example.demo.models.Competition;
import com.example.demo.repositories.CompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompetitionService {

    private final CompetitionRepository competitionRepository;
    @Autowired
    public CompetitionService(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
    }

    public void addCompetition(Competition competition){
        competitionRepository.save(competition);
    }

    public void deleteCompetition(Integer id){
        boolean exists = competitionRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Competition with Id " + id + " does not exist");
        } else {
            competitionRepository.deleteById(id);
        }
    }
}
