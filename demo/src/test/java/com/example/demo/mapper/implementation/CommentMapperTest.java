package com.example.demo.mapper.implementation;

import com.example.demo.dto.general.CommentDTO;
import com.example.demo.dto.output.OutputIdeaDTO;
import com.example.demo.dto.output.OutputUserDTO;
import com.example.demo.models.Comment;
import com.example.demo.models.Idea;
import com.example.demo.models.User;
import com.example.demo.repositories.IdeaRepository;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentMapperTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private IdeaRepository ideaRepository;

    @InjectMocks
    private CommentMapper commentMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMapCommentDTOToComment() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("Great Idea!");

        OutputUserDTO userDTO = new OutputUserDTO();
        userDTO.setId(1);

        User user = new User();
        user.setId(1);

        OutputIdeaDTO ideaDTO = new OutputIdeaDTO();
        ideaDTO.setId(1);

        Idea idea = new Idea();
        idea.setId(1);

        commentDTO.setIdea(ideaDTO);
        commentDTO.setUser(userDTO);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(ideaRepository.findById(1)).thenReturn(Optional.of(idea));

        Comment comment = commentMapper.map(commentDTO);

        assertNotNull(comment);
        assertEquals("Great Idea!", comment.getContent());
        assertEquals(user, comment.getUser());
        assertEquals(idea, comment.getIdea());
    }

    @Test
    void testMapCommentToCommentDTO() throws ParseException {
        User user = new User();
        user.setId(1);
        user.setUsername("john_doe");

        Idea idea = new Idea();
        idea.setId(1);
        idea.setTitle("Innovative Idea");
        idea.setDescription("An innovative approach");
        idea.setKeyFeatures("Feature1, Feature2");
        idea.setReferenceLinks("https://example.com");
        idea.setCreatedAt(new SimpleDateFormat("HH:mm dd.MM.yy").parse("14:00 15.09.24"));
        idea.setPictures("pic.jpg");

        Comment comment = new Comment();
        comment.setId(1);
        comment.setContent("Great Idea!");
        comment.setUser(user);
        comment.setIdea(idea);

        CommentDTO commentDTO = commentMapper.map(comment);

        assertNotNull(commentDTO);
        assertEquals(1, commentDTO.getId());
        assertEquals("Great Idea!", commentDTO.getContent());
        assertEquals(1, commentDTO.getUser().getId());
        assertEquals("john_doe", commentDTO.getUser().getUsername());
        assertEquals("Innovative Idea", commentDTO.getIdea().getTitle());
    }
}
