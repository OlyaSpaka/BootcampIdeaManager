package com.example.demo.controllers;

import com.example.demo.models.Comment;
import com.example.demo.services.CommentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "Comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public void addComment(@RequestBody Comment comment) {
        commentService.addComment(comment);
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    @DeleteMapping(path = "{commentId}")
    public void deleteComment(@PathVariable("commentId") Integer id) {
        commentService.deleteComment(id);
    }

    @GetMapping
    public List<Comment> showIdeaComments(@RequestParam("ideaId")Integer ideaId){
        return commentService.showIdeaComments(ideaId);
    }

}
