package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
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
    @JsonBackReference
    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<IdeaSelection> ideaSelections = new HashSet<>();
    @JsonBackReference
    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Idea> ideas = new HashSet<>();

    public Competition() {
    }

    public Competition(String name, String description, Date startDate, Date endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
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
    public Set<Idea> getIdeas() {
        return ideas;
    }

    public void setIdeas(Set<Idea> ideas) {
        this.ideas = ideas;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Set<IdeaSelection> getIdeaSelections() {
        return ideaSelections;
    }

    public void setIdeaSelections(Set<IdeaSelection> ideaSelections) {
        this.ideaSelections = ideaSelections;
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
}