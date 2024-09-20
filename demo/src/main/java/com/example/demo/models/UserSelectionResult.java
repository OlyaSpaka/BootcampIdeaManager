package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_selection_results")
public class UserSelectionResult implements Serializable {

    @Getter
    @Setter
    @NoArgsConstructor
    @Embeddable
    public static class UserSelectionResultId implements Serializable {

        @Column(name = "User_id")
        private int userId;

        @Column(name = "Idea_Selection_id")
        private int ideaSelectionId;

        public UserSelectionResultId(int userId, int ideaSelectionId) {
            this.userId = userId;
            this.ideaSelectionId = ideaSelectionId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserSelectionResultId that = (UserSelectionResultId) o;
            return userId == that.userId && ideaSelectionId == that.ideaSelectionId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, ideaSelectionId);
        }
    }

    @EmbeddedId
    private UserSelectionResultId id;

    public UserSelectionResult(UserSelectionResultId id) {
        this.id = id;
    }
}