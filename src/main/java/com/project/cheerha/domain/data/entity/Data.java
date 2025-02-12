package com.project.cheerha.domain.data.entity;

import com.project.cheerha.domain.keyword.entity.DataKeyword;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private LocalDate hiringStartPeriod;

    private LocalDate hiringEndPeriod;

    @Column(length = 50, nullable = false)
    private String education;

    // todo tinyint일 때 뭘 써야 좋을지 추후 수정 예정
    private int career;

    @Column(length = 255, nullable = false)
    private String position;  // 포지션 (직무명 예시: SW개발)

    private int count;

    public void upCnt(){
        this.count++;
    }

    @OneToMany(mappedBy = "data", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DataKeyword> dataKeywordList = new ArrayList<>();

    // 자격 요건 키워드 리스트 반환
    public List<String> getRequiredSkillList() {
        List<String> skills = new ArrayList<>();
        for (DataKeyword dataKeyword : dataKeywordList) {
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
