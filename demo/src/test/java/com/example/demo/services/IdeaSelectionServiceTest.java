package com.example.demo.services;

import com.example.demo.models.IdeaSelection;
import com.example.demo.repositories.IdeaSelectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class IdeaSelectionServiceTest {

    @Mock
    private IdeaSelectionRepository ideaSelectionRepository;

    @InjectMocks
    private IdeaSelectionService ideaSelectionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddIdeaSelection() {
        // Arrange
        IdeaSelection ideaSelection = new IdeaSelection();
        ideaSelection.setId(1);
        when(ideaSelectionRepository.save(any(IdeaSelection.class))).thenReturn(ideaSelection);

        // Act
        ideaSelectionService.addIdeaSelection(ideaSelection);

        // Assert
        verify(ideaSelectionRepository, times(1)).save(ideaSelection);
    }

    @Test
    void testDeleteIdeaSelection_Success() {
        // Arrange
        Integer ideaSelectionId = 1;
        when(ideaSelectionRepository.existsById(ideaSelectionId)).thenReturn(true);

        // Act
        ideaSelectionService.deleteIdeaSelection(ideaSelectionId);

        // Assert
        verify(ideaSelectionRepository, times(1)).existsById(ideaSelectionId);
        verify(ideaSelectionRepository, times(1)).deleteById(ideaSelectionId);
    }

    @Test
    void testDeleteIdeaSelection_Failure() {
        // Arrange
        Integer ideaSelectionId = 1;
        when(ideaSelectionRepository.existsById(ideaSelectionId)).thenReturn(false);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> ideaSelectionService.deleteIdeaSelection(ideaSelectionId));
        assertEquals("IdeaSelection with Id 1 does not exist", exception.getMessage());

        verify(ideaSelectionRepository, never()).deleteById(ideaSelectionId);
    }
}