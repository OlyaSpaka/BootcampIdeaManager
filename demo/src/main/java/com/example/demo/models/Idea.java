package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "idea")
public class Idea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(name = "key_features")
    private String keyFeatures;

    @Column(name = "reference_links")
    private String referenceLinks;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Transient
    private String formattedDate;

    @Column
    private String pictures;

    @OneToMany(mappedBy = "idea", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "idea", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Bookmark> bookmarks = new HashSet<>();

    @OneToMany(mappedBy = "idea", cascade = CascadeType.ALL, orphanRemoval = true)
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
    public Idea(String title, String description, String keyFeatures, String referenceLinks, Date createdAt, String pictures) {
        this.description = description;
        this.title = title;
        this.keyFeatures = keyFeatures;
        this.referenceLinks = referenceLinks;
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

    @Override
    public String toString() {
        return "Idea{" +
                "id=" + id +
                ", competition_id=" + competition.getId() +
                ", user_id=" + user.getId() +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", key_features='" + keyFeatures + '\'' +
                ", references='" + referenceLinks + '\'' +
                ", created_at=" + createdAt +
                ", pictures='" + pictures + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Idea idea = (Idea) o;
        return Objects.equals(id, idea.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
