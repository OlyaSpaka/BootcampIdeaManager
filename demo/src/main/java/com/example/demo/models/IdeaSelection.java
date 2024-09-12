package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "Idea_Selection")
public class IdeaSelection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "Idea_id")
    private Idea idea;
    @ManyToOne
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @Column(nullable = false)
    private Date date;

    public IdeaSelection() {
    }
    public IdeaSelection(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdeaSelection ideaSelection = (IdeaSelection) o;
        return Objects.equals(id, ideaSelection.id);
    }
}
