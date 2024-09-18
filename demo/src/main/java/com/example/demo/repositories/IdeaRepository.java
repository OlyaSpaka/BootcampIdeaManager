package com.example.demo.repositories;

import com.example.demo.models.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, Integer> {
    List<Idea> findByUserId(Integer userId);

    @Query("""
                        SELECT i FROM Idea i join fetch i.categories c WHERE
                                LOWER(i.title) LIKE %:search% OR
                                LOWER(i.user.username) LIKE %:search% OR
                                LOWER(i.description) LIKE %:search% OR
                                LOWER(i.keyFeatures) LIKE %:search% OR
                                LOWER(c.name) LIKE %:search% OR
                                LOWER(COALESCE(i.referenceLinks, '')) LIKE %:search%
            """)
    List<Idea> searchIdeas(@Param("search") String search);
    List<Idea> findByCompetitionId(Integer competitionId);
}
