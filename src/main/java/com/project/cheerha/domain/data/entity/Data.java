package com.project.cheerha.domain.data.entity;

import com.project.cheerha.domain.keyword.entity.DataKeyword;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "data")
public class Data {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // todo 가독성을 위해 255가 기본값이지만 입력 완료, 추후 데이터 수집 후 값 수정 예정
    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 255, nullable = false)
    private String company;

    @Column(length = 255, nullable = false)
    private String location;

    private int salary;

    @Column(length = 20, nullable = false)
    private String jobType;

    @Column(length = 255, nullable = false)
    private String url;

    // todo LocalDateTime으로 바뀔 수 있음
    private LocalDateTime hiringStartPeriod;

    private LocalDateTime hiringEndPeriod;

    @Column(length = 50, nullable = false)
    private String education;

    // todo tinyint일 때 뭘 써야 좋을지 추후 수정 예정
    private Integer career;

    @Column(length = 255, nullable = false)
    private String position;  // 포지션 (직무명 예시: SW개발)

    // Data와 Keyword를 연결하는 DataKeyword 중간 테이블을 사용하여 자격 요건 관리
    @OneToMany(mappedBy = "data", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DataKeyword> dataKeywords = new ArrayList<>();  // 자격 요건(키워드 리스트)

    // 자격 요건 키워드 리스트 반환
    public List<String> getRequiredSkills() {
        List<String> skills = new ArrayList<>();
        for (DataKeyword dataKeyword : dataKeywords) {
            skills.add(dataKeyword.getKeyword().getName());
        }
        return skills;
    }
}
