package com.project.cheerha.domain.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private LocalDate hiringEndPeriod;

    @Column(length = 50, nullable = false)
    private String education;

    // todo tinyint일 때 뭘 써야 좋을지 추후 수정 예정
    private int career;

    private int count;

    public void upCnt(){
        this.count++;
    }
}
