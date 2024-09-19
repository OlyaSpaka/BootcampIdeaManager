package com.example.demo.controllers;

import com.example.demo.services.VoteService;
import com.example.demo.models.Vote;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping(path = "/Vote")
public class VoteController {
    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public void addVote(@RequestBody Vote vote){
        voteService.addVote(vote);
    }

    @DeleteMapping(path = "{voteId}")
    public void deleteVote(@PathVariable("voteId") Integer id) {
        voteService.deleteVote(id);
    }

    @GetMapping
    public int showPoints(@RequestParam("ideaId")Integer ideaId) {
        return voteService.calculatePoints(ideaId);
    }

    public HashMap<Integer,Integer> getAllPoints(Integer competitionId){
        return voteService.getAllPoints(competitionId);

    }

}
