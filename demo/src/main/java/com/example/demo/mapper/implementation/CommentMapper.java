package com.example.demo.mapper.implementation;

import com.example.demo.dto.general.CommentDTO;
import com.example.demo.dto.output.OutputIdeaDTO;
import com.example.demo.dto.output.OutputUserDTO;
import com.example.demo.models.Comment;
import com.example.demo.repositories.IdeaRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
public class CommentMapper {
    UserRepository userRepository;
    IdeaRepository ideaRepository;

    @Autowired
    public CommentMapper(UserRepository userRepository, IdeaRepository ideaRepository) {
        this.userRepository = userRepository;
        this.ideaRepository = ideaRepository;
    }

    public Comment map(CommentDTO commentDTO) throws IllegalArgumentException{
        if (commentDTO == null) return null;

        Comment comm = new Comment(commentDTO.getContent());

        comm.setIdea(ideaRepository.findById(
                commentDTO.getIdea().getId())
                .orElseThrow(() -> new IllegalArgumentException("Idea with such id was not found"))
        );
        comm.setUser(userRepository.findById(
                        commentDTO.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User with such id was not found"))
        );

        return comm;
    }

    public CommentDTO map(Comment comment){
        if (comment == null) return null;

        String formattedDate = comment.getIdea().getCreatedAt().toString();

        if (comment.getIdea().getCreatedAt() != null) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm dd.MM.yy");
            formattedDate = dateFormatter.format(comment.getIdea().getCreatedAt());
        }

        CommentDTO commentDTO = new CommentDTO(comment.getId(), comment.getContent());
        commentDTO.setUser(new OutputUserDTO(comment.getUser().getId(), comment.getUser().getUsername()));
        commentDTO.setIdea(new OutputIdeaDTO(
                comment.getIdea().getTitle(),
                comment.getIdea().getDescription(),
                comment.getIdea().getKeyFeatures(),
                comment.getIdea().getReferenceLinks(),
                formattedDate,
                comment.getIdea().getPictures()
        ));
        return commentDTO;
    }
}
