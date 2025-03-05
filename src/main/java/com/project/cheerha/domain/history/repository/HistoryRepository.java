package com.project.cheerha.domain.history.repository;

import com.project.cheerha.domain.history.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT h.name FROM History h WHERE h.user.id = :userId")
    Set<String> findNamesByUserId(@Param("userId") Long userId);

    boolean existsByUserIdAndName(Long userId, String name);
}
