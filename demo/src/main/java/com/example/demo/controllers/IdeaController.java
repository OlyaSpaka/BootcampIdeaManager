package com.example.demo.controllers;

import com.example.demo.dto.general.CommentDTO;
import com.example.demo.dto.input.InputIdeaDTO;
import com.example.demo.dto.output.OutputIdeaDTO;
import com.example.demo.models.User;
import com.example.demo.services.*;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.ParseException;
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
    private final AzureBlobStorageService blobService;

    public IdeaController(IdeaService ideaService, CompetitionService competitionService, CommentService commentService, AuthenticationService authenticationService, CategoryService categoryService, BookmarkService bookmarkService, AzureBlobStorageService blobService) {
        this.ideaService = ideaService;
        this.competitionService = competitionService;
        this.commentService = commentService;
        this.authenticationService = authenticationService;
        this.categoryService = categoryService;
        this.bookmarkService = bookmarkService;
        this.blobService = blobService;
    }

    @GetMapping
    public String listIdeas(@RequestParam(value = "search", required = false) String search, Model model) {
        List<OutputIdeaDTO> ideas = ideaService.getFormattedIdeas(search);
        User currentUser = authenticationService.getCurrentUser();
        Map<Integer, Boolean> bookmarkStatusMap = bookmarkService.getBookmarkStatusMap(currentUser, ideas);
        addCommonAttributes(currentUser, model);
        model.addAttribute("ideas", ideas);
        model.addAttribute("bookmarkStatusMap", bookmarkStatusMap);
        model.addAttribute("search", search);
        return "ideas";
    }

    @GetMapping("/my-ideas")
    public String listMyIdeas(Model model) {
        User currentUser = authenticationService.getCurrentUser();
        List<OutputIdeaDTO> userIdeas = ideaService.displayIdeasByUser(currentUser);
        Map<Integer, Boolean> bookmarkStatusMap = bookmarkService.getBookmarkStatusMap(currentUser, userIdeas);
        addCommonAttributes(authenticationService.getCurrentUser(), model);
        model.addAttribute("ideas", userIdeas);
        model.addAttribute("bookmarkStatusMap", bookmarkStatusMap);
        return "ideas";
    }

    @GetMapping("/bookmarks")
    public String listBookmarkedIdeas(Model model) {
        User currentUser = authenticationService.getCurrentUser();
        List<OutputIdeaDTO> bookmarkedIdeas = bookmarkService.getUserBookmarkedIdeas(currentUser.getId());
        Map<Integer, Boolean> bookmarkStatusMap = bookmarkService.getBookmarkStatusMap(currentUser, bookmarkedIdeas);
        addCommonAttributes(authenticationService.getCurrentUser(), model);
        model.addAttribute("ideas", bookmarkedIdeas);
        model.addAttribute("bookmarkStatusMap", bookmarkStatusMap);
        return "ideas";
    }

    @GetMapping("/{userId}")
    public String showUserIdea(@PathVariable Integer userId, Model model){
        User currentUser = authenticationService.getCurrentUser();
        OutputIdeaDTO idea = ideaService.findById(userId);
        Map<Integer, Boolean> bookmarkStatusMap = bookmarkService.getBookmarkStatusMap(currentUser, List.of(idea));
        addCommonAttributes(currentUser, model);
        model.addAttribute("ideaDTO", idea);
        model.addAttribute("comments", commentService.showIdeaComments(userId));
        model.addAttribute("bookmarkStatusMap", bookmarkStatusMap);
        if (!model.containsAttribute("commentDTO")){
            model.addAttribute("commentDTO", new CommentDTO());
        }
        return "separate-idea";
    }

    @PreAuthorize("hasRole('ROLE_User')")
    @GetMapping("/new-idea")
    public String newIdeaForm(Model model) {
        addCommonAttributes(authenticationService.getCurrentUser(), model);
        model.addAttribute("allCategories", categoryService.getAllCategories());
        model.addAttribute("competitions", competitionService.findAll());
        if (!model.containsAttribute("ideaDTO")){
            model.addAttribute("ideaDTO", new InputIdeaDTO());
        }
        return "new-idea-by-ilya";
    }

    @PreAuthorize("hasRole('ROLE_User')")
    @PostMapping("/new-idea/create")
    public String addIdea(@Valid @ModelAttribute("ideaDTO") InputIdeaDTO inputIdeaDTO,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          @RequestParam("fileUpload") MultipartFile[] files) {
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("ideaDTO", inputIdeaDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.ideaDTO", bindingResult);
            return "redirect:/ideas/new-idea";
        }

        int maxFiles = 5;
        long maxSize = 2 * 1024 * 1024; // 2MB

        if (files.length > maxFiles) {
            redirectAttributes.addFlashAttribute("errorMsg", "You can upload a maximum of 5 images.");
            return "redirect:/ideas/new-idea";
        }

        for (MultipartFile file : files) {
            if (file.getSize() > maxSize) {
                redirectAttributes.addFlashAttribute("errorMsg", "Each file must be smaller than 2MB.");
                return "redirect:/ideas/new-idea";
            }
        }

        try {
            List<String> fileUrls = blobService.uploadFiles(files);
            inputIdeaDTO.setPictures(String.join(",", fileUrls));

            int id = ideaService.addNewIdea(inputIdeaDTO);
            return "redirect:/ideas/"+id;
        } catch (IllegalArgumentException | IOException | ParseException e){
            redirectAttributes.addFlashAttribute("errorMsg", e);
            return "redirect:/ideas/new-idea";
        }
    }

    @PreAuthorize("hasRole('ROLE_User')")
    @PostMapping("/{ideaId}/delete")
    public String deleteIdea(@PathVariable("ideaId") Integer id, RedirectAttributes redirectAttributes) {
        try {
            ideaService.deleteIdea(id);
        } catch (IllegalStateException e){
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/ideas/"+id;
        }
        return "redirect:/ideas";
    }

    @PreAuthorize("hasRole('ROLE_User')")
    @GetMapping("/{ideaId}/edit")
    public String editIdeaForm(@PathVariable("ideaId") Integer id, Model model) {
        OutputIdeaDTO idea = ideaService.findById(id);
        if (idea == null) {
            throw new IllegalStateException("Idea with Id " + id + " does not exist");
        }
        addCommonAttributes(authenticationService.getCurrentUser(), model);
        model.addAttribute("allCategories", categoryService.getAllCategories());
        model.addAttribute("ideaDTO", idea);
        return "edit-idea"; // Template name for the edit form
    }

    @PreAuthorize("hasRole('ROLE_User')")
    @PostMapping("/{ideaId}/update")
    public String updateIdea(@PathVariable("ideaId") Integer id,
                           @Valid @ModelAttribute("ideaDTO")OutputIdeaDTO outputIdeaDTO,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("ideaDTO", outputIdeaDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.ideaDTO", bindingResult);
            return "redirect:/ideas/"+id+"/edit";
        }
        try {
            ideaService.updateName(id, outputIdeaDTO);
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/ideas/"+id+"/edit";
        }
        return "redirect:/ideas/"+id;
    }
    private void addCommonAttributes(User currentUser, Model model) {
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("user_id", currentUser.getId());
        model.addAttribute("competitionName", competitionService.getCompetitionName(1));
        model.addAttribute("competitionDescription", competitionService.getCompetitionDescription(1));
    }
}
