package com.project.cheerha.domain.jobopening.repository;

import com.project.cheerha.domain.jobopening.entity.JobOpening;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobOpeningRepository extends JpaRepository<JobOpening, Long>, JobOpeningRepositoryQuery {

}
