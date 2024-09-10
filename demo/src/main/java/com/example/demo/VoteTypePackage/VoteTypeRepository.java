package com.example.demo.VoteTypePackage;

import com.example.demo.VotePackage.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteTypeRepository extends JpaRepository<VoteType,Long> {
}
