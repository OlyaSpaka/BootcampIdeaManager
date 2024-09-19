package com.example.demo.services;

import com.example.demo.models.Comment;
import com.example.demo.models.Competition;
import com.example.demo.models.Idea;
import com.example.demo.models.User;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.CompetitionRepository;
import com.example.demo.repositories.IdeaRepository;
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

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class CommentServiceTest {
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    IdeaRepository ideaRepository;
    @Autowired
    CompetitionRepository competitionRepository;

    private Comment comment;
    private User user;
    private Idea idea;
    private Competition competition;

    @BeforeEach
    void setUp() {
        comment = new Comment();
        LocalDate startDate = LocalDate.of(2024,9,01);
        LocalDate endDate = LocalDate.of(2024,10,01);
        user = userRepository.save(new User("username", "email@example.com", "password"));
        competition = competitionRepository.save(new Competition("title", "description", startDate, endDate,3));
        idea = new Idea();
        idea.setCreatedAt(new Date());
        idea.setTitle("Test Idea");
        idea.setDescription("Test description");
        idea.setKeyFeatures("Test features");
        idea.setReferenceLinks("Test references");
        user.addIdea(idea);
        competition.addIdea(idea);
        idea = ideaRepository.save(idea);

        comment.setContent("Very nice idea");
        comment.setUser(user);
        comment.setIdea(idea);

        commentRepository.save(comment);

    }

//    @Test
//    void testAddComment() {
//        Comment testComment = new Comment();
//        testComment.setIdea(idea);
//        testComment.setUser(user);
//        testComment.setContent("Just want to repeat it is a very nice idea");
//
//        commentService.addComment(testComment);
//        List<Comment> commentList = commentRepository.findAll();
//        assertThat(commentList).hasSize(2);
//    }

    @Test
    void testDeleteCommentWhenExists() {
        Comment commentToDelete = new Comment();
        commentToDelete.setIdea(idea);
        commentToDelete.setUser(user);
        commentToDelete.setContent("Just want to repeat it is a very nice idea");

        commentRepository.save(commentToDelete);

        commentService.deleteComment(commentToDelete.getId());
        List<Comment> commentsAfter = commentRepository.findAll();
        assertThat(commentsAfter).doesNotContain(commentToDelete);
    }

    @Test
    void testDeleteCommentWhenNotExists() {
        assertThrows(IllegalStateException.class, () -> commentService.deleteComment(999));
    }

    @Test
    void testUpdateCommentWhenExists() {
        // When
        commentService.updateComment(comment.getId(), "This is updated comment");

        // Then
        Comment updatedComment = commentRepository.findById(comment.getId()).orElseThrow();
        assertThat(updatedComment.getContent()).isEqualTo("This is updated comment");
    }
    @Test
    void testUpdateCommentWhenNotExists() {
        assertThrows(IllegalStateException.class, () -> commentService.updateComment(9999, "invalid comment"));
    }


    @AfterEach
    void cleanUp() {
        commentRepository.deleteAll();
        ideaRepository.deleteAll();
        competitionRepository.deleteAll();
        userRepository.deleteAll();

    }
}
