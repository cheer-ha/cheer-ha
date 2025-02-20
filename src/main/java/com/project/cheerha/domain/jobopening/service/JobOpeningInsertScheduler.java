package com.project.cheerha.domain.jobopening.service;

import com.project.cheerha.domain.jobopening.entity.EducationLevel;
import com.project.cheerha.domain.jobopening.entity.EmploymentType;
import com.project.cheerha.domain.jobopening.entity.JobOpening;
import com.project.cheerha.domain.jobopening.repository.JobOpeningRepository;
import com.project.cheerha.domain.keyword.entity.JobOpeningKeyword;
import com.project.cheerha.domain.keyword.entity.Keyword;
import com.project.cheerha.domain.keyword.repository.JobOpeningKeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobOpeningInsertScheduler {

    private final JobOpeningRepository jobOpeningRepository;
    private final JobOpeningKeywordRepository jobOpeningKeywordRepository;
    private final Random random = new Random();

    private final String[] companies = {
            "삼성", "LG", "카카오", "네이버", "구글", "마이크로소프트", "애플", "토스", "배달의민족", "카페24",
            "당근마켓", "마켓컬리", "롯데", "SK", "한화", "KT", "현대자동차", "포스코", "삼성전자", "LG전자",
            "CJ", "쿠팡", "위메프", "11번가", "인터파크", "다이소", "이마트", "삼성생명", "교보문고", "하이닉스",
            "제일기획", "넥슨", "넷마블", "엔씨소프트", "반도건설", "현대백화점", "신세계", "롯데쇼핑", "삼성SDS", "카카오게임즈",
            "배달통", "레진코믹스", "마켓플러스", "왓챠", "피지컬갤러리", "브랜디", "오르비", "부릉", "그라운드X", "라인플러스",
            "모두의연애", "윙크", "아모레퍼시픽", "셀트리온", "비바리퍼블리카", "아이디병원", "피자헛", "도미노피자", "올리브영",
            "브라운백", "삼성물산", "SK텔레콤", "농심", "기아", "삼성전기", "LG디스플레이", "아시아나항공", "대한항공", "롯데면세점",
            "유한양행", "한미약품", "광주은행", "하나금융그룹", "신한은행", "삼성카드", "현대카드", "롯데카드", "BC카드", "KB국민카드",
            "우리은행", "카카오뱅크", "토스뱅크", "NH농협은행", "하나은행", "삼성생명", "AIG손해보험", "한화생명", "메리츠화재", "롯데손해보험",
            "셀트리온헬스케어", "헬로윤미", "코스모스", "메타넷", "LG화학", "신세계인터내셔날", "LG생활건강", "애경", "유니레버", "한국타이어",
            "두산", "넥스트레벨", "뷰웍스", "영림원소프트랩", "모비스", "컴투스", "게임빌", "게임펍", "카트라이더", "타이탄", "쿠팡플러스",
            "에어비앤비", "우버", "배달의민족", "링크드인", "패스트파이브", "티몬", "오늘의집", "필립스", "시그마", "하이네켄", "애드테크",
            "알리바바", "아이유브이", "로젠택배", "네오위즈", "스마일게이트", "리틀빅아이디어", "넥스트챌린지", "더존비즈온", "로켓펀치"
    };
    private final String[] positions = {
            "백엔드 개발자", "Java 개발자", "Spring 개발자", "Node.js 개발자", "풀스택",
            "데이터베이스 관리자(DBA)", "시스템 엔지니어", "데브옵스 엔지니어", "클라우드 엔지니어", "데이터 엔지니어",
            "웹 애플리케이션 개발자", "프론트엔드 개발자", "파이썬 개발자", "자바스크립트 개발자", "QA"
    };
    private final String[] locations = {
            "서울", "부산", "대전", "대구", "광주",
            "인천", "수원", "울산", "창원", "청주"
    };

    @Scheduled(fixedRate = 1)
    public void insertRandomJobOpening() {

        String company = companies[random.nextInt(companies.length)];

        String position = positions[random.nextInt(positions.length)];

        String location = locations[random.nextInt(locations.length)];

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
        EmploymentType employmentType = EmploymentType.정규직;
        if (random.nextDouble() > 0.5) {
            employmentType = employmentTypes[random.nextInt(employmentTypes.length)];
        }

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
        jobOpeningRepository.save(jobOpening);

        int keywordCount = 45;
        int numKeywords = (random.nextInt(6) + 5);
        Set<Long> keywordSet = new HashSet<>();
        while (keywordSet.size() < numKeywords) {
            long keywordId = random.nextInt(keywordCount) + 1;
            keywordSet.add(keywordId);
        }

        List<JobOpeningKeyword> jobOpeningKeywordList = keywordSet.stream()
                .map(id -> JobOpeningKeyword.toEntity(jobOpening, Keyword.toEntity(id)))
                .toList();

        jobOpeningKeywordRepository.saveAll(jobOpeningKeywordList);
        log.info("랜덤 채용 공고 삽입: {}", jobOpening);
    }
}
