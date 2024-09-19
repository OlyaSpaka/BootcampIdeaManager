package com.example.demo.services;

import com.example.demo.models.Role;
import com.example.demo.repositories.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testAddRole() {
        // Arrange
        Role role = new Role();
        role.setId(1);
        role.setName("Admin");
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        // Act
        roleService.addRole(role);

        // Assert
        verify(roleRepository, times(1)).save(role); // Verify save method is called once
    }

    @Test
    void testDeleteRole_Success() {
        // Arrange
        Integer roleId = 1;
        when(roleRepository.existsById(roleId)).thenReturn(true);

        // Act
        roleService.deleteRole(roleId);

        // Assert
        verify(roleRepository, times(1)).existsById(roleId);
        verify(roleRepository, times(1)).deleteById(roleId);
    }

    @Test
    void testDeleteRole_Failure() {
        // Arrange
        Integer roleId = 1;
        when(roleRepository.existsById(roleId)).thenReturn(false);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> roleService.deleteRole(roleId));
        assertEquals("Role with Id 1 does not exist", exception.getMessage());

        verify(roleRepository, never()).deleteById(roleId);
    }
}
