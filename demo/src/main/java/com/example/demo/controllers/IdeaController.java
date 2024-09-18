package com.example.demo.controllers;

import com.example.demo.dto.IdeaDTO;
import com.example.demo.models.User;
import com.example.demo.services.*;
import com.example.demo.models.Idea;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ideas")
public class IdeaController {
    private final IdeaService ideaService;
    private final CompetitionService competitionService;
    private final CommentService commentService;
    private final AuthenticationService authenticationService;
    private final CategoryService categoryService;
    private final BookmarkService bookmarkService;

    public IdeaController(IdeaService ideaService, CompetitionService competitionService, CommentService commentService, AuthenticationService authenticationService, CategoryService categoryService, BookmarkService bookmarkService) {
        this.ideaService = ideaService;
        this.competitionService = competitionService;
        this.commentService = commentService;
        this.authenticationService = authenticationService;
        this.categoryService = categoryService;
        this.bookmarkService = bookmarkService;
    }

    @GetMapping
    public String listIdeas(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Idea> ideas = ideaService.getFormattedIdeas(search);
        User currentUser = authenticationService.getCurrentUser();
        Map<Integer, Boolean> bookmarkStatusMap = bookmarkService.getBookmarkStatusMap(currentUser, ideas);
        addCommonAttributes(authenticationService.getCurrentUser(), model);
        model.addAttribute("ideas", ideas);
        model.addAttribute("bookmarkStatusMap", bookmarkStatusMap);
        model.addAttribute("search", search);
        return "ideas";
    }

    @GetMapping("/my-ideas")
    public String listMyIdeas(Model model) {
        User currentUser = authenticationService.getCurrentUser();
        List<Idea> userIdeas = ideaService.displayIdeasByUser(currentUser);
        Map<Integer, Boolean> bookmarkStatusMap = bookmarkService.getBookmarkStatusMap(currentUser, userIdeas);
        addCommonAttributes(authenticationService.getCurrentUser(), model);
        model.addAttribute("ideas", userIdeas);
        model.addAttribute("bookmarkStatusMap", bookmarkStatusMap);
        return "ideas";
    }

    @GetMapping("/bookmarks")
    public String listBookmarkedIdeas(Model model) {
        User currentUser = authenticationService.getCurrentUser();
        List<Idea> bookmarkedIdeas = bookmarkService.getUserBookmarkedIdeas(currentUser.getId());
        Map<Integer, Boolean> bookmarkStatusMap = bookmarkService.getBookmarkStatusMap(currentUser, bookmarkedIdeas);
        addCommonAttributes(authenticationService.getCurrentUser(), model);
        model.addAttribute("ideas", bookmarkedIdeas);
        model.addAttribute("bookmarkStatusMap", bookmarkStatusMap);
        return "ideas";
    }

    @GetMapping("/{userId}")
    public String showUserIdea(@PathVariable Integer userId, Model model) {
        User currentUser = authenticationService.getCurrentUser();
        Idea idea = ideaService.displayIdea(userId);
        Map<Integer, Boolean> bookmarkStatusMap = bookmarkService.getBookmarkStatusMap(currentUser, List.of(idea));
        addCommonAttributes(authenticationService.getCurrentUser(), model);
        model.addAttribute("idea", ideaService.displayIdea(userId));
        model.addAttribute("comments", commentService.showIdeaComments(userId));
        model.addAttribute("bookmarkStatusMap", bookmarkStatusMap);
        return "separate-idea";
    }

    @GetMapping("/new-idea")
    public String newIdeaForm(Model model) {
        addCommonAttributes(authenticationService.getCurrentUser(), model);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("ideaDTO", new IdeaDTO());
        return "new-idea-by-ilya";
    }

    @PostMapping("/new-idea/create")
    public String addIdea(@Valid @ModelAttribute("ideaDTO") IdeaDTO ideaDTO,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "redirect:/ideas/new-idea";
        }
        return "ideas";
    }

    @PostMapping("/{ideaId}/delete")
    public void deleteIdea(@PathVariable("ideaId") Integer id) {
        ideaService.deleteIdea(id);
    }

    @PostMapping("/{ideaId}/update")
    public void updateIdea(@PathVariable("ideaId") Integer id,
                           @RequestParam(required = false) String description,
                           @RequestParam(required = false) String title,
                           @RequestParam(required = false) String keyFeatures,
                           @RequestParam(required = false) String referenceLinks) {
        ideaService.updateName(id, description, title, keyFeatures, referenceLinks);
    }

    private void addCommonAttributes(User currentUser, Model model) {
        model.addAttribute("user", currentUser.getUsername());
        model.addAttribute("user_id", currentUser.getId());
        model.addAttribute("competitionName", competitionService.getCompetitionName(1));
        model.addAttribute("competitionDescription", competitionService.getCompetitionDescription(1));
    }

}
