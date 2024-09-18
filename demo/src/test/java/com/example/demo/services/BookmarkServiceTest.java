package com.example.demo.services;

import com.example.demo.models.*;
import com.example.demo.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class BookmarkServiceTest {

    @Autowired
    BookmarkService bookmarkService;
    @Autowired
    BookmarkRepository bookmarkRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    IdeaRepository ideaRepository;
    @Autowired
    CompetitionRepository competitionRepository;
    @Autowired
    AuthenticationService authenticationService;

    private User user;
    private Idea idea;
    private Competition competition;

//    @BeforeEach
//    void setUp() {
//        competition = competitionRepository.save(new Competition("title", "description", new Date(), new Date(), 3));
//        user = createUser();
//        idea = createIdea(user);
//    }

    @Test
    void addBookmark() {
        bookmarkService.addBookmark(idea.getId(), user.getId());

        assertTrue(bookmarkRepository.findAll().stream().anyMatch(bookmark -> {
            return bookmark.getIdea().getId().equals(idea.getId()) && bookmark.getUser().getId().equals(user.getId());
        }));
    }

    @Test
    void deleteBookmark() {
        Bookmark bookmarkToDelete = new Bookmark();
        bookmarkToDelete.setIdea(idea);
        bookmarkToDelete.setUser(user);
        bookmarkRepository.save(bookmarkToDelete);

        bookmarkService.deleteBookmark(bookmarkToDelete.getUser().getId(), bookmarkToDelete.getIdea().getId());

        assertFalse(bookmarkRepository.findAll().stream().anyMatch(bookmark -> {
            return bookmark.getIdea().getId().equals(idea.getId()) && bookmark.getUser().getId().equals(user.getId());
        }));
    }

//    public User createUser() {
//        try {
//            User testUser = new User();
//            String username = UUID.randomUUID().toString().substring(0, 15);
//            testUser.setUsername(username);
//            testUser.setEmail(UUID.randomUUID().toString().substring(0, 15) + "@example.com");
//            testUser.setPassword("password");
//            authenticationService.registerNewUser(testUser);
//            return userRepository.findByUsername(username).get();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    public Idea createIdea(User user) {
        Idea testIdea = new Idea();
        testIdea.setCreatedAt(new Date());
        testIdea.setTitle("Test Idea");
        testIdea.setDescription("Test description");
        testIdea.setKeyFeatures("Test features");
        testIdea.setReferenceLinks("Test references");
        testIdea.setUser(user);
        testIdea.setCompetition(competition);
        return ideaRepository.save(testIdea);
    }

    @AfterEach
    void cleanUp() {
        bookmarkRepository.deleteAll();
        ideaRepository.deleteAll();
        competitionRepository.deleteAll();
        userRepository.deleteAll();
    }
}
