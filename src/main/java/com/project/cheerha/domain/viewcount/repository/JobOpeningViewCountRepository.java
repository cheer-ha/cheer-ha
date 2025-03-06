package com.project.cheerha.domain.viewcount.repository;

import com.project.cheerha.domain.viewcount.entity.JobOpeningViewCount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobOpeningViewCountRepository extends JpaRepository<JobOpeningViewCount, Long> {

//    /**
//     * 비관적 락을 사용하여 ViewCount 엔티티를 조회하는 메서드입니다.
//     * 특정 JobOpening ID에 해당하는 JobOpeningViewCount 엔티티를 조회합니다.
//     * @param id 조회할 JobOpening의 ID
//     * @return 비관적 락이 걸린 JobOpeningViewCount 엔티티(Optional 형태)
//     */
//    @Query("SELECT b FROM JobOpeningViewCount b WHERE b.jobOpening.id = :id")
//    Optional<JobOpeningViewCount> findWithLockByJobOpeningId(@Param("id") Long id);

    /**
     * 특정 JobOpening ID에 해당하는 ViewCount 엔티티를 조회하는 메서드입니다.
     * 비관적 락을 적용하지 않은 단순 조회 메서드
     * @param id 조회를 위한 jobOpening의 ID
     * @return 락 없이 일반적으로 조회된 JobOpeningViewCount 엔티티(Optional 형태)
     */
    @Query("SELECT b FROM JobOpeningViewCount b WHERE b.jobOpening.id = :id")
    Optional<JobOpeningViewCount> findByJobOpeningId(@Param("id") Long id);

    /**
     * JobOpeningViewCount 테이블에서 고유한 JobOpening ID 목록을 조회하는 메서드입니다.
     * 집계 테이블인 JobOpeningViewCount에 저장된 값 중 viewcount값이 1이상인 JobOpening의 ID를 조회합니다.
     * DISTINCT 키워드를 사용하여 중복을 제거한 ID 목록을 반환합니다.
     * 이는 조회수 동기화 스케줄러(syncViewCounts)에서 사용됩니다.
     * @return JobOpeningViewCount에 저장된 고유한 JobOpening ID 목록(List<Long>)
     */
    @Query("SELECT DISTINCT b.jobOpening.id FROM JobOpeningViewCount b WHERE b.viewCount > 0")
    List<Long> findDistinctViewedJobOpeningIdList();

    /**
     * 특정 JobOpening ID에 해당하는 ViewCount 값을 조회하여 조회수 동기화에 사용되는 메서드입니다.
     * JobOpeningViewCount 테이블에서 특정 JobOpening ID에 연결된 조회수(viewCount)를 반환합니다.
     * 조회수 동기화 스케줄러(syncViewCounts)에서 JobOpening 테이블의 viewCount 값을 업데이트 하기 위해 사용됩니다.
     * @param id 조회할 JobOpening의 ID
     * @return JobOpeningViewCount 테이블에서 해당 ID에 대한 viewCount 값(Long)
     */
    @Query("SELECT b.viewCount FROM JobOpeningViewCount b WHERE b.jobOpening.id = :id")
    Long getViewCountByJobOpeningId(@Param("id") Long id);

    /**
     * 특정 JobOpening ID에 해당하는 ViewCount를 초기화(0으로 설정)하는 메서드입니다.
     * 조회수 동기화 스케줄러(syncViewCount)에서 사용됩니다.
     * 스케줄러가 실행될 때, JobOpening 테이블로 조회수를 동기화한 후 초기화 작업을 수행합니다.
     * @param jobOpeningId 초기화할 JobOpening의 ID
     */
    @Modifying
    @Query("UPDATE JobOpeningViewCount v SET v.viewCount = 0 WHERE v.jobOpening.id = :jobOpeningId")
    void resetViewCountByJobOpeningId(@Param("jobOpeningId") Long jobOpeningId);
}
