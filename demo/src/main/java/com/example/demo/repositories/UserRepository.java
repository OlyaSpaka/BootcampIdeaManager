package com.example.demo.repositories;

import com.example.demo.models.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @NotNull Optional<User> findById(@NotNull Integer id);

    @Query(value = """
                SELECT COUNT(*) from user_role where role_id = (select id from role where role.name = 'User')
            """, nativeQuery = true)
    Integer countUsers();

    @Query(value = """
                SELECT u.* FROM appuser u
                    JOIN user_role ur ON u.id = ur.user_id
                    WHERE ur.role_id = (select id from role where role.name = 'User')
            """, nativeQuery = true)
    List<User> findAllWithUserRole();
}
