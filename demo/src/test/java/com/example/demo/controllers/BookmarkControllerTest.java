package com.example.demo.controllers;

import com.example.demo.dto.BookmarkRequestDTO;
import com.example.demo.models.Bookmark;
import com.example.demo.services.BookmarkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookmarkController.class)
@WithMockUser(username = "user", roles = {"User"})
public class BookmarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookmarkService bookmarkService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddBookmark() throws Exception {
        BookmarkRequestDTO requestDTO = new BookmarkRequestDTO(1, 1, true);

        mockMvc.perform(post("/bookmark/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Bookmark saved successfully"));

        verify(bookmarkService, times(1)).addBookmark(1, 1);
    }

    @Test
    public void testDeleteBookmark() throws Exception {
        BookmarkRequestDTO requestDTO = new BookmarkRequestDTO(1, 1, false);

        mockMvc.perform(post("/bookmark/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Bookmark deleted successfully"));

        verify(bookmarkService, times(1)).deleteBookmark(1, 1);
    }

    @Test
    public void testGetUserBookmarks() throws Exception {
        Bookmark bookmark = new Bookmark();
        bookmark.setId(1);
        when(bookmarkService.getUserBookmarks(1)).thenReturn(Collections.singletonList(bookmark));

        mockMvc.perform(get("/bookmark")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(bookmarkService, times(1)).getUserBookmarks(1);
    }
}
