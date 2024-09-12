package com.example.demo.services;

import com.example.demo.models.Comment;
import com.example.demo.models.Idea;
import com.example.demo.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }

    public void deleteComment(Integer id) {
        boolean exists = commentRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Comment with Id " + id + " does not exist");
        } else {
            commentRepository.deleteById(id);
        }
    }

    public List<Comment> showIdeaComments(Integer ideaId){
        return commentRepository.findByIdeaId(ideaId);
    }
}
