package com.example.demo.VoteTypePackage;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class VoteType {
    @Id
    private Long id;
    private String name;
    private String points;

    public VoteType(Long id, String name, String points) {
        this.id = id;
        this.name = name;
        this.points = points;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
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
                ", points='" + points + '\'' +
                '}';
    }
}
