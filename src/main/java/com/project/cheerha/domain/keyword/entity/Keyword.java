package com.project.cheerha.domain.keyword.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "keyword")
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    public static Keyword toEntity(Long id) {
        Keyword keyword = new Keyword();
        keyword.id = id;
        return keyword;
    }

    // 키워드 객체 목록에서 이름을 추출하는 메서드
    public static List<String> extractNameFromEntity(List<Keyword> keywordList) {
        return keywordList.stream()
            .map(Keyword::getName)
            .toList();
    }
}