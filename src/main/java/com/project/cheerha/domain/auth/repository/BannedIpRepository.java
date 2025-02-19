package com.project.cheerha.domain.auth.repository;

import com.project.cheerha.domain.auth.entity.BannedIp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannedIpRepository extends JpaRepository<BannedIp, Long> {
    boolean existsByIp(String ip);
}
