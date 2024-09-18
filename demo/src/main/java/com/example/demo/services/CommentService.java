package com.example.demo.services;

import com.example.demo.dto.general.CommentDTO;
import com.example.demo.mapper.implementation.CommentMapper;
import com.example.demo.models.Comment;
import com.example.demo.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    @Autowired
    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    public void addComment(CommentDTO commentDTO) {
        commentRepository.save(commentMapper.map(commentDTO));
    }

    public void deleteComment(Integer id) {
        boolean exists = commentRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Comment with Id " + id + " does not exist");
        } else {
            commentRepository.deleteById(id);
        }
    }

    @Transactional
    public void updateComment(Integer id,
                              String content) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "comment with Id " + id + " does not exist."));
        if (content != null && !content.isEmpty()) {
            comment.setContent(content);
        }
    }

    public List<CommentDTO> showIdeaComments(Integer ideaId) {
        List<CommentDTO> resultList = new ArrayList<>();
        commentRepository.findByIdeaId(ideaId).forEach(comment ->
                resultList.add(commentMapper.map(comment))
        );
        return resultList;
    }
}
