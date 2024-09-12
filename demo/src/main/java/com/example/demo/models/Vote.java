package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "vote")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "VoteType_id")
    private VoteType VoteType;

    @ManyToOne
    @JoinColumn(name = "User_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "Idea_id")
    private Idea idea;

    public Vote() {
    }
    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", voteType_id=" + VoteType.getId() +
                ", user_id=" + user +
                ", idea_id=" + idea +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return id != null && id.equals(vote.id);
    }
}
