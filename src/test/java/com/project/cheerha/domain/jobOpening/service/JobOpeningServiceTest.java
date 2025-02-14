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

    /**
     * 테스트를 시작하기 전에 실행되는 설정 메서드입니다.
     * 데이터베이스의 기존 데이터를 삭제하고, 더미 데이터를 삽입합니다.
     */
    @BeforeEach
    @Transactional
    public void setup() {
//        jobOpeningRepository.deleteAll();
//        jobOpeningKeywordRepository.deleteAll();
//        keywordRepository.deleteAll();
        log.info("더미 데이터 설정을 시작합니다...");
//        addDummyData();
    }

    /**
     * 더미 채용 공고 데이터를 생성하여 데이터베이스에 삽입하는 메서드입니다.
     * 여러 개의 채용 공고를 생성하여 테스트에 필요한 데이터를 준비합니다.
     */
    private void addDummyData() {

        for (int i = 1; i <= 5; i++) {
            List<String> requiredSkillList = List.of("java", "spring", "mysql");

            log.info("채용 공고 " + i + "의 더미 데이터를 생성합니다...");

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

    /**
     * 채용 공고 데이터를 생성하고 키워드를 설정하여 데이터베이스에 저장하는 메서드입니다.
     *
     * 주어진 데이터를 기반으로 채용 공고와 관련된 키워드 목록을 생성하여 연결하고,
     * 이를 데이터베이스에 저장합니다.
     *
     * @param title 채용 공고 제목
     * @param company 회사명
     * @param location 근무지
     * @param salary 연봉
     * @param employmentType 고용 형태
     * @param educationLevel 교육 수준
     * @param jobOpeningUrl 채용 공고 URL
     * @param minExperienceYears 최소 경력 연수
     * @param maxExperienceYears 최대 경력 연수
     * @param position 포지션 (직무명)
     * @param hiringStartAt 채용 시작일
     * @param hiringEndAt 채용 마감일
     * @param requiredSkillList 채용 공고 키워드 목록
     */
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
        log.info("타이틀이 '{}'인 채용 공고 엔티티를 생성합니다.", title);

        List<JobOpeningKeyword> jobOpeningKeywords = new ArrayList<>();
        for (String skill : requiredSkillList) {
            Keyword keyword = getOrCreateKeyword(skill);
            JobOpeningKeyword jobOpeningKeyword = JobOpeningKeyword.toEntity(null, keyword);
            jobOpeningKeywords.add(jobOpeningKeyword);
            log.info("기술 스택으로 채용 공고 키워드 생성: {}", skill);
        }

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
        ReflectionTestUtils.setField(jobOpening, "jobOpeningKeywordList", jobOpeningKeywords);
        jobOpening = jobOpeningRepository.save(jobOpening);
        log.info("채용 공고 '{}'의 채용 공고 엔티티를 저장했습니다. ID: {}", title, jobOpening.getId());
        saveJobOpeningKeywordsWithJobOpening(jobOpening, jobOpeningKeywords);
    }

    /**
     * 채용 공고 생성 DTO를 엔티티로 변환하는 메서드입니다.
     *
     * DTO에 포함된 채용 공고 정보를 엔티티 객체에 매핑하여 반환합니다.
     *
     * @param dto 채용 공고 생성 요청 DTO
     * @param keywords 채용 공고에 대한 키워드 리스트
     * @return 변환된 JobOpening 엔티티 객체
     */
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
        ReflectionTestUtils.setField(jobOpening, "jobOpeningKeywordList", keywords);
        return jobOpening;
    }

    /**
     * 채용 공고와 연결된 채용 공고 키워드를 저장하는 메서드입니다.
     *
     * 채용 공고와 연결된 키워드들을 `jobOpeningKeywordRepository`를 통해 저장합니다.
     *
     * @param jobOpening 저장할 채용 공고 엔티티
     * @param jobOpeningKeywords 저장할 채용 공고 키워드 리스트
     */
    private void saveJobOpeningKeywordsWithJobOpening(JobOpening jobOpening, List<JobOpeningKeyword> jobOpeningKeywords) {
        log.info("채용 공고 엔티티 ID: {}와 연결된 채용 공고 키워드를 저장 중...", jobOpening.getId());
        for (JobOpeningKeyword keyword : jobOpeningKeywords) {
            ReflectionTestUtils.setField(keyword, "jobOpening", jobOpening);
        }
        jobOpeningKeywordRepository.saveAll(jobOpeningKeywords);
        log.info("{}개의 채용 공고 키워드가 채용 공고 엔티티에 저장되었습니다.", jobOpeningKeywords.size());
    }

    /**
     * 주어진 키워드 이름으로 키워드를 조회하거나, 없으면 새로 생성하는 메서드입니다.
     *
     * 주어진 키워드 이름이 데이터베이스에 존재하면 해당 키워드를 반환하고, 없으면 새로 생성하여 저장합니다.
     *
     * @param keywordName 조회하거나 생성할 키워드의 이름
     * @return 조회된 또는 새로 생성된 Keyword 객체
     */
    private Keyword getOrCreateKeyword(String keywordName) {
        Keyword keyword = keywordRepository.findByName(keywordName)
                .orElseGet(() -> keywordRepository.save(Keyword.toEntity(keywordName)));
        log.info("키워드 '{}'를 조회하거나 새로 생성했습니다.", keywordName);
        return keyword;
    }

    /**
     * 채용 공고 더미 데이터 생성 테스트 메서드입니다.
     *
     * 이 테스트는 더미 데이터가 제대로 삽입되었는지, 그리고 채용 공고와 관련된 키워드가 제대로 연결되어 있는지 검증합니다.
     */
    @Test
    @DisplayName("채용 공고 더미 데이터 생성 테스트")
    public void testAddDummyData() {
        log.info("더미 데이터 삽입 테스트를 시작합니다...");
        List<JobOpening> jobOpeningList = jobOpeningRepository.findAll();
        assertThat(jobOpeningList.size()).isEqualTo(5);
        List<JobOpeningKeyword> jobOpeningKeywords = jobOpeningKeywordRepository.findAll();
        assertThat(jobOpeningKeywords.size()).isGreaterThan(0);
    }
}
