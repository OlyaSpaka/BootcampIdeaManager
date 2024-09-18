package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "Comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "User_id")
    private User user;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "Idea_id")
    private Idea idea;
    @Column(nullable = false)
    private String content;

    public Comment() {
    }

    public Comment(String content) {
        this.content = content;
    }
    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", username=" + user +
                ", idea=" + idea +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comm = (Comment) o;
        return Objects.equals(id, comm.id);
    }
}
