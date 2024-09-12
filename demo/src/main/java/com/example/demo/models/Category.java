package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<Idea> ideas = new HashSet<>();

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }
    public void addIdea(Idea idea){
        this.ideas.add(idea);
        idea.getCategories().add(this);
    }

    public void removeIdea(Idea idea){
        this.ideas.remove(idea);
        idea.getCategories().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category cat = (Category) o;
        return Objects.equals(id, cat.id);
    }
}
