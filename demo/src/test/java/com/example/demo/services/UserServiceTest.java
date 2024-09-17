package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
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
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private User user = new User();

    @BeforeEach
    void setUp(){
        user.setUsername("username");
        user.setEmail("email@email");
        user.setPassword("password");
        userRepository.save(user);
    }

    @Test
    void addUser(){
        User testUser = new User();
        testUser.setUsername("newUser");
        testUser.setEmail("newEmail@google.com");
        testUser.setPassword("newPassword");

        userService.addUser(testUser);

        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(2);
        assertThat(userList).extracting(User::getUsername).contains("newUser");
    }
    @Test
    void deleteUserWhenExists(){
        User userToDelete = new User();
        userToDelete.setUsername("newUser");
        userToDelete.setEmail("newEmai11l@verygoodmood.com");
        userToDelete.setPassword("newPassword");

        userRepository.save(userToDelete);

        List<User> userListBefore = userRepository.findAll();
        assertThat(userListBefore).hasSize(2);
        userService.deleteUser(userToDelete.getId());
        List<User> userListAfter = userRepository.findAll();
        assertThat(userListAfter).hasSize(1);
        assertThat(userListAfter).doesNotContain(userToDelete);

    }
    @Test
    void deleteUserWhenNotExists(){
        assertThrows(IllegalStateException.class, () -> userService.deleteUser(999));
    }
    @AfterEach
    void cleanUp(){
        userRepository.deleteAll();
    }



}
