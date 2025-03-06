package com.project.cheerha.domain.jobopening.schdeuler.factory;

import com.project.cheerha.domain.jobopening.entity.EducationLevel;
import com.project.cheerha.domain.jobopening.entity.EmploymentType;
import com.project.cheerha.domain.jobopening.entity.JobOpening;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.UUID;

import static com.project.cheerha.common.util.factory.CompanyList.companies;
import static com.project.cheerha.common.util.factory.LocationList.locations;
import static com.project.cheerha.common.util.factory.PositionList.positions;

public class JobOpeningFactory {

    private static final Random random = new Random();

    //jobOpeningGenerator 의 로직을 그대로 사용하므로, 상수를 static 으로 두지 않음
    //location "서울" 50%로 확률 올림
    //url 도 UUID 로 랜덤으로 생성
    public static JobOpening createRandomJobOpening() {
        String company = companies[random.nextInt(companies.length)];
        String position = positions[random.nextInt(positions.length)];
        String location = random.nextDouble() > 0.5 ? locations[random.nextInt(locations.length)] : "서울";

        int maxExperienceYears = 1 + random.nextInt(5);
        int minExperienceYears = random.nextInt(3);

        String jobOpeningUrl = "https://example.com/job/" + UUID.randomUUID();
        int salary = calculateRandomSalary();

        EmploymentType[] employmentTypes = EmploymentType.values();
        EmploymentType employmentType = random.nextDouble() > 0.5 ? employmentTypes[random.nextInt(employmentTypes.length)] : EmploymentType.정규직;
        EducationLevel[] educationLevels = EducationLevel.values();
        EducationLevel educationLevel = educationLevels[random.nextInt(educationLevels.length)];

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        int duration = 7 + random.nextInt(24);
        ZonedDateTime hiringEndAt = now.plusDays(duration);

        return JobOpening.toEntity(
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
    }

    //jobOpeningGenerator 의 로직을 그대로 사용하므로, 상수를 static 으로 두지 않음
    private static int calculateRandomSalary() {
        double randomPercentage = random.nextDouble();
        if (randomPercentage <= 0.5) {
            return 33000000 + (random.nextInt(12) * 1000000);
        } else if (randomPercentage <= 0.51) {
            return 70000000 + (random.nextInt(2) * 1000000);
        } else {
            return 42000000 + (random.nextInt(28) * 1000000);
        }
    }
}
