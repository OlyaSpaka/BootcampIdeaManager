package com.example.demo.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("unittestuser", "test@example.com", "password123");
    }

    @Test
    public void testUserConstructor() {
        assertEquals("unittestuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
    }

    @Test
    public void testAddComment() {
        Comment comment = new Comment();
        user.addComment(comment);
        assertTrue(user.getComments().contains(comment));
        assertEquals(user, comment.getUser());
    }

    @Test
    public void testAddBookmark() {
        Bookmark bookmark = new Bookmark();
        user.addBookmark(bookmark);
        assertTrue(user.getBookmarks().contains(bookmark));
        assertEquals(user, bookmark.getUser());
    }

    @Test
    public void testAddVote() {
        Vote vote = new Vote();
        user.addVote(vote);
        assertTrue(user.getVotes().contains(vote));
        assertEquals(user, vote.getUser());
    }

    @Test
    public void testAddIdea() {
        Idea idea = new Idea();
        user.addIdea(idea);
        assertTrue(user.getIdeas().contains(idea));
        assertEquals(user, idea.getUser());
    }

    @Test
    public void testAddRole() {
        Role role = new Role("USER");
        user.addRole(role);
        assertTrue(user.getRoles().contains(role));
        assertTrue(role.getUsers().contains(user));
    }

    @Test
    public void testRemoveRole() {
        Role role = new Role("ADMIN");
        user.addRole(role);
        user.removeRole(role);
        assertFalse(user.getRoles().contains(role));
        assertFalse(role.getUsers().contains(user));
    }

    @Test
    public void testToString() {
        String expected = "User{Id=null, username='unittestuser', email='test@example.com', password='password123'}";
        assertEquals(expected, user.toString());
    }
}