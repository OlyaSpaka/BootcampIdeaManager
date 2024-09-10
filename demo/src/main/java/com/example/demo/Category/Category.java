package com.example.demo.Category;

import com.example.demo.IdeaPackage.Idea;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
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

    public Set<Idea> getIdeas() {
        return ideas;
    }

    public void setIdeas(Set<Idea> ideas) {
        this.ideas = ideas;
    }
}
