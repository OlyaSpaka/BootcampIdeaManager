package com.example.demo.controllers;

import com.example.demo.models.Comment;
import com.example.demo.services.VoteService;
import com.example.demo.models.Vote;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(path = "Vote")
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

/*    @PutMapping(path = "{voteId}/vote")
    public void updateVote(@PathVariable("voteId") Integer id,
                           @RequestParam(required = false) Integer user_id,
                           @RequestParam(required = false) Integer idea_id,
                           @RequestParam(required = false) Integer voteType_id) {
        voteService.updateVote(id,user_id,idea_id,voteType_id);

    }*/
    @GetMapping
    public int showPoints(@RequestParam("ideaId")Integer ideaId) {
        return voteService.calculatePoints(ideaId);
    }

    public HashMap<Integer,Integer> getAllPoints(){
        return voteService.getAllPoints();

    }
}
