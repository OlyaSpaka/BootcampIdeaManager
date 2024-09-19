package com.example.demo.controllers;

import com.example.demo.dto.general.CommentDTO;
import com.example.demo.dto.output.OutputIdeaDTO;
import com.example.demo.services.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "user", roles = {"User"})
    public void testAddComment() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(1);
        commentDTO.setContent("This is a comment");
        commentDTO.setIdea(new OutputIdeaDTO()); // Adjust as per your DTO structure
        commentDTO.getIdea().setId(1);

        mockMvc.perform(MockMvcRequestBuilders.post("/comment/create")
                        .flashAttr("commentDTO", commentDTO)
                        .param("content", "This is a comment")
                        .param("idea.id", "1")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ideas/1"));

        verify(commentService, times(1)).addComment(commentDTO);
    }

    @Test
    @WithMockUser(username = "user", roles = {"User"})
    public void testAddCommentValidationFailure() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        OutputIdeaDTO idea = new OutputIdeaDTO();
        idea.setId(0);
        commentDTO.setIdea(idea);
        commentDTO.setContent(""); // Invalid content for testing validation

        mockMvc.perform(MockMvcRequestBuilders.post("/comment/create")
                        .flashAttr("commentDTO", commentDTO)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ideas/0")) // Adjust as per your redirection logic
                .andExpect(MockMvcResultMatchers.flash().attributeExists("org.springframework.validation.BindingResult.commentDTO"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("commentDTO"));

        // Verify that addComment was not called due to validation failure
        verify(commentService, never()).addComment(any(CommentDTO.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"User"})
    public void testDeleteComment() throws Exception {
        int commentId = 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/comment/{commentId}", commentId)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(commentService, times(1)).deleteComment(commentId);
    }
}
