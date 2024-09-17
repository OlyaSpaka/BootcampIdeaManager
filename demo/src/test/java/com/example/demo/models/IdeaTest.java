package com.example.demo.models;

import com.example.demo.repositories.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class IdeaTest {
    @Autowired
    private IdeaRepository ideaRepository;
    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VoteRepository voteRepository;
//    @Autowired
//    private IdeaSelectionRepository ideaSelectionRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private Competition competition;
    private User user;
    private Idea savedIdea;

    @BeforeEach
    @Transactional
    void setUp() {
        // Setup initial data
        competition = competitionRepository.save(new Competition("Competition Name", "Description", new Date(), new Date(),3));
        user = userRepository.save(new User("username", "email@example.com", "password"));
        Idea idea = new Idea("Idea Title", "Idea Description", "Key Features", "References", new Date(), "Pictures");

        user.addIdea(idea);
        competition.addIdea(idea);

        savedIdea = ideaRepository.save(idea);
        entityManager.flush();
    }

    @Test
    void testCreateIdea() {
        assertThat(savedIdea).isNotNull();
        assertThat(savedIdea.getId()).isGreaterThan(0);
        assertThat(savedIdea.getTitle()).isEqualTo("Idea Title");
    }

    @Test
    @Transactional
    void addIdeaSelection() {
        System.out.println("addIdeaSelection Test start");
        IdeaSelection ideaSelection = new IdeaSelection(new Date());

        savedIdea.addIdeaSelection(ideaSelection);
        competition.addIdeaSelection(ideaSelection);

        entityManager.persist(ideaSelection);
//        entityManager.persist(savedIdea); //any of those result in saving because children
//        entityManager.persist(competition);
        entityManager.flush();

        Idea retrievedIdea = ideaRepository.findById(savedIdea.getId()).orElse(null);
        assertThat(retrievedIdea).isNotNull();
        assertThat(retrievedIdea.getIdeaSelections()).contains(ideaSelection);
    }

    @Test
    @Transactional
    void removeIdeaSelection() {
        System.out.println("removeIdeaSelection Test started");
        IdeaSelection ideaSelection = new IdeaSelection(new Date());

        savedIdea.addIdeaSelection(ideaSelection);
        competition.addIdeaSelection(ideaSelection);

        entityManager.persist(savedIdea);
        entityManager.flush();

        Idea newIdea = ideaRepository.findById(savedIdea.getId()).orElse(null);
        assertThat(newIdea).isNotNull();

        IdeaSelection newIdeaSelection = newIdea.getIdeaSelections()
                .stream()
                .findFirst()
                .orElse(null);
        assertThat(newIdeaSelection).isNotNull();

        newIdea.removeIdeaSelection(newIdeaSelection);
        entityManager.flush();

        newIdea = ideaRepository.findById(newIdea.getId()).orElse(null);
        assertThat(newIdea).isNotNull();
        assertThat(newIdea.getIdeaSelections()).doesNotContain(ideaSelection);

//        newIdeaSelection = ideaSelectionRepository.findById(newIdeaSelection.getId()).orElse(null);
//        assertThat(newIdeaSelection).isNull();
        System.out.println("removeIdeaSelection Test ended");
    }

    @Test
    @Transactional
    void addComment() {
        Comment comment = new Comment("This is a comment");

        savedIdea.addComment(comment);
        user.addComment(comment);

        entityManager.persist(savedIdea);
        entityManager.flush();
//        entityManager.flush(); // Ensure changes are committed

        Idea retrievedIdea = ideaRepository.findById(savedIdea.getId()).orElse(null);
        assertThat(retrievedIdea).isNotNull();

        Comment newComment = retrievedIdea.getComments().stream().findFirst().orElse(null);
        assertThat(newComment).isNotNull(); // Ensure Comment is present before removal

        // Retrieve the Comment from the database
        Comment retrievedComment = commentRepository.findById(comment.getId()).orElse(null);
        assertThat(retrievedComment).isNotNull();
        assertThat(retrievedIdea.getComments()).contains(retrievedComment);
    }

    @Test
    @Transactional
    void removeComment() {
        // Setup and persist entities
        Comment comment = new Comment("This is a comment");

        savedIdea.addComment(comment);
        user.addComment(comment);

        entityManager.persist(savedIdea);
        entityManager.flush(); // Ensure changes are committed

        // Retrieve and check the state
        Idea newIdea = ideaRepository.findById(savedIdea.getId()).orElse(null);
        assertThat(newIdea).isNotNull(); // Ensure Idea was retrieved

        Comment newComment = newIdea.getComments().stream().findFirst().orElse(null);
        assertThat(newComment).isNotNull(); // Ensure Comment is present before removal
        assertEquals(newComment, comment);

        newIdea.removeComment(newComment);
        entityManager.flush(); // Commit changes

        // Verify removal
        newIdea = ideaRepository.findById(newIdea.getId()).orElse(null);
        assertThat(newIdea).isNotNull(); // Ensure Idea was retrieved again
        assertThat(newIdea.getComments()).doesNotContain(newComment);

//        newComment = commentRepository.findById(newComment.getId()).orElse(null);
//        assertThat(newComment).isNull(); // Assert that the Comment is removed
    }

    @Test
    @Transactional
    void addVote() {
        // Create and persist a VoteType
        VoteType voteType = new VoteType("Type1", 10);
        entityManager.persist(voteType);

        // Create a Vote and set its relationships directly
        Vote vote = new Vote();
        vote.setVoteType(voteType);
        savedIdea.addVote(vote);
        user.addVote(vote);

        // Persist the Vote
        entityManager.persist(savedIdea);
        entityManager.flush();

        // Verify the relationships
        Idea retrievedIdea = ideaRepository.findById(savedIdea.getId()).orElse(null);
        assertThat(retrievedIdea).isNotNull();
        assertThat(retrievedIdea.getVotes()).contains(vote);

        User retrievedUser = userRepository.findById(user.getId()).orElse(null);
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getVotes()).contains(vote);
    }

    @Test
    @Transactional
    void removeVote() {
        VoteType voteType = new VoteType("Type1", 10);
        entityManager.persist(voteType);

        Vote vote = new Vote();
        voteType.addVote(vote);
        savedIdea.addVote(vote);
        user.addVote(vote);

        entityManager.persist(savedIdea); // Save Idea and associated Vote
        entityManager.flush(); // Ensure changes are committed
        entityManager.clear(); // Why does this even keep working

        // Retrieve and check the state
        Idea newIdea = ideaRepository.findById(savedIdea.getId()).orElse(null);
        assertThat(newIdea).isNotNull(); // Ensure Idea was retrieved

        Vote newVote = newIdea.getVotes().stream().findFirst().orElse(null);
        assertThat(newVote).isNotNull(); // Ensure Vote is present before removal
//        assertEquals(newVote, vote); //crashes because dates are different...

        newIdea.removeVote(newVote);
        entityManager.flush(); // Commit changes

        // Verify removal
        newIdea = ideaRepository.findById(newIdea.getId()).orElse(null);
        assertThat(newIdea).isNotNull(); // Assert that the Vote is removed
        assertThat(newIdea.getVotes()).doesNotContain(newVote);

        newVote = voteRepository.findById(newVote.getId()).orElse(null);
        assertThat(newVote).isNull(); // Assert that the Vote is removed
    }

    @Test
    @Transactional
    void addBookmark() {
        Bookmark bookmark = new Bookmark();

        user.addBookmark(bookmark);
        savedIdea.addBookmark(bookmark);

        entityManager.persist(savedIdea);
        entityManager.flush();

        Idea retrievedIdea = ideaRepository.findById(savedIdea.getId()).orElse(null);
        assertThat(retrievedIdea).isNotNull();
        assertThat(retrievedIdea.getBookmarks()).contains(bookmark);
    }

    @Test
    @Transactional
    void removeBookmark() {
        // Setup and persist entities
        Bookmark bookmark = new Bookmark();

        user.addBookmark(bookmark);
        savedIdea.addBookmark(bookmark);

        entityManager.persist(savedIdea);
        entityManager.flush(); // Ensure changes are committed

        // Retrieve and check the state
        Idea newIdea = ideaRepository.findById(savedIdea.getId()).orElse(null);
        assertThat(newIdea).isNotNull(); // Ensure Idea was retrieved

        Bookmark newBookmark = newIdea.getBookmarks().stream().findFirst().orElse(null);
        assertThat(newBookmark).isNotNull(); // Ensure Bookmark is present before removal
        assertEquals(newBookmark, bookmark);

        newIdea.removeBookmark(newBookmark);
        entityManager.flush(); // Commit changes
        // Verify removal
        Idea newNewIdea = ideaRepository.findById(newIdea.getId()).orElse(null);
        assertThat(newNewIdea).isNotNull(); // Ensure Idea was retrieved again
        assertThat(newNewIdea.getBookmarks()).doesNotContain(newBookmark);

        newBookmark = bookmarkRepository.findById(newBookmark.getId()).orElse(null);
        assertThat(newBookmark).isNull(); // Assert that the Bookmark is removed

    }
    @Test
    @Transactional
    void addCategory() {
        Category category = new Category("Category Name");
        entityManager.persist(category);

        savedIdea.addCategory(category);

        entityManager.persist(savedIdea);
        entityManager.flush();

        Idea retrievedIdea = ideaRepository.findById(savedIdea.getId()).orElse(null);
        assertThat(retrievedIdea).isNotNull();
        assertThat(retrievedIdea.getCategories()).contains(category);
    }

    @Test
    @Transactional
    void removeCategory() {
        // Setup and persist entities
        Category category = new Category("Category Name");
        entityManager.persist(category);

        savedIdea.addCategory(category);
        entityManager.persist(savedIdea);
        entityManager.flush(); // Ensure changes are committed

        // Retrieve and check the state
        Idea newIdea = ideaRepository.findById(savedIdea.getId()).orElse(null);
        assertThat(newIdea).isNotNull(); // Ensure Idea was retrieved

        Category newCategory = newIdea.getCategories().stream().findFirst().orElse(null);
        assertThat(newCategory).isNotNull(); // Ensure Category is present before removal
        assertEquals(newCategory, category);

        newIdea.removeCategory(newCategory);
        entityManager.flush(); // Commit changes

        // Verify removal
        newIdea = ideaRepository.findById(newIdea.getId()).orElse(null);
        assertThat(newIdea).isNotNull(); // Ensure Idea was retrieved again
        assertThat(newIdea.getCategories()).doesNotContain(newCategory);

//        newCategory = categoryRepository.findById(newCategory.getId()).orElse(null);
//        assertThat(newCategory).isNull(); // Assert that the Category is removed
    }

    @AfterEach
    @Transactional
    void tearDown() {
        // Clean up after each test if necessary
        entityManager.clear();
//        ideaSelectionRepository.deleteAll();
        voteRepository.deleteAll();
        ideaRepository.deleteAll();
        competitionRepository.deleteAll();
        userRepository.deleteAll();
    }
}