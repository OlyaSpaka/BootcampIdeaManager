package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "Competition")
public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name="start_date", nullable = false)
    private Date startDate;

    @Column(name="end_date")
    private Date endDate;

    @Column(name = "amount_of_winners", nullable = false)
    private int amountOfWinners;

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<IdeaSelection> ideaSelections = new HashSet<>();

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Idea> ideas = new HashSet<>();

    public Competition() {
    }

    public Competition(String name, String description, Date startDate, Date endDate, int amountOfWinners) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amountOfWinners = amountOfWinners;
    }

    public void addIdeaSelection(IdeaSelection ideaSelection){
        this.ideaSelections.add(ideaSelection);
        ideaSelection.setCompetition(this);
    }

    public void removeIdeaSelection(IdeaSelection ideaSelection){
        this.ideaSelections.remove(ideaSelection);
        ideaSelection.setCompetition(null);
    }

    public void addIdea(Idea idea){
        this.ideas.add(idea);
        idea.setCompetition(this);
    }

    public void removeIdea(Idea idea){
        this.ideas.remove(idea);
        idea.setCompetition(null);
    }

    @Override
    public String toString() {
        return "Competition{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Competition comp = (Competition) o;
        return Objects.equals(id, comp.id);
    }
}