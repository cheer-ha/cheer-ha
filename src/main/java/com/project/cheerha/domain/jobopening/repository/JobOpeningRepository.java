package com.project.cheerha.domain.jobopening.repository;

import com.project.cheerha.domain.jobopening.entity.JobOpening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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

    /**
     * 채용공고와 관련된 키워드를 함께 조회하는 메서드입니다.
     *
     * 이 메서드는 `JobOpening` 엔티티와 관련된 `jobOpeningKeywordList`를 **LEFT JOIN FETCH**하여,
     * 채용공고 데이터와 해당 채용공고에 연결된 키워드 목록을 한 번의 쿼리로 조회합니다.
     *
     * LEFT JOIN FETCH를 사용하여 `jobOpeningKeywordList`를 즉시 로딩(eager loading)하도록 하여,
     * 연관된 키워드 데이터를 별도의 쿼리 없이 가져오도록 최적화합니다. 이렇게 하면,
     * N+1 문제를 방지할 수 있습니다.
     *
     * @return 채용공고와 관련된 키워드 목록을 포함한 `JobOpening` 엔티티 리스트
     */
    @Query("SELECT j FROM JobOpening j LEFT JOIN FETCH j.jobOpeningKeywordList")
    List<JobOpening> findAllWithJobOpeningKeywords();
}