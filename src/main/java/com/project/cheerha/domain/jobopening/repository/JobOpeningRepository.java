package com.project.cheerha.domain.jobopening.repository;

import com.project.cheerha.domain.jobopening.entity.JobOpening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobOpeningRepository extends JpaRepository<JobOpening, Long>, JobOpeningRepositoryQuery {

    /**
     * 채용공고 테이블에 있는 viewCount 컬럼에 집계테이블 viewcount 컬럼 값 만큼 더해서 값을
     * 업데이트 하는 메서드입니다.
     * @param id 집계테이블의 특정 채용공고 id
     * @param viewCount  집계테이블의 특정 viewCount
     */
    @Modifying
    @Query("UPDATE JobOpening b SET b.viewCount = b.viewCount + :viewCount WHERE b.id = :id")
    void updateViewCount(@Param("id")Long id, @Param ("viewCount") Long viewCount);
}