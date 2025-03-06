package com.project.cheerha.domain.searchhistory.repository;

import com.project.cheerha.domain.searchhistory.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    List<SearchHistory> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT h.name FROM SearchHistory h WHERE h.user.id = :userId")
    Set<String> findNamesByUserId(@Param("userId") Long userId);
}
