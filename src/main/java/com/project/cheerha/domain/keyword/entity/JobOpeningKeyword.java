package com.project.cheerha.domain.keyword.entity;

import com.project.cheerha.domain.jobOpening.entity.JobOpening;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "job_opening_keyword", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"keyword_id", "job_opening_id"})}
)
public class JobOpeningKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;

    @ManyToOne
    @JoinColumn(name = "job_opening_id")
    private JobOpening jobOpening;

    public static DataKeyword toEntity(Data data, Keyword keyword) {
        DataKeyword dataKeyword = new DataKeyword();
        dataKeyword.keyword = keyword;
        dataKeyword.data = data;
        return dataKeyword;
    }
}
