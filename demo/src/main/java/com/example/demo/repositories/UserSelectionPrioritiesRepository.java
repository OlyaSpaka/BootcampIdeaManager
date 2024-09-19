package com.example.demo.repositories;

import com.example.demo.models.UserSelectionPriorities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSelectionPrioritiesRepository extends JpaRepository<UserSelectionPriorities, Integer> {

    @Query("SELECT COUNT(DISTINCT usp.user.id) FROM UserSelectionPriorities usp")
    int countUsersWithPriorities();
}