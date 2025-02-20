package com.project.cheerha.domain.keyword.repository;

import com.project.cheerha.domain.keyword.entity.UserKeyword;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserKeywordRepository extends JpaRepository<UserKeyword, Long> {

    boolean existsByUserIdAndKeywordId(Long userId, Long keywordId);

    boolean existsByUserIdAndId(Long userId, Long userKeywordId);

    @Query("SELECT uk.keyword.id FROM UserKeyword uk WHERE uk.user.id = :userId")
    List<Long> findKeywordIdsByUserId(@Param("userId") Long userId);
}