package com.example.demo.services;

import com.example.demo.models.Role;
import com.example.demo.repositories.RoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;
    private Role testRole;

    @BeforeEach
    void setUp() {
        testRole = new Role();
        testRole.setName("TestRole");

        roleRepository.save(testRole);

    }

    @Test
    void testAddNewRole() {

        //Given
        Role newRole = new Role();
        newRole.setName("NewRole");

        //When
        roleService.addRole(newRole);
        //Then
        List<Role> roles = roleRepository.findAll();
        assertThat(roles).hasSize(2);
        assertThat(roles).extracting(Role::getName).contains("NewRole");
    }

    @Test
    void testDeleteRoleWhenExists(){
        //Given
        Role roleToDelete = new Role();
        roleToDelete.setName("NewRole");

        roleRepository.save(roleToDelete);

        //When
        roleService.deleteRole(roleToDelete.getId());

        //Then

        List<Role> rolesAfter = roleRepository.findAll();
        assertThat(rolesAfter).doesNotContain(roleToDelete);
    }

    @Test
    void testDeleteRoleWhenNotExists(){
        assertThrows(IllegalStateException.class, () -> roleService.deleteRole(999));
    }
    @AfterEach
    void cleanUp() {
        roleRepository.deleteAll();
    }
}
