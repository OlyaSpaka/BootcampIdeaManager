package com.example.demo.controllers;

import com.example.demo.dto.output.OutputIdeaDTO;
import com.example.demo.models.User;
import com.example.demo.models.Vote;
import com.example.demo.models.VoteType;
import com.example.demo.services.AuthenticationService;
import com.example.demo.services.IdeaService;
import com.example.demo.services.VoteService;
import com.example.demo.services.VoteTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(path = "/vote")
public class VoteController {
    private final VoteService voteService;
    private final IdeaService ideaService;
    private final VoteTypeService voteTypeService;
    private final AuthenticationService authenticationService;
    public VoteController(VoteService voteService, IdeaService ideaService, VoteTypeService voteTypeService, AuthenticationService authenticationService) {
        this.voteService = voteService;
        this.ideaService = ideaService;
        this.voteTypeService = voteTypeService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public void addVote(@RequestBody Vote vote) {
        voteService.addVote(vote);
    }

    @DeleteMapping(path = "{voteId}")
    public void deleteVote(@PathVariable("voteId") Integer id) {
        voteService.deleteVote(id);
    }

    @GetMapping
    public int showPoints(@RequestParam("ideaId") Integer ideaId) {
        return voteService.calculatePoints(ideaId);
    }

    public HashMap<Integer, Integer> getAllPoints(Integer competitionId) {
        return voteService.getAllPoints(competitionId);

    }
    private void addCommonAttributes(User currentUser, Model model) {
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("user_id", currentUser.getId());
    }

    @GetMapping("/vote")
    public String listSelectedIdeasToVote(@RequestParam(value = "search", required = false) String search, Model model) {
        User user = authenticationService.getCurrentUser();
        List<OutputIdeaDTO> ideas = ideaService.getFormattedIdeas(search);
        List<VoteType> voteTypes = voteTypeService.getVoteTypes();
        HashMap<Integer, Integer> votePoints = voteService.getAllPoints(1);
        addCommonAttributes(user, model);
        model.addAttribute("ideas", ideas);
        model.addAttribute("search", search);
        model.addAttribute("voteTypes", voteTypes);
        model.addAttribute("votePoints", votePoints);
        return "vote.html";
    }

}
