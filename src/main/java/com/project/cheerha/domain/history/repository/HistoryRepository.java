package com.project.cheerha.domain.history.repository;

import com.project.cheerha.domain.history.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

    // TODO: 현재는 전체 조회인데 상위 몇개까지 조회하도록 정하는 것도 좋을 것 같습니다!
    List<History> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}
