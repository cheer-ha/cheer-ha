package com.project.cheerha.domain.auth.repository;

import com.project.cheerha.domain.auth.entity.BannedEmail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannedEmailRepository extends JpaRepository<BannedEmail, Long> {
}
