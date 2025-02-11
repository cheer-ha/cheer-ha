package com.project.cheerha.domain.data.entity;

import com.project.cheerha.domain.keyword.entity.DataKeyword;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
    private LocalDate hiringStartPeriod;

    private LocalDate hiringEndPeriod;

    @Column(length = 50, nullable = false)
    private String education;

    // todo tinyint일 때 뭘 써야 좋을지 추후 수정 예정
    private int career;

    @Column(length = 255, nullable = false)
    private String position;  // 포지션 (직무명 예시: SW개발)

    private int count;

    // Data와 Keyword를 연결하는 DataKeyword 중간 테이블을 사용하여 자격 요건 관리
    @OneToMany(mappedBy = "data", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<DataKeyword> dataKeywords = new HashSet<>();  // 자격 요건(키워드 리스트)

    public Data(String title, String company, String location, Integer salary, String jobType, String url,
                LocalDate hiringStartPeriod, LocalDate hiringEndPeriod, String education, Integer career,
                String position, Set<DataKeyword> dataKeywords) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.salary = salary;
        this.jobType = jobType;
        this.url = url;
        this.hiringStartPeriod = hiringStartPeriod;
        this.hiringEndPeriod = hiringEndPeriod;
        this.education = education;
        this.career = career;
        this.position = position;
        this.dataKeywords = dataKeywords;
    }

    // 자격 요건 키워드 리스트 반환
    public Set<String> getRequiredSkills() {
        Set<String> skills = new HashSet<>();
        for (DataKeyword dataKeyword : dataKeywords) {
            skills.add(dataKeyword.getKeyword().getName());
        }
        return skills;
    }

    // 채용 기간을 문자열로 반환하는 메서드
    public String getHiringPeriod() {
        if (hiringStartPeriod != null && hiringEndPeriod != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return hiringStartPeriod.format(formatter) + " ~ " + hiringEndPeriod.format(formatter);
        } else {
            return "채용 기간 정보 없음";
        }
    }
}
