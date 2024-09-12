package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "votetype")
public class VoteType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 30)
    private String name;
    @Column(name = "points")
    private int points;
    @OneToMany(mappedBy = "VoteType", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Vote> votes = new HashSet<>();

    public VoteType() {
    }

    public VoteType(String name, int points) {
        this.name = name;
        this.points = points;
    }

    public void addVote(Vote vote){
        this.votes.add(vote);
        vote.setVoteType(this);
    }

    public void removeVote(Vote vote){
        this.votes.remove(vote);
        vote.setVoteType(null);
    }
    @Override
    public String toString() {
        return "VoteType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteType voteType = (VoteType) o;
        return id != null && id.equals(voteType.id);
    }
}
