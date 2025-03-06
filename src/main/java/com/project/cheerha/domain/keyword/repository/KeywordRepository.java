package com.project.cheerha.domain.keyword.repository;

import com.project.cheerha.domain.keyword.entity.Keyword;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Long>, KeywordRepositoryQuery {
    List<Keyword> findByNameContaining(String searchTerm);
}
