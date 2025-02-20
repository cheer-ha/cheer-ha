package com.project.cheerha.domain.jobopening.repository;

import com.project.cheerha.domain.jobopening.entity.JobOpening;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface JobOpeningRepository extends JpaRepository<JobOpening, Long>, JobOpeningRepositoryQuery {

    //비관적 락
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from JobOpening b where b.id = :id")
    Optional<JobOpening> findByForUpdateViewCount(Long id);

}
