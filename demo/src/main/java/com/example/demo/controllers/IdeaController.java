package com.example.demo.controllers;

import com.example.demo.dto.IdeaDTO;
import com.example.demo.models.User;
import com.example.demo.services.*;
import com.example.demo.models.Idea;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/ideas")
public class IdeaController {
    private final IdeaService ideaService;
    private final CompetitionService competitionService;
    private final CommentService commentService;
    private final AuthenticationService authenticationService;
    private final CategoryService categoryService;

    public IdeaController(IdeaService ideaService, CompetitionService competitionService, CommentService commentService, AuthenticationService authenticationService, CategoryService categoryService) {
        this.ideaService = ideaService;
        this.competitionService = competitionService;
        this.commentService = commentService;
        this.authenticationService = authenticationService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listIdeas(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Idea> ideas = ideaService.getFormattedIdeas(search);
        addCommonAttributes(model);
        model.addAttribute("ideas", ideas);
        model.addAttribute("search", search);
        return "ideas";
    }

    @GetMapping("/{userId}")
    public String showUserIdea(@PathVariable Integer userId, Model model){
        addCommonAttributes(model);
        model.addAttribute("idea", ideaService.displayIdea(userId));
        model.addAttribute("comments", commentService.showIdeaComments(userId));
        return "separate-idea";
    }

    @PreAuthorize("hasRole('ROLE_User')")
    @GetMapping("/new-idea")
    public String newIdeaForm(Model model) {
        addCommonAttributes(model);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("ideaDTO", new IdeaDTO());
        return "new-idea-by-ilya";
    }

    @PreAuthorize("hasRole('ROLE_User')")
    @PostMapping("/new-idea/create")
    public String addIdea(@Valid @ModelAttribute("ideaDTO") IdeaDTO ideaDTO,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()){
            return "redirect:/ideas/new-idea";
        }
        return "/ideas";
    }

    @PreAuthorize("hasRole('ROLE_User')")
    @PostMapping("/{ideaId}/delete")
    public void deleteIdea(@PathVariable("ideaId") Integer id) {
        ideaService.deleteIdea(id);
    }

    @PreAuthorize("hasRole('ROLE_User')")
    @PostMapping("/{ideaId}/update")
    public void updateIdea(@PathVariable("ideaId") Integer id,
                           @RequestParam(required = false) String description,
                           @RequestParam(required = false) String title,
                           @RequestParam(required = false) String keyFeatures,
                           @RequestParam(required = false) String referenceLinks) {
        ideaService.updateName(id, description, title, keyFeatures, referenceLinks);
    }
    private void addCommonAttributes(Model model) {
        User user = authenticationService.getCurrentUser();
        model.addAttribute("user", user.getUsername());
        model.addAttribute("user_id", user.getId());
        model.addAttribute("competitionName", competitionService.getCompetitionName(1));
        model.addAttribute("competitionDescription", competitionService.getCompetitionDescription(1));
    }
}
