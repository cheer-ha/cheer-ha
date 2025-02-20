package com.project.cheerha.domain.jobopening.entity;

import com.project.cheerha.domain.jobopening.entity.EducationLevel;
import com.project.cheerha.domain.jobopening.entity.EmploymentType;
import com.project.cheerha.domain.keyword.entity.JobOpeningKeyword;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import jakarta.persistence.*;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "job_opening")
public class JobOpening {
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
    private EmploymentType employmentType;

    @Column(length = 50, nullable = false)
    private EducationLevel educationLevel;

    @Column(length = 255, nullable = false)
    private String jobOpeningUrl;

    //Integer을 사용한 이유는 경력에 경력없는 신입(0년)일 경우도 있지만, 경력무관(null)인 경우도 있다.
    //이로 인해 int를 사용하면 null(경력무관)을 쓸 수 없기 때문에 Integer 사용
    @Column(nullable = true)
    private Integer minExperienceYears;

    @Column(nullable = true)
    private Integer maxExperienceYears;

    @Column(length = 255, nullable = false)
    private String position;  // 포지션 (직무명 예시: SW개발)

    //해외 사이트 적용을 생각해서 ZonedDateTime 사용
    private ZonedDateTime hiringStartAt;
    private ZonedDateTime hiringEndAt;

    private int viewCount;

    public void increaseViewCount() {
        this.viewCount++;
    }

    @OneToMany(mappedBy = "jobOpening", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobOpeningKeyword> jobOpeningKeywordList = new ArrayList<>();

    // 자격 요건 키워드 리스트 반환
    public List<String> getRequiredSkillList() {
        List<String> skillList = new ArrayList<>();
        for (JobOpeningKeyword jobOpeningKeyword : jobOpeningKeywordList) {
            skillList.add(jobOpeningKeyword.getKeyword().getName());
        }
        return skillList;
    }
}
