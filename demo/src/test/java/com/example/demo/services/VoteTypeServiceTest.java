package com.example.demo.services;

import com.example.demo.models.VoteType;
import com.example.demo.repositories.VoteTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class VoteTypeServiceTest {
    @Mock
    private VoteTypeRepository voteTypeRepository;

    @InjectMocks
    private VoteTypeService voteTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks before each test
    }

    @Test
    void testAddVoteType() {
        // Arrange
        VoteType voteType = new VoteType();
        voteType.setId(1);
        voteType.setPoints(10);
        when(voteTypeRepository.save(any(VoteType.class))).thenReturn(voteType);

        // Act
        voteTypeService.addVoteType(voteType);

        // Assert
        verify(voteTypeRepository, times(1)).save(voteType); // Verify save method is called once
    }

    @Test
    void testDeleteVoteType_Success() {
        // Arrange
        Integer voteTypeId = 1;
        when(voteTypeRepository.existsById(voteTypeId)).thenReturn(true);

        // Act
        voteTypeService.deleteVoteType(voteTypeId);

        // Assert
        verify(voteTypeRepository, times(1)).existsById(voteTypeId); // Verify existence check
        verify(voteTypeRepository, times(1)).deleteById(voteTypeId); // Verify delete method is called
    }

    @Test
    void testDeleteVoteType_Failure() {
        // Arrange
        Integer voteTypeId = 1;
        when(voteTypeRepository.existsById(voteTypeId)).thenReturn(false);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> voteTypeService.deleteVoteType(voteTypeId));
        assertEquals("VoteType with Id 1 does not exist", exception.getMessage());

        // Verify that deleteById is never called if vote type doesn't exist
        verify(voteTypeRepository, never()).deleteById(voteTypeId);
    }
}
