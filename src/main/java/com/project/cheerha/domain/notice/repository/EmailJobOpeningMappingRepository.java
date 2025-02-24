package com.project.cheerha.domain.notice.repository;

import com.project.cheerha.domain.notice.entity.EmailJobOpeningMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailJobOpeningMappingRepository extends JpaRepository<EmailJobOpeningMapping, Long> {
}