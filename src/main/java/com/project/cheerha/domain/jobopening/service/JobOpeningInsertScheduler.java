package com.project.cheerha.domain.jobopening.service;

import com.project.cheerha.common.annotation.ScheduledDynamic;
import com.project.cheerha.domain.jobopening.entity.EducationLevel;
import com.project.cheerha.domain.jobopening.entity.EmploymentType;
import com.project.cheerha.domain.jobopening.entity.JobOpening;
import com.project.cheerha.domain.jobopening.repository.JobOpeningRepository;
import com.project.cheerha.domain.keyword.entity.JobOpeningKeyword;
import com.project.cheerha.domain.keyword.entity.Keyword;
import com.project.cheerha.domain.keyword.repository.JobOpeningKeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.project.cheerha.common.util.CompanyList.companies;
import static com.project.cheerha.common.util.LocationList.locations;
import static com.project.cheerha.common.util.PositionList.positions;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobOpeningInsertScheduler {

    private final JobOpeningRepository jobOpeningRepository;
    private final JobOpeningKeywordRepository jobOpeningKeywordRepository;
    private final Random random = new Random();

    @ScheduledDynamic(minMinutes = 1, maxMinutes = 3)
    public void insertRandomJobOpening() {
        log.info("랜덤 채용공고 데이터 삽입 시작");

        int jobCount = random.nextInt(3) + 1;
        log.info("{}개의 채용 공고 삽입 예정", jobCount);

        List<JobOpening> jobOpenings = new ArrayList<>();
        List<JobOpeningKeyword> allJobOpeningKeywords = new ArrayList<>();

        for (int i = 0; i < jobCount; i++) {
            String company = companies[random.nextInt(companies.length)];
            String position = positions[random.nextInt(positions.length)];
            String location = random.nextDouble() > 0.5 ? locations[random.nextInt(locations.length)] : "서울";
            int maxExperienceYears = 1 + random.nextInt(5);
            int minExperienceYears = random.nextInt(3);
            String jobOpeningUrl = "https://example.com/job/" + UUID.randomUUID();

            double randomPercentage = random.nextDouble();
            int salary;
            if (randomPercentage <= 0.5) {
                salary = 33000000 + (random.nextInt(12) * 1000000);
            } else if (randomPercentage <= 0.51) {
                salary = 70000000 + (random.nextInt(2) * 1000000);
            } else {
                salary = 42000000 + (random.nextInt(28) * 1000000);
            }

            EmploymentType[] employmentTypes = EmploymentType.values();
            EmploymentType employmentType = random.nextDouble() > 0.5 ? employmentTypes[random.nextInt(employmentTypes.length)] : EmploymentType.정규직;
            EducationLevel[] educationLevels = EducationLevel.values();
            EducationLevel educationLevel = educationLevels[random.nextInt(educationLevels.length)];

            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
            int duration = 7 + random.nextInt(24);
            ZonedDateTime hiringEndAt = now.plusDays(duration);

            JobOpening jobOpening = JobOpening.toEntity(
                    company + " " + position + " 모집",
                    company,
                    location,
                    salary,
                    employmentType,
                    educationLevel,
                    jobOpeningUrl,
                    minExperienceYears,
                    maxExperienceYears,
                    position,
                    now,
                    hiringEndAt
            );
            jobOpenings.add(jobOpening);

            int keywordCount = 45;
            int numKeywords = random.nextInt(6) + 5;
            Set<Long> keywordSet = new HashSet<>();
            while (keywordSet.size() < numKeywords) {
                keywordSet.add((long) (random.nextInt(keywordCount) + 1));
            }

            List<JobOpeningKeyword> jobOpeningKeywords = keywordSet.stream()
                    .map(id -> JobOpeningKeyword.toEntity(jobOpening, Keyword.toEntity(id)))
                    .toList();

            allJobOpeningKeywords.addAll(jobOpeningKeywords);
        }

        jobOpeningRepository.saveAll(jobOpenings);
        jobOpeningKeywordRepository.saveAll(allJobOpeningKeywords);

        log.info("총 {}개의 채용 공고가 삽입되었습니다.", jobOpenings.size());
    }
}
