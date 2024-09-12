package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user") //@Table(name = "\"USER\"") for testing
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 30)
    private String username;

    @Column(unique = true, nullable = false, length = 40)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Bookmark> bookmarks = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Vote> votes = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Idea> ideas = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_role", // Join table name
            joinColumns = @JoinColumn(name = "user_id"), // Column in the join table referring to Idea
            inverseJoinColumns = @JoinColumn(name = "role_id") // Column in the join table referring to Category
    )
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
        comment.setUser(this);
    }

    public void removeComment(Comment comment){
        this.comments.remove(comment);
        comment.setUser(null);
    }

    public void addBookmark(Bookmark bookmark){
        this.bookmarks.add(bookmark);
        bookmark.setUser(this);
    }

    public void removeBookmark(Bookmark bookmark){
        this.bookmarks.remove(bookmark);
        bookmark.setUser(null);
    }

    public void addVote(Vote vote){
        this.votes.add(vote);
        vote.setUser(this);
    }

    public void removeVote(Vote vote){
        this.votes.remove(vote);
        vote.setUser(null);
    }

    public void addIdea(Idea idea){
        this.ideas.add(idea);
        idea.setUser(this);
    }

    public void removeIdea(Idea idea){
        this.ideas.remove(idea);
        idea.setUser(null);
    }

    public void addRole(Role role){
        this.roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role){
        this.roles.remove(role);
        role.getUsers().remove(this);
    }
    @Override
    public String toString() {
        return "User{" +
                "Id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
