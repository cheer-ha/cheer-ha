package com.project.cheerha.domain.jobOpening.repository;

import com.project.cheerha.domain.jobOpening.entity.JobOpening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobOpeningRepository extends JpaRepository<JobOpening, Long>, JobOpeningRepositoryQuery {

//    //비관적 락
//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @Query("select b from JobOpening b where b.id = :id")
//    Optional<JobOpening> findByForUpdateViewCount(Long id);

    //낙관적 락_ 특정 컬럼만 변경하기 위하여 JPQL문 추가, version을 사용하여 동시성관리
    @Modifying
    @Query("UPDATE JobOpening b SET b.viewCount = b.viewCount + 1, b.version = b.version + 1 WHERE b.id = :id AND b.version = :version")
    int updateViewCountWithOptimisticLock(@Param("id") Long id, @Param("version") Long version);


}
