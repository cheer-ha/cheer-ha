package com.project.cheerha.domain.notice.repository;

import com.project.cheerha.domain.notice.entity.Mapping;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MappingRepository extends JpaRepository<Mapping, Long> {

    boolean existsByEmailAndJobOpeningUrl(
        String email,
        String jobOpeningUrl
    );

    List<Mapping> findByIsEmailSentFalse();
}