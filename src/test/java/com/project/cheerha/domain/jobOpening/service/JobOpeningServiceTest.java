package com.project.cheerha.domain.jobOpening.service;

import com.project.cheerha.domain.jobOpening.entity.JobOpening;
import com.project.cheerha.domain.jobOpening.repository.JobOpeningRepository;
import com.project.cheerha.domain.jobOpening.dto.request.CreateJobOpeningRequestDto;
import com.project.cheerha.domain.keyword.entity.JobOpeningKeyword;
import com.project.cheerha.domain.keyword.entity.Keyword;
import com.project.cheerha.domain.keyword.repository.JobOpeningKeywordRepository;
import com.project.cheerha.domain.keyword.repository.KeywordRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class JobOpeningServiceTest {

    @Autowired
    private JobOpeningRepository jobOpeningRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private JobOpeningKeywordRepository jobOpeningKeywordRepository;

    @BeforeEach
    @Transactional
    public void setup() {
        // 데이터베이스 초기화
        jobOpeningRepository.deleteAll();
        jobOpeningKeywordRepository.deleteAll();
        keywordRepository.deleteAll();

        // 더미 데이터를 삽입
        log.info("더미 데이터 설정을 시작합니다...");
        addDummyData();
    }

    private void addDummyData() {
        // 반복문을 통해 더미 데이터 생성 및 저장
        for (int i = 1; i <= 5; i++) {
            List<String> requiredSkillList = List.of("java", "spring", "mysql");

            log.info("잡 " + i + "의 더미 데이터를 생성합니다...");
            // 더미 데이터를 DTO를 사용하여 생성
            createAndSaveDataWithKeywords(
                    "Job " + i,
                    "Company " + i,
                    "Location " + i,
                    5000000,
                    "Full-time",
                    "Bachelor's Degree",
                    "https://www.example.com/jobs/job" + i,
                    2,
                    5,
                    "SW Developer",
                    ZonedDateTime.parse("2025-03-01T00:00:00Z"),
                    ZonedDateTime.parse("2025-04-15T00:00:00Z"),
                    requiredSkillList
            );
        }
    }

    private void createAndSaveDataWithKeywords(
            String title,
            String company,
            String location,
            Integer salary,
            String employmentType,
            String educationLevel,
            String jobOpeningUrl,
            Integer minExperienceYears,
            Integer maxExperienceYears,
            String position,
            ZonedDateTime hiringStartAt,
            ZonedDateTime hiringEndAt,
            List<String> requiredSkillList
    ) {
        log.info("타이틀이 '{}'인 JobOpening 엔티티를 생성합니다.", title);

        // 키워드 목록을 생성하고 연결
        List<JobOpeningKeyword> jobOpeningKeywords = new ArrayList<>();
        for (String skill : requiredSkillList) {
            Keyword keyword = getOrCreateKeyword(skill);
            JobOpeningKeyword jobOpeningKeyword = JobOpeningKeyword.toEntity(null, keyword);
            jobOpeningKeywords.add(jobOpeningKeyword);
            log.info("기술 스택으로 JobOpeningKeyword 생성: {}", skill);
        }

        // DTO로 JobOpening 생성
        CreateJobOpeningRequestDto jobOpeningRequestDto = new CreateJobOpeningRequestDto(
                title,
                company,
                location,
                salary,
                employmentType,
                educationLevel,
                jobOpeningUrl,
                minExperienceYears,
                maxExperienceYears,
                position,
                hiringStartAt,
                hiringEndAt,
                requiredSkillList
        );

        JobOpening jobOpening = mapDtoToEntity(jobOpeningRequestDto, jobOpeningKeywords);

        // `jobOpeningKeywordList` 연결
        ReflectionTestUtils.setField(jobOpening, "jobOpeningKeywordList", jobOpeningKeywords); // 연결된 키워드 리스트 설정

        // JobOpening 저장
        jobOpening = jobOpeningRepository.save(jobOpening);
        log.info("잡 '{}'의 JobOpening 엔티티를 저장했습니다. ID: {}", title, jobOpening.getId());

        // 연결된 JobOpeningKeyword 저장
        saveJobOpeningKeywordsWithJobOpening(jobOpening, jobOpeningKeywords);
    }

    private JobOpening mapDtoToEntity(CreateJobOpeningRequestDto dto, List<JobOpeningKeyword> keywords) {
        JobOpening jobOpening = new JobOpening();
        ReflectionTestUtils.setField(jobOpening, "title", dto.title());
        ReflectionTestUtils.setField(jobOpening, "company", dto.company());
        ReflectionTestUtils.setField(jobOpening, "location", dto.location());
        ReflectionTestUtils.setField(jobOpening, "salary", dto.salary());
        ReflectionTestUtils.setField(jobOpening, "employmentType", dto.employmentType());
        ReflectionTestUtils.setField(jobOpening, "jobOpeningUrl", dto.jobOpeningUrl());
        ReflectionTestUtils.setField(jobOpening, "educationLevel", dto.educationLevel());
        ReflectionTestUtils.setField(jobOpening, "minExperienceYears", dto.minExperienceYears());
        ReflectionTestUtils.setField(jobOpening, "maxExperienceYears", dto.maxExperienceYears());
        ReflectionTestUtils.setField(jobOpening, "position", dto.position());
        ReflectionTestUtils.setField(jobOpening, "hiringStartAt", dto.hiringStartAt());
        ReflectionTestUtils.setField(jobOpening, "hiringEndAt", dto.hiringEndAt());

        // 키워드 리스트 연결
        ReflectionTestUtils.setField(jobOpening, "jobOpeningKeywordList", keywords);

        return jobOpening;
    }

    private void saveJobOpeningKeywordsWithJobOpening(JobOpening jobOpening, List<JobOpeningKeyword> jobOpeningKeywords) {
        log.info("JobOpening 엔티티 ID: {}와 연결된 JobOpeningKeywords를 저장 중...", jobOpening.getId());
        for (JobOpeningKeyword keyword : jobOpeningKeywords) {
            ReflectionTestUtils.setField(keyword, "jobOpening", jobOpening);
        }
        jobOpeningKeywordRepository.saveAll(jobOpeningKeywords);
        log.info("{}개의 JobOpeningKeyword가 JobOpening 엔티티에 저장되었습니다.", jobOpeningKeywords.size());
    }

    private Keyword getOrCreateKeyword(String keywordName) {
        Keyword keyword = keywordRepository.findByName(keywordName)
                .orElseGet(() -> keywordRepository.save(Keyword.toEntity(keywordName)));
        log.info("키워드 '{}'를 조회하거나 새로 생성했습니다.", keywordName);
        return keyword;
    }

    @Test
    @DisplayName("채용공고 더미데이터 생성 테스트")
    public void testAddDummyData() {
        log.info("더미 데이터 삽입 테스트를 시작합니다...");

        // 데이터베이스에서 JobOpening 엔티티를 조회합니다.
        List<JobOpening> jobOpeningList = jobOpeningRepository.findAll();
        log.info("데이터베이스에서 {}개의 JobOpening 엔티티를 조회했습니다.", jobOpeningList.size());

        // 데이터가 존재하는지 확인합니다.
        assertThat(jobOpeningList).isNotEmpty();
        assertThat(jobOpeningList.size()).isGreaterThan(0);

        // 데이터베이스에서 Keyword 엔티티를 조회하여 적어도 1개의 키워드가 생성되었는지 확인합니다.
        List<Keyword> keywords = keywordRepository.findAll();
        log.info("데이터베이스에서 {}개의 Keyword 엔티티를 조회했습니다.", keywords.size());
        assertThat(keywords).isNotEmpty();
        assertThat(keywords.size()).isGreaterThan(0);

        // JobOpeningKeyword가 제대로 연결되어 저장되었는지 확인합니다.
        List<JobOpeningKeyword> jobOpeningKeywords = jobOpeningKeywordRepository.findAll();
        log.info("데이터베이스에서 {}개의 JobOpeningKeyword 엔티티를 조회했습니다.", jobOpeningKeywords.size());
        assertThat(jobOpeningKeywords).isNotEmpty();
        assertThat(jobOpeningKeywords.size()).isGreaterThan(0);

        // 첫 번째 JobOpening에 연결된 키워드들을 확인합니다.
        JobOpening firstJobOpening = jobOpeningList.get(0);
        List<JobOpeningKeyword> firstJobOpeningKeywords = jobOpeningKeywords.stream()
                .filter(keyword -> keyword.getJobOpening().getId().equals(firstJobOpening.getId()))
                .toList();
        log.info("첫 번째 JobOpening 엔티티 (ID: {})는 {}개의 JobOpeningKeyword가 연결되어 있습니다.", firstJobOpening.getId(), firstJobOpeningKeywords.size());
        assertThat(firstJobOpeningKeywords).isNotEmpty();
    }
}
