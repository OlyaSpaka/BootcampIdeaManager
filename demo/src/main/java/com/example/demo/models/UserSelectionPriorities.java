package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "user_selection_priorities")
public class UserSelectionPriorities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "idea_selection_id")
    private IdeaSelection ideaSelection;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "submitted_at")
    private Timestamp submissionTime;

    public UserSelectionPriorities() {
    }

    @Override
    public String toString() {
        return "UserSelectionPriorities{" +
                "id=" + id +
                ", user=" + user +
                ", ideaSelection=" + ideaSelection +
                ", priority=" + priority +
                ", submittedAt=" + submissionTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSelectionPriorities userSelectionPriorities = (UserSelectionPriorities) o;
        return Objects.equals(id, userSelectionPriorities.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, ideaSelection, priority, submissionTime);
    }
}
