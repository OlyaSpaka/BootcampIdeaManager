package com.example.demo.controllers;

import com.example.demo.dto.general.CommentDTO;
import com.example.demo.dto.input.InputIdeaDTO;
import com.example.demo.dto.output.OutputIdeaDTO;
import com.example.demo.models.User;
import com.example.demo.models.VoteType;
import com.example.demo.services.*;
import com.example.demo.models.Idea;
import com.example.demo.models.VoteType;
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
import java.util.*;

@Controller
@RequestMapping("/ideas")
public class IdeaController {
    private final IdeaService ideaService;
    private final CompetitionService competitionService;
    private final CommentService commentService;
    private final AuthenticationService authenticationService;
    private final CategoryService categoryService;
    private final BookmarkService bookmarkService;
    private final UserSelectionService userSelectionService;
    private final AzureBlobStorageService blobService;
    private final VoteTypeService voteTypeService;


    public IdeaController(IdeaService ideaService, CompetitionService competitionService, CommentService commentService, AuthenticationService authenticationService, CategoryService categoryService, BookmarkService bookmarkService, UserSelectionService userSelectionService, AzureBlobStorageService blobService, VoteTypeService voteTypeService) {
        this.ideaService = ideaService;
        this.competitionService = competitionService;
        this.commentService = commentService;
        this.authenticationService = authenticationService;
        this.categoryService = categoryService;
        this.bookmarkService = bookmarkService;
        this.userSelectionService = userSelectionService;
        this.blobService = blobService;
        this.voteTypeService = voteTypeService;
    }

    @GetMapping
    public String listIdeas(@RequestParam(value = "search", required = false) String search, Model model) {
        List<OutputIdeaDTO> ideas = ideaService.getFormattedIdeas(search);
        User currentUser = authenticationService.getCurrentUser();
        Map<Integer, Boolean> bookmarkStatusMap = bookmarkService.getBookmarkStatusMap(currentUser, ideas);
        addCommonAttributes(currentUser, model);
        model.addAttribute("results", userSelectionService.resultsPresent());
        model.addAttribute("preferences", userSelectionService.preferenceChoiceActive());
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
                          @RequestParam(value = "fileUpload", required = false) MultipartFile[] files) {
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("ideaDTO", inputIdeaDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.ideaDTO", bindingResult);
            return "redirect:/ideas/new-idea";
        }
        MultipartFile[] filteredFiles = Arrays.stream(files)
                .filter(file -> !file.isEmpty() && file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty())
                .toArray(MultipartFile[]::new);

        int maxFiles = 5;
        long maxSize = 2 * 1024 * 1024; // 2MB

        if (filteredFiles.length > maxFiles) {
            redirectAttributes.addFlashAttribute("errorMsg", "You can upload a maximum of 5 images.");
            return "redirect:/ideas/new-idea";
        }

        for (MultipartFile file : filteredFiles) {
            if (file.getSize() > maxSize) {
                redirectAttributes.addFlashAttribute("errorMsg", "Each file must be smaller than 2MB.");
                return "redirect:/ideas/new-idea";
            }
        }

        try {
            List<String> fileUrls = blobService.uploadFiles(filteredFiles);
            String joinResult = String.join(",", fileUrls);
            inputIdeaDTO.setPictures(joinResult.isEmpty() ? null : joinResult);

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
            OutputIdeaDTO idea = ideaService.findById(id);
            if (idea.getPictures() != null && !idea.getPictures().isEmpty()){
                for (String picture : idea.getPictures().split(",")){
                    if (!picture.isEmpty()) blobService.deleteBlob(picture);
                }
            }
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
                             @Valid @ModelAttribute("ideaDTO") OutputIdeaDTO outputIdeaDTO,
                             @RequestParam(value = "picturesToRemove", required = false) String[] picturesToRemove,
                             @RequestParam(value = "fileUpload", required = false) MultipartFile[] files,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("ideaDTO", outputIdeaDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.ideaDTO", bindingResult);
            return "redirect:/ideas/"+id+"/edit";
        }
        MultipartFile[] filteredFiles = (files != null) ? Arrays.stream(files)
                .filter(file -> !file.isEmpty() && file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty())
                .toArray(MultipartFile[]::new) : new MultipartFile[0];
        try {
            OutputIdeaDTO existingIdea = ideaService.findById(id);

            // Handle deleted pictures
            Set<String> existingPictures = new HashSet<>();
            if (existingIdea.getPictures() != null && !existingIdea.getPictures().isEmpty()){
                existingPictures = new HashSet<>(Arrays.asList(existingIdea.getPictures().split(",")));
                if (picturesToRemove != null) {
                    Set<String> picturesToRemoveSet = new HashSet<>(Arrays.asList(picturesToRemove));
                    existingPictures.removeAll(picturesToRemoveSet);

                    // Delete blobs
                    for (String picture : picturesToRemoveSet) {
                        if (!picture.trim().isEmpty()) {
                            blobService.deleteBlob(picture);
                            existingPictures.remove(picture);
                        }
                    }
                }
            }

            // Upload new files and get their URLs
            List<String> fileUrls = blobService.uploadFiles(filteredFiles);

            // Combine existing pictures with new ones
            existingPictures.addAll(fileUrls);

            // Convert the set of picture URLs to a comma-separated string
            String updatedPictures = String.join(",", existingPictures);
            outputIdeaDTO.setPictures(updatedPictures.isEmpty() ?
                    null : updatedPictures);

            // Save the updated idea
            ideaService.updateIdea(id, outputIdeaDTO);
        } catch (IllegalStateException | IOException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/ideas/" + id + "/edit";
        }
        return "redirect:/ideas/"+id;
    }
    private void addCommonAttributes(User currentUser, Model model) {
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("user_id", currentUser.getId());
        model.addAttribute("competitionName", competitionService.getCompetitionName(1));
        model.addAttribute("competitionDescription", competitionService.getCompetitionDescription(1));
    }

    @GetMapping("selected-ideas")
    public String listSelectedIdeas( Model model) {
        User user = authenticationService.getCurrentUser();
        addCommonAttributes(user, model);

        List<Idea> ideas = ideaService.getSelectedIdeas(1);
        model.addAttribute("ideas", ideas);

        return "selected-ideas";
    }

}
