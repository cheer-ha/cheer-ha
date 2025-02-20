package com.project.cheerha.domain.jobopening.repository;

import com.project.cheerha.domain.jobopening.entity.JobOpening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobOpeningRepository extends JpaRepository<JobOpening, Long>, JobOpeningRepositoryQuery {

    @Modifying
    @Query("UPDATE JobOpening b SET b.viewCount = b.viewCount + :viewCount WHERE b.id = :id")
    void updateViewCount(@Param("id")Long id, @Param ("viewCount") Long viewCount);
}