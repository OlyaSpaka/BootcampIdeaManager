package com.example.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "appuser") //@Table(name = "\"USER\"") for testing
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 6, max = 30, message = "Username must have between 6 and 30 characters.")
    @NotEmpty(message = "Username field must be filled out.")
    @Pattern(regexp = "^[a-zA-Z0-9._\\-?!]+$", message = "No special characters, except for .?!-_")
    @Column(unique = true, nullable = false, length = 30)
    private String username;

    @Email
    @NotEmpty(message = "Email field must be filled out.")
    @Column(unique = true, nullable = false, length = 40)
    private String email;

    @Size(min=6, message = "Password must have at least 6 characters.")
    @NotEmpty(message = "Password field must be filled out.")
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

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
