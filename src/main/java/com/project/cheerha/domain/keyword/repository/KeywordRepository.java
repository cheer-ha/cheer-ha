package com.project.cheerha.domain.keyword.repository;

import com.project.cheerha.domain.keyword.entity.Keyword;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Optional<Keyword> findByName(String name);

    List<Keyword> findByNameContaining(String searchTerm);
}
