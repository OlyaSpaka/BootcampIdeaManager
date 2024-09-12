package com.example.demo.controllers;

import com.example.demo.services.VoteTypeService;
import com.example.demo.models.VoteType;
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
    public void deleteVote(@PathVariable("voteTypeId") Integer id) {
        voteTypeService.deleteVoteType(id);
    }
}
