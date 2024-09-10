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
    public void deleteVote(@PathVariable("voteId") Integer id) {
        voteService.deleteVote(id);
    }
    @PutMapping(path = "{voteId}/vote")
    public void updateVote(@PathVariable("voteId") Integer id,
                           @RequestParam(required = false) Integer user_id,
                           @RequestParam(required = false) Integer idea_id,
                           @RequestParam(required = false) Integer voteType_id) {
        voteService.updateVote(id,user_id,idea_id,voteType_id);

    }
}
