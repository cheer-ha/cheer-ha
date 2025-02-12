package com.project.cheerha.domain.bookmark.dto;

import com.project.cheerha.domain.bookmark.entity.Bookmark;
import com.project.cheerha.domain.data.entity.Data;

import java.util.List;
import java.util.Set;

public record ReadBookmarkResponseDto(
        String company,       // 회사명
        String hiringPeriod,  // 채용 기간
        String position,      // 포지션 (직무명)
        List<String> requiredSkillList // 자격 요건 (기술 키워드 리스트)
) {
    public static ReadBookmarkResponseDto toDto(Bookmark bookmark) {
        Data data = bookmark.getData();
        return new ReadBookmarkResponseDto(
                data.getCompany(),           // 회사명
                data.getHiringPeriod(),      // 채용 기간
                data.getPosition(),          // 포지션 (직무명)
                data.getRequiredSkillList()     // 자격 요건 (기술 키워드 리스트)
        );
    }
}
