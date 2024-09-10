package com.example.demo.VoteTypePackage;

import com.example.demo.VotePackage.Vote;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "votetype")
public class VoteType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(name = "points")
    private int points;

    @OneToMany(mappedBy = "VoteType_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "VoteType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }
}
