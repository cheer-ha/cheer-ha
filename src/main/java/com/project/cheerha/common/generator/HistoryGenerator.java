package com.project.cheerha.common.generator;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HistoryGenerator {

    public static void main(String[] args) {

        int historyCount = 50000; // 생성할 검색 기록 수
        int userCount = 2000; // 생성할 사용자 수

        // Randome 객체를 생성하여 난수 생성 준비
        Random random = new Random();

        // 검색 기록 생성에 사용할 회사명 배열
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

        // 검색 기록 생성에 사용할 직무명 배열
        String[] names = {
            "개발자", "백엔드", "프론트엔드", "개발", "cloud",
            "Cloud", "Java", "Spring", "Spring Boot", "웹",
            "클라우드", "데이터", "엔지니어", "시스템", "engineer",
            "Backend", "QA", "품질", "품질 보증", "품질보증",
            "Java 개발자", "Spring 개발자", "백엔드 개발자", "프론트엔드 개발자", "풀스택 개발자",
            "웹 애플리케이션 개발자", "애플리케이션", "웹 개발자", "DevOps 엔지니어", "API 서버 개발자",
            "데브옵스", "데브옵스 엔지니어", "Python 개발자", "파이썬", "파이썬 개발자",
            "소프트웨어", "web", "Web", "Web 개발자", "백엔드 개발자 채용",
            "풀스택 채용", "front end", "Front End", "웹개발자", "플랫폼",
            "플랫폼 개발자", "JAVA", "블록체인", "SW", "SW 개발자",
            "Backend", "Software Engineer", "서버", "서버 개발자", "Node.js",
            "백엔드개발자", "AI", "Back-end", "Front-end", "BE",
            "FE", "Back-End", "Front-End", "Server", "server", "BACK",
            "developer", "Engineer", "React", "Springframework", "데이터 엔지니어",
            "Platform", "platform", "AI platform", "AI Platform", "웹 풀스택",
            "JAVA 개발자", "빅데이터", "python", "BigData", "빅데이터 플랫폼",
            "솔루션", "솔루션 백엔드", "플랫폼 데이터", "파이썬 데이터", "풀스택",
            "Fullstack", "Full-stack", "자바", "자바 개발자", "Fullstack 개발",
            "풀스택 소프트웨어", "Cloud Platform", "클라우드 자바", "Fullstack Developer",
            "Full stack", "Full Stack Developer", "Vue.js", "node.js", "NodeJS",
            "블록체인 SW", "JAVA Spring", "Node", "SPRING", "spring",
            "스프링", "스프링부트", "WEB", "리액트", "Spring Framework",
            "QA엔지니어", "QA Engineer", "서버 백엔드", "백엔드개발", "프론트엔드개발",
            "front", "back", "FRONT", "Front", "Back"
        };

        try (FileWriter writer = new FileWriter("history.csv")) {
            // CSV 파일 헤더(header) 작성
            writer.append("id,name,user_id,created_at\n");

            long id = 1; // 검색 기록 식별자 초기화
            int totalGenerated = 0; // 생성된 검색 기록 수

            // (1) 사용별로 검색 횟수를 무작위로 결정
            // (2) 검색 기록 생성
            for (int userId = 1; userId <= userCount && totalGenerated < historyCount; userId++) {

                // 각 사용자가 검색할 휫수를 무작위로 결정
                // 1~20 사이
                int searchCount = random.nextInt(100) + 1;

                // 해당 사용자의 검색 횟수만큼 기록 생성
                for (int i = 0; i < searchCount && totalGenerated < historyCount; i++) {
                    String name;
                    if (random.nextBoolean()) { // 직무명 무작위로 선택
                        name = names[random.nextInt(names.length)];
                    } else { // 회사명 무작위로 선택
                        name = companies[random.nextInt(companies.length)];
                    }

                    // 검색 기록 날짜를 1~7일 중 무작위로 결정
                    LocalDateTime createdAt = LocalDateTime.now()
                        .minusDays(1 + random.nextInt(7));

                    // 검색 기록을 CSV 파일에 생성
                    writer.append(String.valueOf(id++))
                        .append(",").append(name)
                        .append(",").append(String.valueOf(userId))
                        .append(",").append(createdAt.toString())
                        .append("\n");

                    // 생성된 검색 기록 수 증가
                    totalGenerated++;
                }
            }

            log.info("CSV 파일 생성 성공");
        } catch (IOException e) {
            log.error("파일 생성 중 오류 발생: {}", e.getMessage());
        }
    }
}