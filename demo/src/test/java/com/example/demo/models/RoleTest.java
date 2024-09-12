package com.example.demo.models;

import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
class RoleTest {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private Role role;

    @BeforeEach
    @Transactional
    void setUp() {
        User user = userRepository.save(new User("username", "email@example.com", "password"));
        role = roleRepository.save(new Role("ROLE_USER"));

        role.addUser(user);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @Transactional
    void testCreateRole() {
        assertThat(role).isNotNull();
        assertThat(role.getId()).isGreaterThan(0);
        assertThat(role.getName()).isEqualTo("ROLE_USER");
    }

    @Test
    @Transactional
    void addUserToRole() {
        User newUser = new User("anotherUser", "another@example.com", "password");
//        userRepository.save(newUser);

        role.addUser(newUser);
        entityManager.persist(newUser);
        entityManager.flush();
        entityManager.clear();

        Role retrievedRole = roleRepository.findById(role.getId()).orElse(null);
        assertThat(retrievedRole).isNotNull();
        assertThat(retrievedRole.getUsers()).contains(newUser);

        User retrievedUser = userRepository.findById(newUser.getId()).orElse(null);
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getRoles()).contains(role);
    }

    @Test
    @Transactional
    void removeUserFromRole() {
        User newUser = new User("anotherUser", "another@example.com", "password");
//        userRepository.save(newUser);

        role.addUser(newUser);
        entityManager.persist(newUser);
        entityManager.flush();
        entityManager.clear();

        Role retrievedRole = roleRepository.findById(role.getId()).orElse(null);
        assertThat(retrievedRole).isNotNull();

        newUser = userRepository.findById(newUser.getId()).orElse(null);
        assertThat(newUser).isNotNull();

        retrievedRole.removeUser(newUser);
        entityManager.flush();

        retrievedRole = roleRepository.findById(role.getId()).orElse(null);
        assertThat(retrievedRole).isNotNull();
        assertThat(retrievedRole.getUsers()).doesNotContain(newUser);

        newUser = userRepository.findById(newUser.getId()).orElse(null);
        assertThat(newUser).isNotNull();
        assertThat(newUser.getRoles()).doesNotContain(role);
    }

    @Transactional
    @Test
    void testEquals() {
        Role newRole = roleRepository.findById(role.getId()).orElse(null);
        assertThat(newRole).isNotNull();

        assertEquals(newRole, role);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        entityManager.clear();
        roleRepository.deleteAll();
        userRepository.deleteAll();
    }
}