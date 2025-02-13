package com.project.cheerha.domain.bookmark.dto;

import com.project.cheerha.domain.bookmark.entity.Bookmark;
import com.project.cheerha.domain.jobOpening.entity.JobOpening;

import java.time.ZonedDateTime;
import java.util.List;

public record ReadBookmarkResponseDto(
        String company,       // 회사명
        ZonedDateTime hiringStartAt,  // 채용시작일
        ZonedDateTime hiringEndAt,   // 치용마감일
        String position,      // 포지션 (직무명)
        List<String> requiredSkillList // 자격 요건 (기술 키워드 리스트)
) {
    public static ReadBookmarkResponseDto toDto(Bookmark bookmark) {
        JobOpening jopOpening = bookmark.getJobOpening();
        return new ReadBookmarkResponseDto(
                jopOpening.getCompany(),           // 회사명
                jopOpening.getHiringStartAt(),      // 채용시작일
                jopOpening.getHiringEndAt(),      // 채용마감일
                jopOpening.getPosition(),          // 포지션 (직무명)
                jopOpening.getRequiredSkillList()     // 자격 요건 (기술 키워드 리스트)
        );
    }
}
