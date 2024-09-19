package com.example.demo.controllers;

import com.example.demo.dto.general.VoteDTO;
import com.example.demo.dto.output.OutputIdeaDTO;
import com.example.demo.models.User;
import com.example.demo.models.Vote;
import com.example.demo.models.VoteType;
import com.example.demo.services.AuthenticationService;
import com.example.demo.services.IdeaService;
import com.example.demo.services.VoteService;
import com.example.demo.services.VoteTypeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public String addVote(@RequestBody @Valid VoteDTO voteDTO, BindingResult result,
                        RedirectAttributes redirectAttributes){
        if (result.hasErrors()){
            return "redirect:/";
        }
        try {
            voteService.addVote(voteDTO);
        } catch (Exception e){
            System.out.println(e);
        }
        return "redirect:/vote/vote";
    }

    @DeleteMapping(path = "{voteId}")
    public void deleteVote(@PathVariable("voteId") Integer id) {
        voteService.deleteVote(id);
    }

    @GetMapping
    public int showPoints(@RequestParam("ideaId")Integer ideaId) {
        return voteService.calculatePoints(ideaId);
    }
    private void addCommonAttributes(User currentUser, Model model) {
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("user_id", currentUser.getId());
    }


    @GetMapping("/vote")
    public String listSelectedIdeasToVote(@RequestParam(value = "search", required = false) String search, Model model) {
        User user = authenticationService.getCurrentUser();
//        List<VoteDTO> voteDTOS = voteService.findByUserId(user.getId());
        List<OutputIdeaDTO> votedByUser = voteService.findIdeasByUserId(user.getId());
        List<OutputIdeaDTO> ideas = ideaService.getFormattedIdeas(search);
        List<VoteType> voteTypes = voteTypeService.getVoteTypes();
        HashMap<Integer, Integer> votePoints = voteService.getAllPoints(1);

        Map<Integer, Integer> userVotes = new HashMap<>();
        Map<Integer, Integer> voteTypePoints = voteTypes.stream()
                .collect(Collectors.toMap(VoteType::getId, VoteType::getPoints));

        for (OutputIdeaDTO idea : votedByUser) {
            idea.getVotes().forEach(vote -> userVotes.put(idea.getId(), vote.getVoteTypeDTO().getId()));
        }

        addCommonAttributes(user, model);
        model.addAttribute("ideas", ideas);
        model.addAttribute("userVotes", userVotes);
        model.addAttribute("voteTypePoints", voteTypePoints);
        model.addAttribute("search", search);
        model.addAttribute("voteTypes", voteTypes);
        model.addAttribute("votePoints", votePoints);
        return "vote";
    }

}
