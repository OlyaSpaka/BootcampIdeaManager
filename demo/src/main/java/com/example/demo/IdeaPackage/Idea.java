package com.example.demo.IdeaPackage;

import com.example.demo.BookmarkPackage.Bookmark;
import com.example.demo.Category.Category;
import com.example.demo.CommentPackage.Comment;
import com.example.demo.CompetitionPackage.Competition;
import com.example.demo.IdeaSelectionPackage.IdeaSelection;
import com.example.demo.UserPackage.User;
import com.example.demo.VotePackage.Vote;
import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "idea")
public class Idea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "competition_id", nullable = false)
    private Competition competition;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private String title;
    @Column
    private String description;
    @Column(name = "key_features")
    private String keyFeatures;
    @Column(name = "'references'")
    private String references;
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
    @Column
    private String pictures;

    @OneToMany(mappedBy = "idea", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "idea", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Bookmark> bookmarks = new HashSet<>();

    @OneToMany(mappedBy = "idea", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Vote> votes = new HashSet<>();

    @OneToMany(mappedBy = "idea", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<IdeaSelection> ideaSelections = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "idea_category", // Join table name
            joinColumns = @JoinColumn(name = "idea_id"), // Column in the join table referring to Idea
            inverseJoinColumns = @JoinColumn(name = "category_id") // Column in the join table referring to Category
    )
    private Set<Category> categories = new HashSet<>();

    public Idea() {
    }
    public Idea(Competition competition, User user, String description, String title, String keyFeatures, String references, Date createdAt, String pictures) {
        this.competition = competition;
        this.user = user;
        this.description = description;
        this.title = title;
        this.keyFeatures = keyFeatures;
        this.references = references;
        this.createdAt = createdAt;
        this.pictures = pictures;
    }

    public void addIdeaSelection(IdeaSelection ideaSelection){
        this.ideaSelections.add(ideaSelection);
        ideaSelection.setIdea(this);
    }

    public void removeIdeaSelection(IdeaSelection ideaSelection){
        this.ideaSelections.remove(ideaSelection);
        ideaSelection.setIdea(null);
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
        comment.setIdea(this);
    }

    public void removeComment(Comment comment){
        this.comments.remove(comment);
        comment.setIdea(null);
    }

    public void addVote(Vote vote){
        this.votes.add(vote);
        vote.setIdea(this);
    }

    public void removeVote(Vote vote){
        this.votes.remove(vote);
        vote.setIdea(null);
    }

    public void addBookmark(Bookmark bookmark){
        this.bookmarks.add(bookmark);
        bookmark.setIdea(this);
    }
    public void removeBookmark(Bookmark bookmark){
        this.bookmarks.remove(bookmark);
        bookmark.setIdea(null);
    }

    public void addCategory(Category category){
        this.categories.add(category);
        category.getIdeas().add(this);
    }

    public void removeCategory(Category category){
        this.categories.remove(category);
        category.getIdeas().remove(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
        competition.getIdeas().add(this);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeyFeatures() {
        return keyFeatures;
    }

    public void setKeyFeatures(String keyFeatures) {
        this.keyFeatures = keyFeatures;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(Set<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

    public Set<IdeaSelection> getIdeaSelections() {
        return ideaSelections;
    }

    public void setIdeaSelections(Set<IdeaSelection> ideaSelections) {
        this.ideaSelections = ideaSelections;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Idea{" +
                "id=" + id +
                ", competition_id=" + competition.getId() +
                ", user_id=" + user.getId() +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", key_features='" + keyFeatures + '\'' +
                ", references='" + references + '\'' +
                ", created_at=" + createdAt +
                ", pictures='" + pictures + '\'' +
                '}';
    }
}
