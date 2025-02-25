package com.project.cheerha.common.generator;

import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;

import com.project.cheerha.domain.jobopening.entity.EducationLevel;
import com.project.cheerha.domain.jobopening.entity.EmploymentType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JobOpeningGenerator {

    public static void main(String[] args) {
        int jobOpeningCount = 10000; // 생성할 채용 공고 수

        Random random = new Random();
        // Random 객체로 무작위 값 생성

        // 데이터 생성에 사용할 회사명 배열
        String[] companies = {
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

        // 데이터 생성에 사용할 학력 수준 배열 (enum으로 수정)
        EducationLevel[] educationLevels = EducationLevel.values();

        // 데이터 생성에 사용할 고용 형태 배열 (enum으로 수정)
        EmploymentType[] employmentTypes = EmploymentType.values();

        // 데이터 생성에 사용할 지역 배열
        String[] locations = {
            "서울", "부산", "대전", "대구", "광주",
            "인천", "수원", "울산", "창원", "청주"
        };

        // 데이터 생성에 사용할 직무 배열
        String[] positions = {
            "백엔드 개발자", "Java 개발자", "Spring 개발자", "Node.js 개발자", "풀스택",
            "데이터베이스 관리자(DBA)", "시스템 엔지니어", "데브옵스 엔지니어", "클라우드 엔지니어", "데이터 엔지니어",
            "웹 애플리케이션 개발자", "프론트엔드 개발자", "파이썬 개발자", "자바스크립트 개발자", "QA"
        };

        try (FileWriter writer = new FileWriter("job_openings.csv")) {
            // CSV 헤더 작성
            writer.append(
                "id,company,education_level,employment_type,hiring_end_at,hiring_start_at,job_opening_url,location,created_at,max_experience_years,min_experience_years,salary,title,view_count,position\n");

            // 지정된 채용 공고 수만큼 값 생성
            for (int jobOpeningId = 1; jobOpeningId <= jobOpeningCount; jobOpeningId++) {
                // 무작위로 값 생성
                String company = companies[random.nextInt(companies.length)];
                EducationLevel educationLevel = educationLevels[random.nextInt(
                    educationLevels.length)];
                String location = locations[random.nextInt(locations.length)];
                String position = positions[random.nextInt(positions.length)];

                // 고용 형태의 50%는 정규직, 나머지는 무작위
                EmploymentType employmentType = EmploymentType.정규직;
                if (random.nextDouble() > 0.5) {
                    employmentType = employmentTypes[random.nextInt(employmentTypes.length)];
                }

                // 채용 시작일을 현재 날짜로 설정
                ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
                String hiringStartAt = now.toString();

                // 채용 기간을 7일에서 30일 사이 무작위로 설정
                int duration = 7 + random.nextInt(24);
                ZonedDateTime hiringEndAt = now.plusDays(duration);
                String hiringEndAtStr = hiringEndAt.toString();

                // 고유한 채용 공고 URL 생성
                String jobOpeningUrl = "https://example.com/job/" + jobOpeningId;

                // 최소 연차 및 최대 연차 무작위 생성
                // maxExperienceYears: 1~5년
                // minExperienceYears: 0~2년
                int maxExperienceYears = 1 + random.nextInt(5);
                int minExperienceYears = random.nextInt(3);

                double randomPercentage = random.nextDouble();  // 0.0 ~ 1.0 사이의 랜덤 실수
                int salary;

                if (randomPercentage <= 0.5) {
                    // 50% 확률로 3300만 원에서 4200만 원 사이의 값
                    salary = 33000000 + (random.nextInt(12) * 1000000);
                } else if (randomPercentage <= 0.51) {
                    // 1% 확률로 7000만 원에서 8000만 원 사이의 값
                    salary = 70000000 + (random.nextInt(2) * 1000000);
                } else {
                    // 나머지 49% 확률로 4200만 원에서 7000만 원 사이
                    salary = 42000000 + (random.nextInt(28) * 1000000);
                }

                // 채용 공고 조회수 무작위 생성
                int viewCount = random.nextInt(5000);

                // 채용 공고 생성일
                // 채용 공고 생성일
                ZonedDateTime createdAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
                String createdAtStr = createdAt.toString();

                // 채용 공고 제목 설정 (회사 이름 + 포지션 + 모집)
                String title = company + " " + position + " 모집";

                // CSV 파일에 기록
                writer.append(String.valueOf(jobOpeningId)).append(",")
                    .append(company).append(",")
                    .append(educationLevel.name()).append(",")
                    .append(employmentType.name()).append(",")
                    .append(hiringEndAtStr).append(",")
                    .append(hiringStartAt).append(",")
                    .append(jobOpeningUrl).append(",")
                    .append(location).append(",")
                    .append(createdAtStr).append(",")
                    .append(String.valueOf(maxExperienceYears)).append(",")
                    .append(String.valueOf(minExperienceYears)).append(",")
                    .append(String.valueOf(salary)).append(",")
                    .append(title).append(",")
                    .append(String.valueOf(viewCount)).append(",")
                    .append(position)
                    .append("\n");
            }

            log.info("CSV 파일 생성 성공");

        } catch (IOException e) {
            log.error("파일 생성 중 오류 발생: {}", String.valueOf(e));
        }
    }
}