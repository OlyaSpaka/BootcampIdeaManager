package com.example.demo.controllers;

import com.example.demo.models.Competition;
import com.example.demo.services.CompetitionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "Competition")
public class CompetitionController {

    private final CompetitionService competitionService;

    public CompetitionController(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    @PostMapping
    public void addCompetition(@RequestBody Competition competition){
        competitionService.addCompetition(competition);
    }

    @DeleteMapping(path = "{competitionId}")
    public void deleteCompetition(@PathVariable("competitionId") Integer id){
        competitionService.deleteCompetition(id);
    }
}
