package com.example.demo.controllers;

import com.example.demo.models.Idea;
import com.example.demo.models.User;
import com.example.demo.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
public class WebPageController {

    private final IdeaService ideaService;
    private final CompetitionService competitionService;
    private final CommentService commentService;
    private final CategoryService categoryService;
    private final UserController userController;

    public WebPageController(IdeaService ideaService, CompetitionService competitionService,
                             CommentService commentService, UserController userController, CategoryService categoryService) {
        this.ideaService = ideaService;
        this.competitionService = competitionService;
        this.commentService = commentService;
        this.userController = userController;
        this.categoryService = categoryService;
    }

    @GetMapping("/ideas")
    public String listIdeas(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Idea> ideas = ideaService.getFormattedIdeas(search);
        addCommonAttributes(model);
        model.addAttribute("ideas", ideas);
        model.addAttribute("search", search);
        return "ideas";
    }

    @GetMapping("/ideas/{id}")
    public String showIdea(@PathVariable("id") Integer id, Model model) {
        addCommonAttributes(model);
        model.addAttribute("idea", ideaService.displayIdea(id));
        model.addAttribute("comments", commentService.showIdeaComments(id));
        return "separate-idea";
    }

    @GetMapping("/new-idea")
    public String newIdeaForm(Model model) {
        addCommonAttributes(model);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "new-idea";
    }

    private void addCommonAttributes(Model model) {
        User user = userController.getCurrentUser();
        model.addAttribute("user", user.getUsername());
        model.addAttribute("competitionName", competitionService.getCompetitionName(1));
        model.addAttribute("competitionDescription", competitionService.getCompetitionDescription(1));
    }
}
