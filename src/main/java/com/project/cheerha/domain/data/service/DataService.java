package com.project.cheerha.domain.data.service;

import com.project.cheerha.domain.data.dto.CreateDataRequestDto;
import com.project.cheerha.domain.data.entity.Data;
import com.project.cheerha.domain.data.repository.DataRepository;
import com.project.cheerha.domain.keyword.entity.DataKeyword;
import com.project.cheerha.domain.keyword.entity.Keyword;
import com.project.cheerha.domain.keyword.repository.DataKeywordRepository;
import com.project.cheerha.domain.keyword.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DataService {

    private final DataRepository dataRepository;
    private final KeywordRepository keywordRepository;
    private final DataKeywordRepository dataKeywordRepository;

    public void addDummyData() {
        // 키워드 예시 생성
        Keyword keyword1 = keywordRepository.save(new Keyword("Java"));
        Keyword keyword2 = keywordRepository.save(new Keyword("Spring"));
        Keyword keyword3 = keywordRepository.save(new Keyword("MySQL"));
        Keyword keyword4 = keywordRepository.save(new Keyword("Python"));
        Keyword keyword5 = keywordRepository.save(new Keyword("Django"));
        Keyword keyword6 = keywordRepository.save(new Keyword("PostgreSQL"));

        // 첫 번째 Data 엔티티 생성 (네이버 - Java Developer)
        Set<String> requiredSkillsJob1 = new HashSet<>(Arrays.asList("Java", "Spring", "MySQL"));
        CreateDataRequestDto dataDto1 = new CreateDataRequestDto(
                "Java Developer",
                "네이버",
                "Seoul",
                5000000,
                "Full-time",
                "https://www.naver.com/jobs/java-developer",
                LocalDate.parse("2025-03-01"),
                LocalDate.parse("2025-04-15"),
                "Bachelor's Degree",
                2,
                "SW Developer",
                requiredSkillsJob1 // 기술 스택 추가
        );

        // 첫 번째 Data 엔티티 생성
        Data data1 = CreateDataRequestDto.fromDto(dataDto1, null); // 필드에 Keyword 연결을 추가하지 않음

        // 두 번째 Data 엔티티 생성 (네이버 - Python Developer)
        Set<String> requiredSkillsJob2 = new HashSet<>(Arrays.asList("Python", "Django", "PostgreSQL"));
        CreateDataRequestDto dataDto2 = new CreateDataRequestDto(
                "Python Developer",
                "네이버",
                "Seoul",
                5000000,
                "Full-time",
                "https://www.naver.com/jobs/python-developer",
                LocalDate.parse("2025-03-01"),
                LocalDate.parse("2025-04-15"),
                "Bachelor's Degree",
                2,
                "SW Developer",
                requiredSkillsJob2 // 기술 스택 추가
        );

        // 두 번째 Data 엔티티 생성
        Data data2 = CreateDataRequestDto.fromDto(dataDto2, null); // 필드에 Keyword 연결을 추가하지 않음

        // Data를 먼저 저장하여 DB에 영속화
        dataRepository.save(data1);  // Data 저장
        dataRepository.save(data2);  // Data 저장

        // DataKeyword들을 연결하여 저장
        Set<DataKeyword> dataKeywordsForJob1 = new HashSet<>(Arrays.asList(
                new DataKeyword(data1, keyword1), // data1과 keyword1 연결
                new DataKeyword(data1, keyword2), // data1과 keyword2 연결
                new DataKeyword(data1, keyword3)  // data1과 keyword3 연결
        ));

        Set<DataKeyword> dataKeywordsForJob2 = new HashSet<>(Arrays.asList(
                new DataKeyword(data2, keyword4), // data2와 keyword4 연결
                new DataKeyword(data2, keyword5), // data2와 keyword5 연결
                new DataKeyword(data2, keyword6)  // data2와 keyword6 연결
        ));

        // DataKeyword 저장 (Data와 연결)
        dataKeywordRepository.saveAll(dataKeywordsForJob1);
        dataKeywordRepository.saveAll(dataKeywordsForJob2);
    }
}
