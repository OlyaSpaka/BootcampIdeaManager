package com.example.demo.controllers;

import com.example.demo.dto.general.CommentDTO;
import com.example.demo.dto.input.InputIdeaDTO;
import com.example.demo.dto.output.OutputIdeaDTO;
import com.example.demo.models.User;
import com.example.demo.services.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
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
        List<OutputIdeaDTO> ideas = ideaService.getFormattedIdeas(search);
        addCommonAttributes(model);
        model.addAttribute("ideas", ideas);
        model.addAttribute("search", search);
        return "ideas";
    }

    @GetMapping("/{userId}")
    public String showUserIdea(@PathVariable Integer userId, Model model){
        addCommonAttributes(model);
        model.addAttribute("ideaDTO", ideaService.findById(userId));
        model.addAttribute("comments", commentService.showIdeaComments(userId));
        if (!model.containsAttribute("commentDTO")){
            model.addAttribute("commentDTO", new CommentDTO());
        }
        return "separate-idea";
    }

    @GetMapping("/new-idea")
    public String newIdeaForm(Model model) {
        addCommonAttributes(model);
        model.addAttribute("allCategories", categoryService.getAllCategories());
        model.addAttribute("competitions", competitionService.findAll());
        if (!model.containsAttribute("ideaDTO")){
            model.addAttribute("ideaDTO", new InputIdeaDTO());
        }
        return "new-idea-by-ilya";
    }

    @PostMapping("/new-idea/create")
    public String addIdea(@Valid @ModelAttribute("ideaDTO") InputIdeaDTO inputIdeaDTO,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("ideaDTO", inputIdeaDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.ideaDTO", bindingResult);
            return "redirect:/ideas/new-idea";
        }
        try {
            int id = ideaService.addNewIdea(inputIdeaDTO);
            return "redirect:/ideas/"+id;
        } catch (IllegalArgumentException | ParseException e){
            redirectAttributes.addFlashAttribute("errorMsg", e);
            return "redirect:/ideas/new-idea";
        }
    }

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

    @GetMapping("/{ideaId}/edit")
    public String editIdeaForm(@PathVariable("ideaId") Integer id, Model model) {
        OutputIdeaDTO idea = ideaService.findById(id);
        if (idea == null) {
            throw new IllegalStateException("Idea with Id " + id + " does not exist");
        }
        addCommonAttributes(model);
        model.addAttribute("allCategories", categoryService.getAllCategories());
        model.addAttribute("ideaDTO", idea);
        return "edit-idea"; // Template name for the edit form
    }

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
    private void addCommonAttributes(Model model) {
        User user = authenticationService.getCurrentUser();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("user_id", user.getId());
        model.addAttribute("competitionName", competitionService.getCompetitionName(1));
        model.addAttribute("competitionDescription", competitionService.getCompetitionDescription(1));
    }
}
