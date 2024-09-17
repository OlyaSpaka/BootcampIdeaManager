package com.example.demo.controllers;

import com.example.demo.dto.general.CommentDTO;
import com.example.demo.services.CommentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping(path = "/comment")
public class CommentController {
    private final CommentService commentService;
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create")
    public String addComment(@ModelAttribute("commentDTO") @Valid CommentDTO commentDTO,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.commentDTO", bindingResult);
            redirectAttributes.addFlashAttribute("commentDTO", commentDTO);
            return "redirect:/ideas/"+commentDTO.getIdea().getId();
        }
        try {
            commentService.addComment(commentDTO);
        } catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("errorMsg", e);
        }
        return "redirect:/ideas/"+commentDTO.getIdea().getId();
    }

    @DeleteMapping(path = "{commentId}")
    public void deleteComment(@PathVariable("commentId") Integer id) {
        commentService.deleteComment(id);
    }

//    @GetMapping
//    public List<Comment> showIdeaComments(@RequestParam("ideaId")Integer ideaId){
//        return commentService.showIdeaComments(ideaId);
//    }
}
