package com.example.demo.services;

import com.example.demo.dto.output.OutputIdeaDTO;
import com.example.demo.mapper.interf.IdeaMapperInt;
import com.example.demo.models.Bookmark;
import com.example.demo.models.Idea;
import com.example.demo.models.User;
import com.example.demo.repositories.BookmarkRepository;
import com.example.demo.repositories.IdeaRepository;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookmarkServiceTest {
    @InjectMocks
    private BookmarkService bookmarkService;

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IdeaRepository ideaRepository;

    @Mock
    private IdeaMapperInt ideaMapperInt;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testAddBookmark() {
        // Arrange
        Integer userId = 1;
        Integer ideaId = 1;
        User user = new User();
        user.setId(userId);
        Idea idea = new Idea();
        idea.setId(ideaId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ideaRepository.findById(ideaId)).thenReturn(Optional.of(idea));

        // Act
        bookmarkService.addBookmark(ideaId, userId);

        // Assert
        verify(bookmarkRepository, times(1)).save(any(Bookmark.class));
    }

    @Test
    void testDeleteBookmark() {
        // Arrange
        Integer userId = 1;
        Integer ideaId = 1;

        // Act
        bookmarkService.deleteBookmark(ideaId, userId);

        // Assert
        verify(bookmarkRepository, times(1)).deleteByIdeaIdAndUserId(ideaId, userId);
    }

    @Test
    void testGetUserBookmarks() {
        // Arrange
        Integer userId = 1;
        List<Bookmark> bookmarks = List.of(new Bookmark(), new Bookmark());
        when(bookmarkRepository.findByUserId(userId)).thenReturn(bookmarks);

        // Act
        List<Bookmark> result = bookmarkService.getUserBookmarks(userId);

        // Assert
        assertEquals(bookmarks.size(), result.size());
        verify(bookmarkRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetUserBookmarkedIdeas() {
        // Arrange
        Integer userId = 1;
        List<Bookmark> bookmarks = List.of(createBookmark(1));
        OutputIdeaDTO ideaDTO = new OutputIdeaDTO();
        when(bookmarkRepository.findByUserId(userId)).thenReturn(bookmarks);
        when(ideaMapperInt.map(any(Idea.class))).thenReturn(ideaDTO);

        // Act
        List<OutputIdeaDTO> result = bookmarkService.getUserBookmarkedIdeas(userId);

        // Assert
        assertEquals(1, result.size());
        verify(bookmarkRepository, times(1)).findByUserId(userId);
        verify(ideaMapperInt, times(1)).map(any(Idea.class));
    }


    @Test
    void testGetBookmarkStatusMap() {
        // Arrange
        Integer userId = 1;
        User currentUser = new User();
        currentUser.setId(userId);

        Idea idea1 = new Idea();
        idea1.setId(1);
        Idea idea2 = new Idea();
        idea2.setId(2);

        OutputIdeaDTO dto1 = new OutputIdeaDTO();
        dto1.setId(1);
        OutputIdeaDTO dto2 = new OutputIdeaDTO();
        dto2.setId(2);

        Bookmark bookmark1 = new Bookmark();
        bookmark1.setIdea(idea1);

        List<OutputIdeaDTO> ideas = List.of(dto1, dto2);
        List<Bookmark> bookmarks = List.of(bookmark1);
        when(bookmarkRepository.findByUserId(userId)).thenReturn(bookmarks);

        // Act
        Map<Integer, Boolean> result = bookmarkService.getBookmarkStatusMap(currentUser, ideas);

        // Assert
        assertTrue(result.get(1));
        assertFalse(result.get(2));
        verify(bookmarkRepository, times(1)).findByUserId(userId);
    }
    private Bookmark createBookmark(Integer ideaId) {
        Bookmark bookmark = new Bookmark();
        Idea idea = new Idea();
        idea.setId(ideaId);
        bookmark.setIdea(idea);
        return bookmark;
    }
}
