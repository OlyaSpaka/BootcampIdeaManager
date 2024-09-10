package com.example.demo.VoteTypePackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "VoteType")
public class VoteTypeController {

    private final VoteTypeService voteTypeService;

    public VoteTypeController(VoteTypeService voteTypeService) {
        this.voteTypeService = voteTypeService;
    }
    @PostMapping
    public void addVoteType(@RequestBody VoteType voteType){
        voteTypeService.addVoteType(voteType);
    }
    @DeleteMapping(path = "{voteTypeId}")
    public void deleteVote(@PathVariable("voteTypeId") Long id) {
        voteTypeService.deleteVoteType(id);
    }
}
