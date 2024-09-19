package com.example.demo.controllers;

import com.example.demo.dto.GroupAssignmentsDTO;
import com.example.demo.dto.UserSelectionPrioritiesDTO;
import com.example.demo.models.Idea;
import com.example.demo.models.User;
import com.example.demo.repositories.IdeaRepository;
import com.example.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Controller
@RequestMapping("/user-selection")
public class UserSelectionController {

    private final IdeaRepository ideaRepository;
    private final UserSelectionService userSelectionService;
    private final AuthenticationService authenticationService;
    private final CompetitionService competitionService;

    @Autowired
    public UserSelectionController(UserSelectionService userSelectionService,
                                   AuthenticationService authenticationService,
                                   CompetitionService competitionService,
                                   IdeaRepository ideaRepository) {
        this.userSelectionService = userSelectionService;
        this.authenticationService = authenticationService;
        this.competitionService = competitionService;
        this.ideaRepository = ideaRepository;
    }

    @GetMapping
    public String showGroupSelectionPage(Model model) {
        User currentUser = authenticationService.getCurrentUser();
        List<Idea> ideas = ideaRepository.findSelectedIdeas(); // todo: replace with dto

        addCommonAttributes(currentUser, model);
        model.addAttribute("userId", currentUser.getId());
        model.addAttribute("ideas", ideas);

        return "user-selection";
    }

    @PostMapping("/submit-preferences")
    public String submitPreferences(@RequestBody UserSelectionPrioritiesDTO userSelectionPrioritiesDTO, Model model) {

        try {
            System.out.println("Received submission: " + userSelectionPrioritiesDTO.toString());

            Timestamp submissionTimestamp = Timestamp.from(Instant.parse(userSelectionPrioritiesDTO.submissionTime()));
            userSelectionService.saveUserSelections(
                    userSelectionPrioritiesDTO.userId(), userSelectionPrioritiesDTO.priorities(), submissionTimestamp
            );

            model.addAttribute("userId", userSelectionPrioritiesDTO.userId());

            return "preferences-waiting-page";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/results")
    public String showSelectionResults(Model model) {
        User currentUser = authenticationService.getCurrentUser();

        List<GroupAssignmentsDTO> groupAssignments = userSelectionService.getGroupAssignments();

        addCommonAttributes(currentUser, model);
        model.addAttribute("groupAssignments", groupAssignments);

        return "results";
    }


    @GetMapping("/preferences-waiting-page")
    public String showPreferencesWaitingPage(Model model) {
        User currentUser = authenticationService.getCurrentUser();
        addCommonAttributes(currentUser, model);
        return "preferences-waiting-page";
    }

    private void addCommonAttributes(User currentUser, Model model) {
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("user_id", currentUser.getId());
        model.addAttribute("competitionName", competitionService.getCompetitionName(1));
        model.addAttribute("competitionDescription", competitionService.getCompetitionDescription(1));
    }
}

