package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
@Table(name = "Comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "User_id", nullable = false)
    private User user;
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "Idea_id", nullable = false)
    private Idea idea;
    @Column(nullable = false)
    private String content;

    public Comment() {
    }

    public Comment(User user, Idea idea, String content) {
        this.user = user;
        this.idea = idea;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Idea getIdea() {
        return idea;
    }

    public void setIdea(Idea idea) {
        this.idea = idea;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", user=" + user +
                ", idea=" + idea +
                ", content='" + content + '\'' +
                '}';
    }
}
