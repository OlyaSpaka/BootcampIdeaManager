package com.example.demo.services;

import com.example.demo.dto.general.CompetitionDTO;
import com.example.demo.mapper.implementation.CompetitionMapper;
import com.example.demo.models.Competition;
import com.example.demo.repositories.CompetitionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CompetitionServiceTest {
    @Mock
    private CompetitionRepository competitionRepository;
    @Mock
    private CompetitionMapper competitionMapper;
    @InjectMocks
    private CompetitionService competitionService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testAddCompetition() {
        // Arrange
        Competition competition = new Competition();
        when(competitionRepository.save(any(Competition.class))).thenReturn(competition);

        // Act
        competitionService.addCompetition(competition);

        // Assert
        verify(competitionRepository, times(1)).save(competition);
    }

    @Test
    void testDeleteCompetition() {
        // Arrange
        Integer id = 1;
        when(competitionRepository.existsById(id)).thenReturn(true);

        // Act
        competitionService.deleteCompetition(id);

        // Assert
        verify(competitionRepository, times(1)).existsById(id);
        verify(competitionRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteCompetition_NotFound() {
        // Arrange
        Integer id = 1;
        when(competitionRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () -> competitionService.deleteCompetition(id));
        assertEquals("Competition with Id " + id + " does not exist", exception.getMessage());

        verify(competitionRepository, times(1)).existsById(id);
        verify(competitionRepository, never()).deleteById(any());
    }

    @Test
    void testFindAllCompetitions() {
        // Arrange
        List<Competition> competitions = List.of(new Competition(), new Competition());
        List<CompetitionDTO> competitionDTOs = List.of(new CompetitionDTO(), new CompetitionDTO());

        when(competitionRepository.findAll()).thenReturn(competitions);
        when(competitionMapper.map(competitions)).thenReturn(competitionDTOs);

        // Act
        List<CompetitionDTO> result = competitionService.findAll();

        // Assert
        assertEquals(competitionDTOs, result);
        verify(competitionRepository, times(1)).findAll();
        verify(competitionMapper, times(1)).map(competitions);
    }

    @Test
    void testUpdateCompetitionContent() {
        // Arrange
        Integer id = 1;
        Competition competition = new Competition();
        competition.setName("Old Name");
        competition.setDescription("Old Description");

        when(competitionRepository.findById(id)).thenReturn(Optional.of(competition));

        // Act
        competitionService.updateCompetitionContent(id, "New Description", "New Name");

        // Assert
        assertEquals("New Name", competition.getName());
        assertEquals("New Description", competition.getDescription());
        verify(competitionRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateCompetitionDate() {
        // Arrange
        Integer id = 1;
        Competition competition = new Competition();
        Date oldStartDate = new Date();
        Date oldEndDate = new Date();
        competition.setStartDate(oldStartDate);
        competition.setEndDate(oldEndDate);

        Date newStartDate = new Date(oldStartDate.getTime() + 100000);
        Date newEndDate = new Date(oldEndDate.getTime() + 100000);

        when(competitionRepository.findById(id)).thenReturn(Optional.of(competition));

        // Act
        competitionService.updateCompetitionDate(id, newStartDate, newEndDate);

        // Assert
        assertEquals(newStartDate, competition.getStartDate());
        assertEquals(newEndDate, competition.getEndDate());
        verify(competitionRepository, times(1)).findById(id);
    }

    @Test
    void testGetCompetitionName() {
        // Arrange
        Integer id = 1;
        Competition competition = new Competition();
        competition.setName("Competition Name");

        when(competitionRepository.findById(id)).thenReturn(Optional.of(competition));

        // Act
        String name = competitionService.getCompetitionName(id);

        // Assert
        assertEquals("Competition Name", name);
        verify(competitionRepository, times(1)).findById(id);
    }

    @Test
    void testGetCompetitionDescription() {
        // Arrange
        Integer id = 1;
        Competition competition = new Competition();
        competition.setDescription("Competition Description");

        when(competitionRepository.findById(id)).thenReturn(Optional.of(competition));

        // Act
        String description = competitionService.getCompetitionDescription(id);

        // Assert
        assertEquals("Competition Description", description);
        verify(competitionRepository, times(1)).findById(id);
    }
}
