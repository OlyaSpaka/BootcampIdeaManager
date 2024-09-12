package com.example.demo.models;

import jakarta.persistence.*;

@Entity
@Table(name = "vote")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "VoteType_id", nullable = false)
    private VoteType VoteType_id;
    @ManyToOne
    @JoinColumn(name = "User_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "Idea_id", nullable = false)
    private Idea idea;
    public Vote() {
    }

    public Vote(VoteType voteType, User user, Idea idea) {
        this.VoteType_id = voteType;
        this.user = user;
        this.idea = idea;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public VoteType getVoteType() {
        return VoteType_id;
    }

    public void setVoteType(VoteType voteType) {
        this.VoteType_id = voteType;
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

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", voteType_id=" + VoteType_id.getId() +
                ", user_id=" + user +
                ", idea_id=" + idea +
                '}';
    }
}
