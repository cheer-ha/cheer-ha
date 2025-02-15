package com.project.cheerha.domain.keyword.repository;

import com.project.cheerha.domain.keyword.entity.JobOpeningKeyword;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobOpeningKeywordRepository extends JpaRepository<JobOpeningKeyword, Long> {

    // [수정 후 Query]
    @Query(
        "SELECT DISTINCT jok.jobOpening.id "
            + "FROM JobOpeningKeyword jok "
            + "WHERE jok.keyword.id IN :keywordIdList"
    )
    List<Long> findJobOpeningIdListByKeywordId(
        @Param("keywordIdList") List<Long> keywordIdList
    );
}
