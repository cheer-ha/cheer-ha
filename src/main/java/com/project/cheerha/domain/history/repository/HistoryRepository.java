package com.project.cheerha.domain.history.repository;

import com.project.cheerha.domain.history.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {

}
