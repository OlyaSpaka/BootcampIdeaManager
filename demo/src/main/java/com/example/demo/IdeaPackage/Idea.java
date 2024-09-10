package com.example.demo.IdeaPackage;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Date;
@Entity
@Table
public class Idea {

    private long id;
    private long competition_id;
    private long  user_id;
    private String title;
    private String description;
    private String key_features;
    private String references;
    private Date created_at;
    private String pictures; // String type for now

    public Idea() {
    }

    public Idea(long id, long competition_id, long user_id, String description, String title, String key_features, String references, Date created_at, String pictures) {
        this.id = id;
        this.competition_id = competition_id;
        this.user_id = user_id;
        this.description = description;
        this.title = title;
        this.key_features = key_features;
        this.references = references;
        this.created_at = created_at;
        this.pictures = pictures;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCompetition_id() {
        return competition_id;
    }

    public void setCompetition_id(long competition_id) {
        this.competition_id = competition_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey_features() {
        return key_features;
    }

    public void setKey_features(String key_features) {
        this.key_features = key_features;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    @Override
    public String toString() {
        return "Idea{" +
                "id=" + id +
                ", competition_id=" + competition_id +
                ", user_id=" + user_id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", key_features='" + key_features + '\'' +
                ", references='" + references + '\'' +
                ", created_at=" + created_at +
                ", pictures='" + pictures + '\'' +
                '}';
    }
}
