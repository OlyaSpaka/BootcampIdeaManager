package com.example.demo.VotePackage;

import jakarta.persistence.*;

@Table
@Entity
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long Vote_type_id;
    private Long User_id;
    private Long Idea_id;

    public Vote() {
    }

    public Vote(Long id, Long vote_type_id, Long user_id, Long idea_id) {
        this.id = id;
        this.Vote_type_id = vote_type_id;
        this.User_id = user_id;
        this.Idea_id = idea_id;
    }

    public Long getIdea_id() {
        return Idea_id;
    }

    public void setIdea_id(Long idea_id) {
        this.Idea_id = idea_id;
    }

    public Long getUser_id() {
        return User_id;
    }

    public void setUser_id(Long user_id) {
        this.User_id = user_id;
    }

    public Long getVote_type_id() {
        return Vote_type_id;
    }

    public void setVote_type_id(Long vote_type_id) {
        this.Vote_type_id = vote_type_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", voteType_id=" + Vote_type_id +
                ", user_id=" + User_id +
                ", idea_id=" + Idea_id +
                '}';
    }
}
