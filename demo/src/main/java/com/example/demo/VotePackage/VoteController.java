package com.example.demo.VotePackage;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "Vote")
public class VoteController {
    private final VoteService voteService;


    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public void addVote(@RequestBody  Vote vote){
        voteService.addVote(vote);
    }

    @DeleteMapping(path = "{voteId}")
    public void deleteVote(@PathVariable("voteId") Long id) {
        voteService.deleteVote(id);
    }
    @PutMapping(path = "{voteId}/vote")
    public void updateVote(@PathVariable("voteId") Long id,
                           @RequestParam(required = false) Long user_id,
                           @RequestParam(required = false) Long idea_id,
                           @RequestParam(required = false) Long voteType_id) {
        voteService.updateVote(id,user_id,idea_id,voteType_id);

    }
}
