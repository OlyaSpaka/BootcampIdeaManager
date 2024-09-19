package com.example.demo.services;

import com.example.demo.dto.general.CommentDTO;
import com.example.demo.mapper.implementation.CommentMapper;
import com.example.demo.models.Comment;
import com.example.demo.repositories.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddComment() {
        // Arrange
        CommentDTO commentDTO = new CommentDTO();
        Comment comment = new Comment();
        when(commentMapper.map(commentDTO)).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Act
        commentService.addComment(commentDTO);

        // Assert
        verify(commentMapper, times(1)).map(commentDTO);
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testDeleteComment() {
        // Arrange
        Integer id = 1;
        when(commentRepository.existsById(id)).thenReturn(true);

        // Act
        commentService.deleteComment(id);

        // Assert
        verify(commentRepository, times(1)).existsById(id);
        verify(commentRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteComment_NotFound() {
        // Arrange
        Integer id = 1;
        when(commentRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () -> commentService.deleteComment(id));
        assertEquals("Comment with Id " + id + " does not exist", exception.getMessage());

        verify(commentRepository, times(1)).existsById(id);
        verify(commentRepository, never()).deleteById(any());
    }

    @Test
    void testUpdateComment() {
        // Arrange
        Integer id = 1;
        Comment comment = new Comment();
        comment.setContent("Old Content");

        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));

        // Act
        commentService.updateComment(id, "New Content");

        // Assert
        assertEquals("New Content", comment.getContent());
        verify(commentRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateComment_NotFound() {
        // Arrange
        Integer id = 1;
        when(commentRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () -> commentService.updateComment(id, "New Content"));
        assertEquals("comment with Id " + id + " does not exist.", exception.getMessage());

        verify(commentRepository, times(1)).findById(id);
    }

    @Test
    void testShowIdeaComments() {
        // Arrange
        Integer ideaId = 1;
        List<Comment> comments = List.of(new Comment(), new Comment());
        List<CommentDTO> commentDTOs = List.of(new CommentDTO(), new CommentDTO());

        when(commentRepository.findByIdeaId(ideaId)).thenReturn(comments);
        when(commentMapper.map(any(Comment.class))).thenReturn(new CommentDTO());

        // Act
        List<CommentDTO> result = commentService.showIdeaComments(ideaId);

        // Assert
        assertEquals(commentDTOs.size(), result.size());
        verify(commentRepository, times(1)).findByIdeaId(ideaId);
        verify(commentMapper, times(comments.size())).map(any(Comment.class));
    }
}