package com.project.cheerha.domain.viewcount.repository;

import com.project.cheerha.domain.viewcount.entity.JobOpeningViewCount;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobOpeningViewCountRepository extends JpaRepository<JobOpeningViewCount, Long> {

    //비관적 락
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM JobOpeningViewCount b WHERE b.jobOpening.id = :id")
    Optional<JobOpeningViewCount> findByForUpdateViewCount(@Param("id") Long id);

    /**
     * 조회수 관리 테이블(JobOpeningViewCount)에서 viewCount값을 조회하는 메서드로 채용공고 테이블(jobOpening)에 있는
     * viewCount값과 동기화를 할 때 사용합니다.
     * @return JobOpeningViewCount에서 채용공고 id와 동일한 id를 가진 viewCount값
     */
    @Query("SELECT DISTINCT b.jobOpening.id FROM JobOpeningViewCount b")
    List<Long> findAllJobOpeningId();

    @Query("SELECT b.viewCount FROM JobOpeningViewCount b WHERE b.jobOpening.id = :id")
    Long findViewCountByJobOpeningId(@Param("id") Long id);

    @Modifying
    @Query("UPDATE JobOpeningViewCount v SET v.viewCount = 0 WHERE v.jobOpening.id = :jobOpeningId")
    void resetViewCount(@Param("jobOpeningId") Long jobOpeningId);


}
