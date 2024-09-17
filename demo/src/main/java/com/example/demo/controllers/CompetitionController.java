package com.example.demo.controllers;

import com.example.demo.models.Competition;
import com.example.demo.services.CompetitionService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(path = "Competition")
public class CompetitionController {

    private final CompetitionService competitionService;

    public CompetitionController(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    @PostMapping
    public void addCompetition(@RequestBody Competition competition) {
        competitionService.addCompetition(competition);
    }

    @DeleteMapping(path = "{competitionId}")
    public void deleteCompetition(@PathVariable("competitionId") Integer id) {
        competitionService.deleteCompetition(id);
    }

    //Updates competition title and description.
    @PutMapping(path = "{competitionId}/content")
    public void updateCompetitionContent(@PathVariable("competitionId") Integer id,
                                         @RequestParam(required = false) String description,
                                         @RequestParam(required = false) String title) {
        competitionService.updateCompetitionContent(id, description, title);

    }

    @PutMapping(path = "{competitionId}/date")
    public void updateCompetitionDate(@PathVariable("competitionId") Integer id,
                                         @RequestParam(required = false) Date start,
                                         @RequestParam(required = false) Date end) {
        competitionService.updateCompetitionDate(id, start, end);

    }
}
