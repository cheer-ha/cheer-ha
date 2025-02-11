package com.project.cheerha.domain.keyword.repository;

import com.project.cheerha.domain.keyword.entity.Keyword;
import com.project.cheerha.domain.keyword.entity.UserKeyword;
import com.project.cheerha.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserKeywordRepository extends JpaRepository<UserKeyword, Long> {
    boolean existsByUserAndKeyword (User user, Keyword keyword);
}