package com.example.demo.IdeaSelectionPackage;

import com.example.demo.CompetitionPackage.Competition;
import com.example.demo.IdeaPackage.Idea;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Idea_Selection")
public class IdeaSelection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "Idea_id", nullable = false)
    private Idea idea;

    @ManyToOne
    @JoinColumn(name = "competition_id", nullable = false)
    private Competition competition;
    @Column(nullable = false)
    private Date date;

    public IdeaSelection() {
    }

    public IdeaSelection(Idea idea, Competition competition, Date date) {
        this.idea = idea;
        this.competition = competition;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Idea getIdea() {
        return idea;
    }

    public void setIdea(Idea idea) {
        this.idea = idea;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
