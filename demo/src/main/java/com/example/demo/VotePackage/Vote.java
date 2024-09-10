package com.example.demo.VotePackage;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Table
@Entity
public class Vote {
    private Long id;
    private int voteType_id;
    private Long user_id;
    private Long idea_id;

    public Vote() {
    }

    public Vote(Long id, int voteType_id, Long user_id, Long idea_id) {
        this.id = id;
        this.voteType_id = voteType_id;
        this.user_id = user_id;
        this.idea_id = idea_id;
    }

    public Long getIdea_id() {
        return idea_id;
    }

    public void setIdea_id(Long idea_id) {
        this.idea_id = idea_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public int getVoteType_id() {
        return voteType_id;
    }

    public void setVoteType_id(int voteType_id) {
        this.voteType_id = voteType_id;
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
                ", voteType_id=" + voteType_id +
                ", user_id=" + user_id +
                ", idea_id=" + idea_id +
                '}';
    }
}
