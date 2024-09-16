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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    private Bookmark bookmark;
    private User user;
    private Idea idea;
    private Competition competition;

    @BeforeEach
    void setUp(){
        bookmark = new Bookmark();
        user = userRepository.save(new User("username", "email@example.com", "password"));
        competition = competitionRepository.save(new Competition("title", "description", new Date(), new Date()));
        idea = new Idea();
        idea.setCreatedAt(new Date());
        idea.setTitle("Test Idea");
        idea.setDescription("Test description");
        idea.setKeyFeatures("Test features");
        idea.setReferences("Test references");
        user.addIdea(idea);
        competition.addIdea(idea);
        idea = ideaRepository.save(idea);
        //User bookmarks his own idea. why not?
        bookmark.setIdea(idea);
        bookmark.setUser(user);

        bookmarkRepository.save(bookmark);
    }
    @Test
    void addBookmark(){
        Bookmark testBookmark = new Bookmark();
        Idea testIdea = new Idea();
        testIdea.setCreatedAt(new Date());
        testIdea.setTitle("Test Idea");
        testIdea.setDescription("Test description");
        testIdea.setKeyFeatures("Test features");
        testIdea.setReferences("Test references");
        user.addIdea(testIdea);
        competition.addIdea(testIdea);
        testIdea = ideaRepository.save(testIdea);
        //User bookmarks another of his own idea
        testBookmark.setIdea(testIdea);
        testBookmark.setUser(user);

        bookmarkService.addBookmark(testBookmark);

        List<Bookmark> bookmarkList = bookmarkRepository.findAll();
        assertThat(bookmarkList).hasSize(2);
    }
    @Test
    void deleteBookmarkWhenExists(){
        Bookmark bookmarkToDelete = new Bookmark();
        Idea testIdea = new Idea();
        testIdea.setCreatedAt(new Date());
        testIdea.setTitle("Test Idea");
        testIdea.setDescription("Test description");
        testIdea.setKeyFeatures("Test features");
        testIdea.setReferences("Test references");
        user.addIdea(testIdea);
        competition.addIdea(testIdea);
        testIdea = ideaRepository.save(testIdea);

        bookmarkToDelete.setIdea(testIdea);
        bookmarkToDelete.setUser(user);

        bookmarkRepository.save(bookmarkToDelete);

        bookmarkService.deleteBookmark(bookmarkToDelete.getId());

        List<Bookmark> bookmarksAfter = bookmarkRepository.findAll();
        assertThat(bookmarksAfter).doesNotContain(bookmarkToDelete);
    }
    @Test
    void deleteBookmarkWhenNotExists(){
        assertThrows(IllegalStateException.class, () -> bookmarkService.deleteBookmark(999));
    }
    @AfterEach
    void cleanUp(){
        bookmarkRepository.deleteAll();
        ideaRepository.deleteAll();
        competitionRepository.deleteAll();
        userRepository.deleteAll();
    }
}
