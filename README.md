# 3조 - 👑 취하여 (Cheer-ha)

## 🚀 서비스 소개
### ✨ 서비스 개요

<aside>

### "취업을 위하여! 똑똑한 개발자 취업, 클릭 한 번으로 시작하세요!"

‘취하여’는 포트폴리오와 코딩 테스트 준비로 바쁜 개발자들이 여러 사이트를 돌아다니며 채용 공고를 찾는 수고를 덜고, 원하는 채용 정보를 빠르고 편리하게 확인할 수 있도록 지원하는 서비스입니다.

다양한 사이트에 분산된 개발자 채용 공고를 한곳에 모아 보여주고, 사용자의 기술 역량으로 지원할 수 있는 채용 공고를 이메일 알림으로 제공합니다.

</aside>

---

### 🎯 **‘취하여’가 해결하는 세 가지 불편함**
![취업 고민 이미지](https://github.com/llRosell/sparta/blob/main/%E1%84%8E%E1%85%B1%E1%84%92%E1%85%A1%E1%84%8B%E1%85%A7%20%E1%84%8E%E1%85%AC%E1%84%8C%E1%85%A9%E1%86%BC%20%E1%84%87%E1%85%A1%E1%86%AF%E1%84%91%E1%85%AD%E1%84%92%E1%85%AC%20(1).jpg?raw=true)

🔹 **정확한 단어 검색 필요** ➡️ **단어 일부만 입력해도 검색 가능!** 🔍    
🔹 **인기 기준 모호** ➡️ **연령대·기술 요건 기반 인기 공고 제공!** 🔥  
🔹 **반복 입력 불편** ➡️ **기술 역량 등록 시 맞춤 공고 이메일 발송!** 📩  

---

### 🏷️ **도메인 용어**

| **항목**  | **키워드**                                        | **채용 공고**                             |
|---------|------------------------------------------------|---------------------------------------|
| **정의**  | 채용 공고 안에 있는 기술 요건 목록                           | 여러 채용 사이트를 크롤링하여 가져온 개발자 채용 공고 목록     |
| **특징**  | 백엔드 및 프론트엔드에서 쓰이는 스택 위주                        | 고용 형태, 학력, 직무 등 자격 요건 및 URL 포함        |
| **종류**  | 프로그래밍 언어, 프레임워크, 라이브러리 등                 | 백엔드 개발자 채용 공고, 데이터 엔지니어 채용 공고 등       |
| **예시**  | Java, Python, Spring Boot, Kafka, AWS, Jira    | ‘토스 백엔드 개발자 모집’, ‘카카오뱅크 서버 개발자 모집’    |

---

### 👤 사용자 이용 흐름

![사용자 이용 흐름 이미지](https://github.com/llRosell/sparta/blob/main/%E1%84%8B%E1%85%B5%E1%84%8B%E1%85%AD%E1%86%BC%E1%84%92%E1%85%B3%E1%84%85%E1%85%B3%E1%86%B7.png?raw=true)

---

### 🔄 서비스 작동 흐름

![사용자 이용 흐름 이미지](https://github.com/llRosell/sparta/blob/main/%E1%84%8B%E1%85%B5%E1%84%8B%E1%85%AD%E1%86%BC%E1%84%92%E1%85%B3%E1%84%85%E1%85%B3%E1%86%B71.png?raw=true)

---

### 🔑 핵심 기능

<aside>

#### 🔍 **클린 코드처럼 깔끔한 검색 기능!**
- **자동 완성 & 부분 검색 지원**
  - 한 글자만 입력해도 추천 검색어 제공
  - 오탈자를 입력해도 정확한 채용 공고 조회 가능
- **검색 우선순위 적용**
  - `제목 → 회사명 → 키워드` 순으로 검색
    <details>
      <summary> 필터 항목 보기 </summary>
      <ul>
        <li>지역</li>
        <li>채용 형태: 정규직, 계약직, 아르바이트, 인턴, 프리랜서</li>
        <li>학력: 무관, 고졸, 전문학사, 학사, 석사, 박사</li>
        <li>채용 시작일 & 마감일</li>
        <li>최소 경력 & 최고 경력</li>
        <li>채용 공고 제목, 회사명, 기타 자격 요건</li>
      </ul>
    </details>

#### 🔥 **HOT한 채용 공고만 모아 모아!**
- **조회수 Top 100 인기 채용 공고**
- **연령대마다 가장 많이 즐겨찾기로 등록한 채용 공고 Top 10**
- **연령대별로 많이 등록된 키워드 Top 10**

#### 🔖 **즐겨찾기와 이메일 알림으로 취업을 위하여!**
- **관심 있거나 자신의 기술 역량을 키워드로 등록, 조회, 삭제 가능**
- **맞춤형 채용 공고 이메일 알림**
  - 등록한 키워드를 기반으로 `하루 1회` 맞춤형 채용 공고 20건 이메일 발송
  - 이메일 인증 필수
  - 20건 선정 기준: 사용자가 등록한 키워드가 많이 겹치는 순 내림차순
- **즐겨찾기 기능**
  - 관심 있는 채용 공고를 즐겨찾기에 등록, 조회, 삭제 가능
  - 최대 200개 등록 가능: 초과 시 가장 오래된 항목 자동 삭제
  - 마감된 공고도 즐겨찾기에서 확인 가능
  
<aside>

--- 

## ⚡ 성능 개선, 어디까지 해봤니?

<details>
  <summary> 🏎️ MySQL vs Elasticsearch, 채용 공고 검색 시 무엇을 사용할까요? </summary>

### 환경
- **Elasticsearch 버전**: 8.17.2
- **QueryDSL**: MySQL을 이용한 데이터 조회
- **테스트 도구**: Apache JMeter
- **테스트 요청**: HTTP GET 요청
- **동시 요청**: 200개 (100개씩 총 2번 요청)
- **서버 환경**: 로컬 서버 (localhost)
- **클라이언트 환경**: JMeter 클라이언트

### 비교
| **테스트 항목**    | **MySQL 조회 (QueryDSL)**  | **Elasticsearch 조회**  | **성능 향상률**  |
|---------------|--------------------------|-----------------------|-------------|
| **평균 응답 시간**  | 48ms                     | 14ms                  | 70.83%      |
| **최소 응답 시간**  | 39ms                     | 9ms                   | 77%         |
| **최대 응답 시간**  | 117ms                    | 41ms                  | 65%         |
| **표준편차**      | 9.37ms                   | 3.28ms                | 64%         |
| **TPS**       | 8.63/sec                 | 9.5/sec               | +10.1%      |
| **수신량**       | 8.63KB/sec               | 65.01KB/sec           | +653.5%     |
| **전송된 데이터**   | 0.57KB                   | 3.94KB                | +591.2%     |
| **평균 바이트**    | 6300.9 Byte              | 6971.9 Byte           | +10.7%      |

### 결론
- **응답 시간**: 최소 응답 시간은 77%, 최대 응답 시간은 65% 향상됨
- **표준편차**: 약 64% 향상됨
- **TPS**: 약 10% 증가함
- **수신량 및 전송량**: 수신량은 653.5%, 전송량은 591.2% 증가함
- **대규모 데이터 조회 성능 개선이 필요할 때는 Elasticsearch 사용**
</details>

<details>
  <summary> 🏎️ 채용 공고를 조회할 때 스레드 수는 얼마나 늘릴 수 있을까요? </summary>

###  환경
- **Elasticsearch 버전**: 8.17.2
- **Elasticsearch QueryDSL**: Elasticsearch에서 데이터를 직접 조회하여 처리
- **테스트 도구**: Apache JMeter
- **테스트 요청**: HTTP GET 요청
- **동시 요청**: 100개 ~ 2100개까지 100씩 증가하며 테스트 (10초로 설정)
- **서버 환경**: 로컬 서버 (localhost)
- **클라이언트 환경**: JMeter 클라이언트

### 비교 
![키바나 모니터링 그래프 이미지](https://github.com/llRosell/sparta/blob/main/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202025-03-14%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%208.35.46.png?raw=true)

| **요청 개수**      | **시스템 상태**          | **응답 시간**            | **TPS**             | **기타 영향**       |
|-----------------|-------------------|--------------------|----------------|--------------|
| 🟢 **1600개 이하** | 안정적으로 처리 가능     | ⏳ 일정하게 유지 (8-7ms)  | 📈 일정하게 증가   | -            |
| 🟡 **1600개 이상** | 시스템 자원이 부족해짐   | ⏳ 일정하게 유지 (8-7ms)  | 📉 일부 구간 정체   | 성능 저하 발생  |
| 🔴 **2100개 이상** | 시스템 한계 초과       | ⏳ 느려짐 (11ms)         | 📉 처리량 감소     | 심각한 성능 저하  |

### 결론
- 스레드 수가 증가함에 따라 응답 시간과 TPS가 명확하게 변화함 
- 스레드 수가 1600개일 때까지는 시스템이 원활하게 요청을 처리 
- 스레드 수가 2100개 이상 늘어나면 성능이 저하됨 
- **성능 한계 전에 자원을 효율적으로 관리하고 병목을 예방해야 함**
</details>

<details>
  <summary>🏎️ 연령대별 인기 키워드 조회 기능의 속도와 처리량을 어떻게 늘릴까요?</summary>

### 환경
- **서버**: 로컬 환경 및 배포 서버에서 테스트 수행
- **사용된 도구**: Postman, JMeter, QueryDSL
- **연령대**: 취업 연령층이 가장 많은 25세에서 40세로 고정
- **테스트 도구**: Apache JMeter
- **스레드**: 100 스레드로 고정

### 비교

**1. 쿼리 변경 전후 비교 결과**
- QueryDSL을 사용하여 서브 쿼리 제거
- 단일 쿼리 안에서 `count`를 바로 계산

(1) 스레드 100개를 10초로 나누어서 요청 처리 시 ⬇️

| **비교 항목**         | **서브 쿼리 적용 시** | **단일 쿼리 적용 시** | **성능 향상률** | **배율** |
|----------------------|----------------------|----------------------|----------------|----------|
| **평균 응답 시간**    | 41493ms              | 50ms                 | 99.88%         | 829.86배 |
| **최소 응답 시간**    | 30012ms              | 33ms                 | 99.89%         | 909.45배 |
| **최대 응답 시간**    | 145268ms             | 102ms                | 99.93%         | 1424.2배 |
| **표준 편차**         | 34426.78ms           | 8.05ms               | 99.98%         | 4276.62배 |
| **에러 발생 비율**    | 90.00%               | 0.00%                | 100.0%         | 완전 개선 |
| **처리량**            | 41.2/min             | 606/min              | 1370.87%       | 14.71배  |

  (2) 스레드 100개를 120초로 나누어서 요청 처리 시 ⬇️

| **비교 항목**         | **서브 쿼리 적용 시** | **단일 쿼리 적용 시** | **성능 향상률** | **배율** |
|----------------------|----------------------|----------------------|----------------|----------|
| **평균 응답 시간**    | 48315ms              | 65ms                 | 99.87%         | 743.31배 |
| **최소 응답 시간**    | 30011ms              | 54ms                 | 99.82%         | 555.76배 |
| **최대 응답 시간**    | 145738ms             | 145ms                | 99.9%          | 1005.09배 |
| **표준 편차**         | 40427.71ms           | 10.06ms              | 99.98%         | 4018.66배 |
| **에러 발생 비율**    | 83.00%               | 0.00%                | 100.0%         | 완전 개선 |
| **처리량**            | 23.9/min             | 50.5/min             | 111.3%         | 2.11배   |

**2. **`user`** 테이블의 **`age`** 컬럼에 인덱스 추가**  
(1) 인덱스 적용 전후 **`EXPLAIN`** 비교

| **비교 항목**             | **인덱스 적용 전**      | **인덱스 적용 후**        | **개선 사항**                  |
|--------------------------|------------------------|--------------------------|----------------------------|
| **user 테이블 조회 방식** | ALL (Full Table Scan)   | range (Index Range Scan) | ✅ 인덱스 적용으로 테이블 전체 스캔 제거    |
| **JOIN 방식**             | ref                    | ref                      | -                          |
| **keyword 테이블 조회 방식** | eq_ref                 | eq_ref                   | -                          |
| **조회된 user 테이블 행 개수** | 2000                   | 1118                     | ✅ 인덱스 적용으로 조회 대상 감소        |
| **Filtered 비율**         | 11.11%                 | 100%                     | ✅ 불필요한 행 단위 스캔 제거          |
| **Extra**                 | Using temporary; Using filesort | Using where; Using index; Using temporary | ✅ 파일 정렬 제거<br> ✅ 인덱스 활용 증가 |
| **Possible Keys**         | PRIMARY                | PRIMARY, idx_user_age    | ✅ 추가 인덱스 활용 가능             |

(2) 인덱스 적용 전후 **`EXPLAIN ANALYZE`** 비교

| **비교 항목**             | **인덱스 적용 전** | **인덱스 적용 후** | **개선 사항**           |
|--------------------------|-------------------|-------------------|------------------------|
| **Nested Loop Join 시간** | 40.2ms            | 36ms              | ✅ 불필요한 연산 감소   |
| **필터링된 row 개수**     | 2000              | 1118              | ✅ 불필요한 행 단위 스캔 감소 |
| **Using Index 적용 여부** | No                | Yes               | ✅ Covering Index Scan 활용 |
| **실행 시간**             | 62.5ms            | 52.4ms            | ✅ 약 16% 속도 개선     |

### 결론 

- **서브 쿼리 최적화 후 성능 개선**
   - 실행 시간: 31.49s → 146ms로 **99.5% 단축**
   - 에러 발생 비율: 90% → **0%로 완전 개선**
   - 응답 시간: 평균 50ms로 안정적 유지 
- **인덱스 적용 후 성능 비교**
   - 실행 시간: 62.5ms → 52.4ms로 **약 16% 개선**
   - 인덱스를 적용한 결과는 성능 개선이 크게 이루어지지 않음 
   - 또한, 인덱스를 적용하면 쓰기 성능에 영향을 미치는 만큼, 인덱스 적용 ❌
</details>

<details>
  <summary> 🏎️ 서브 쿼리 vs 단일 쿼리, 연령대별 인기 즐겨찾기 조회에는 무엇이 좋을까요? </summary> 

### 환경 

- **테스트 도구**: Apache JMeter
- **테스트 요청**: HTTP 요청 (GET)
- **동시 요청**: 100개의 스레드로 테스트 (10초 동안)
- **서버 환경**: 로컬 서버 (localhost)
- **클라이언트 환경**: JMeter 클라이언트
- **데이터베이스**: MySQL
- **쿼리 처리 도구**: MySQL QueryDSL

### 비교

| **비교 항목** | **서브쿼리로 조회 시** | **단일쿼리로 조회 시** | **성능 개선 비율**  |
| --- | --- | --- | --- |
| **평균 응답 시간** | 29s 767ms | 787ms | 97.36% |
| **최소 응답 시간** | 12s 860ms | 553ms | 95.70% |
| **최대 응답 시간** | 38s 828ms | 1s 81ms | 97.21% |
| **표준편차** | 6s 145.32ms | 106.37ms | 98.27% |
| **에러 비율** | 70.00% | 0.00% | 100.00% |
| **처리량** | 2.4/sec | 9.5/sec | 295.83% |

### 결론
- 평균 응답 시간: **97.36% 개선**
- 처리량: **295.83% 향상**
- 에러 비율: **70%에서 0%로 완전 개선**
- Postman 로컬 서버에서 조회: 3s 62ms → 376ms로 **87.76% 향상**
- Postman 배포 서버에서 조회: 11s 13ms → 50ms로 **99.55% 향상**
- Jmeter 테스트: 29s 737ms → 787ms로 **97.36% 향상**

</details>

<details>
  <summary> 🏎️ 21초에서 9초, 이메일 알림 발송 속도를 어떻게 개선할 수 있을까요? </summary>

### 환경
- **서버 환경**: 로컬 서버 (localhost)
- **이메일 발송 방식**: Gmail SMTP
- **SMTP 설정**: Gmail SMTP (포트 587)
- **이메일 발송 요청 방식**: Spring Boot MailSender 사용
- **총 이메일 발송 건수**: 9건 (3명 × 3회)
- **데이터 ⬇️**  
   (1) 사용자 

  | **사용자 ID (user_id)**    | **키워드 ID (keyword_id)**   | **이메일 (email)**      |
  |-------------------------|---------------------------|----------------------|
  | **1**                   | 1, 2, 5                   | test1@gmail.com      |
  | **2**                   | 4, 5                      | test2@gmail.com      |
  | **3**                   | 1, 2, 7                   | test3@gmail.com      |

   (2) 채용 공고

  | **채용 공고 ID (job_opening_id)**    | **키워드 ID (keyword_id)**   | **URL (job_opening_url)**    |
  |----------------------------------|---------------------------|------------------------------|
  | **1**                            | 1, 2, 3                   | url1                         |
  | **2**                            | 4, 5                      | url2                         |

   (3) 같은 키워드끼리 연결한 결과

  | **키워드 ID (keyword_id)**    | **채용 공고 ID (job_opening_id)**    | **사용자 ID (user_id)**    |
  |----------------------------|----------------------------------|-------------------------|
  | **1, 2, 3**                | 1                                | 1, 3                    |
  | **4, 5**                   | 2                                | 1, 2                    |

### 비교
- `transform()` 메서드로 불필요한 연산 제거 
- `ThreadPoolTaskScheduler`를 10개로 설정하여 발송 작업을 비동기로 처리

(1) 개선 전 테스트 (채용 공고 및 사용자 조회는 2회차부터 데이터베이스 캐싱이 적용되므로, 해당 값 반영 ❌)

| **회차** | **채용 공고 조회** | **사용자 조회**  | **전체 발송**       | **전체 작업**       | **메일 발송 1** | **이메일 발송 2** | **이메일 발송 3** |
|-------|--------------|-------------|-----------------|-----------------|-------------|--------------|--------------|
| **1** | 150 ms       | 3 ms        | 22 s 490 ms     | 22 s 644 ms     | 9 s 58 ms   | 4 s 199 ms   | 9 s 231 ms   |
| **2** | - ms         | - ms        | 21 s 550 ms     | 21 s 556 ms     | 8 s 419 ms  | 4 s 113 ms   | 9 s 17 ms    |
| **3** | - ms         | - ms        | 21 s 458 ms     | 21 s 470 ms     | 8 s 808 ms  | 3 s 415 ms   | 9 s 95 ms    |
| **평균** | **150 ms**   | **3 ms**    | **21 s 832 ms** | **21 s 890 ms** | **8 s 808 ms** | **3 s 909 ms** | **9 s 114 ms** |


(2) 개선 후 테스트 (채용 공고 및 사용자 조회는 2회차부터 데이터베이스 캐싱이 적용되므로, 해당 값 반영 ❌)

| **회차** | **채용 공고 조회** | **사용자 조회** | **전체 발송**      | **전체 작업**   | **이메일 발송 1**   | **이메일 발송 2**   | **이메일 발송 3** |
|--------|--------------|------------|----------------|-------------|----------------|----------------|--------------|
| **1**  | 100 ms       | 3 ms       | 9 s 830 ms     | 9 s 936 ms  | 9 s 829 ms     | 9 s 292 ms     | 9 s 432 ms   |
| **2**  | - ms         | - ms       | 9 s 615 ms     | 9 s 623 ms  | 8 s 292 ms     | 9 s 614 ms     | 8 s 732 ms   |
| **3**  | - ms         | - ms       | 9 s 762 ms     | 9 s 771 ms  | 9 s 762 ms     | 9 s 406 ms     | 8 s 553 ms   |
| **평균** | **100 ms**   | **3 ms**   | **9 s 736 ms** | **9 s 777 ms** | **9 s 294 ms** | **9 s 437 ms** | **8s 906 ms** |

(3) 개선 전후 비교

| **테스트 항목**    | **개선 전 방식 (평균)** | **개선 후 방식 (평균)** | **성능 향상률** | **배율**   |
|---------------|------------------|------------------|------------|----------|
| **채용 공고 조회**  | 150 ms           | 100 ms           | 33.3%      | 1.5배 향상  |
| **사용자 조회**    | 3 ms             | 3 ms             | -          | **-**    |
| **전체 이메일 발송** | 21 s 832 ms      | 9 s 736 ms       | 55.4%      | 2.24배 향상 |
| **전체 작업**     | 21 s 890 ms      | 9 s 777 ms       | 55.4%      | 2.24배 향상 |

### 결론
- **발송 전 로직 개선 후 성능 비교**
    - 채용 공고 조회 속도: 150ms → 100ms로 **33.3% 단축**
- **발송 작업을 비동기로 처리 후 성능 비교**
    - 전체 이메일 발송 작업: 21s 832ms → 9s 736ms로 **55.4% 개선**
    - 전체 작업 완료: 21s 890ms → 9s 777ms로 **55.4% 개선**
- **스레드 풀과 비동기 처리를 제대로 적용하면 기능 최적화 가능** 
</details>
</aside>

<aside>

## 🏗️ System Architecture

### ☁️ Cloud Architecture

![아키텍쳐 이미지](https://github.com/llRosell/sparta/blob/main/%E1%84%8B%E1%85%A1%E1%84%8F%E1%85%B5%E1%84%90%E1%85%A6%E1%86%A8%E1%84%8E%E1%85%A7.png?raw=true)

### ⛓️ CI/CD Pipeline

![CI/CD Pipeline 이미지](https://github.com/llRosell/sparta/blob/main/cicd.jpg?raw=true)

### 📐 설계 과정

<details>
  <summary> Cloud Architecture </summary>

<details>
  <summary> 백엔드 </summary>

- 백엔드는 **Spring Boot** 기반 애플리케이션으로 **AWS EC2 인스턴스 내의 Docker** 에서 실행됩니다.
- ALB (Application Load Balancer)를 사용하여 사용자 요청을 여러 백엔드 서버로 라우팅하여 부하를 분산하고, 주기적인 Health Check와 ASG로 높은 가용성을 제공합니다.
- EC2 인스턴스는 Auto Scaling Group에 속하여 트래픽 증가에도 안전하게 대응할 수 있도록 설정되었습니다.
</details>

<details>
  <summary> 데이터베이스 및 캐시 </summary>

- **MySQL (RDS)**: 고가용성과 편리한 파라미터 설정을 위해 **RDS**에서 관리됩니다.
- **Redis (ElastiCache)**: Redis는 보안이 취약하므로 **ElastiCache에서 제공하는 높은 보안 수준을** 통해 관리됩니다.
- **Elasticsearch**: 신버전을 편리하게 사용할 수 있는 공식 클라우드서비스인 Elastic Cloud를 사용하였습니다. 플러그인 추가가 편리하고, 까다로운 Security 설정을 직접 하지 않아도 됩니다.
</details>

<details>
  <summary> 네트워크 관리 </summary>

- **Route 53**을 사용하여 도메인을 관리하고, 사용자가 접근하는 요청을 **ALB**를 통해 처리하도록 설정합니다.
- **ALB에 SSL 인증서**를 연결해 **HTTPS 요청만 처리**하도록 합니다. **HTTP 요청은 자동으로 HTTPS로 리디렉션됩니다.**
- **VPC** 내에서 **메인 애플리케이션의 Auto Scaling Group**, **RDS MySQL**과 **ElastiCache Redis, 모니터링/로깅 서버를** 함께 관리하여 보안을 강화하고, Private IP를 이용하여 통신해 네트워크 연결을 빠르고 효율적으로 처리합니다.
- **Web Application Firewall**이 봇, 악성 사용자, SQL injection 공격을 막고 로그를 남겨줍니다.
</details>

<details>
  <summary> 모니터링 및 로깅 </summary>
- Prometheus + Grafana / Fluentd를 VPC 내의 EC2에 각각 도커를 이용해 모니터링-로깅 전용 서버를 구축하였습니다.
- 메인 애플리케이션과 Private IP로 통신하므로, 모니터링 정보(exporter)와 로그(logback) 정보 전달 속도가 빠릅니다.
</details>

</details>

<details>
  <summary> CI/CD Pipeline </summary>

<details>
  <summary> 코드 병합 및 CI 테스트 </summary>

- 배포용 브랜치에 **코드가 병합**되면, **GitHub Actions**를 사용하여 **CI 테스트**를 자동으로 실행합니다.
- 이 단계에서 **코드의 안정성**을 검증하여, 문제가 있는 코드는 배포되지 않도록 합니다.
</details>

<details>
  <summary> 도커 이미지 빌드 및 푸시 </summary>

- 테스트가 **성공적으로 통과**하면, **도커 이미지**를 빌드합니다.
- 빌드된 도커 이미지는 **Docker Hub**에 **푸시**되어, 이후의 배포 과정에서 사용할 수 있도록 준비됩니다.
</details>

<details>
  <summary> CD 단계 (배포 자동화) </summary>

- **CD** 단계에서 **AWS의 Auto Scaling Group**을 활용하여 **롤링 업데이트**를 트리거합니다.
- 롤링 업데이트는 서비스의 가용성을 유지하며 **새로운 버전의 애플리케이션**을 **점진적으로 배포**합니다. 새로운 인스턴스가 완전히 배포되고 헬스체크가 완료되면 구버전 인스턴스를 삭제합니다.
- 시작 템플릿에서 Shell Script를 작성해 완벽히 자동화 했습니다.
</details>

<details>
  <summary> 무중단 배포 </summary>

- **무중단 배포**(Zero-Downtime Deployment) 방식이 적용되어, 서비스의 **다운타임 없이** 배포가 진행됩니다.
- 롤링 업데이트 방식 선택: 별다른 설정 없이 ASG로 편리하게 사용 가능합니다.
- 블루-그린과 비교했을 때 배포 도중 트래픽이 일시적으로 몰리는 현상이 없어 서버 관리 비용 부담이 덜하지만, 배포 중에 신버전과 구버전이 혼재합니다. 하지만 저희 서비스에서는 API가 크게 바뀔 상황이 많지 않고, 모니터링-로깅을 구축해 혹시 모를 상황에 대비하도록 설계했습니다.
- 이를 통해 사용자에게 영향을 주지 않고, 새로운 버전의 애플리케이션을 안전하게 배포할 수 있습니다.
- 새로운 버전에 이상이 있을 때에도 버전 롤백을 통해 기존 버전을 그대로 사용할 수 있습니다.
</details>
</details>


### 📝 **Wireframe**
![스크린샷 2025-03-04 오후 12 49 30](https://github.com/user-attachments/assets/a414c1f1-2d7b-4124-9b70-6b1b7442e48d)

### 💬 **ERD**

```mermaid
erDiagram
    search_history {
        BIGINT id PK
        BIGINT user_id FK
        varchar(100) name
        datetime(6) created_at
    }
    user {
        BIGINT id PK
        varchar(100) email
        varchar(50) name
        int career
        varchar(255) password
        enum role
        int age
        boolean is_notification_enabled
    }
    notification {
        BIGINT id PK
        varchar(255) email
        varchar(255) job_opening_url
        boolean is_email_sent
        boolean is_push_sent
    }
    banned_email {
        varchar(100) email PK
        varchar(100) message
        datetime(6) created_at
    }
       keyword {
        BIGINT id PK
        varchar(50) name
    }
    user_keyword {
        BIGINT id PK
        BIGINT user_id FK
        BIGINT keyword_id FK
    }
    bookmark {
        BIGINT id PK
        BIGINT user_id FK
        BIGINT job_opening_id FK
    }
    job_opening_keyword {
        BIGINT id PK
        BIGINT keyword_id FK
        BIGINT job_opening_id FK
    }
    job_opening {
        BIGINT id PK
        varchar(255) title
        varchar(255) company
        varchar(255) location
        int salary
        enum employment_type
        varchar(255) job_opening_url
        datetime(6) hiring_start_at
        datetime(6) hiring_end_at
        enum education_level
        varchar(255) position
        int view_count
        int max_experience_years
        int min_experience_years
        datetime(6) created_at
    }

    user ||--o{ search_history : searched_by
    user ||--o{ job_opening_bookmark : bookmarked
    user ||--o{ user_keyword : subscribes_to
    job_opening ||--o{ bookmark : has_bookmarks
    job_opening ||--o{ job_opening_keyword : has_keywords
    keyword ||--o{ job_opening_keyword : relates_to
    keyword ||--o{ user_keyword : subscribes_to
```

## 📑 **API 명세서**
- [API 명세서](https://docs.google.com/spreadsheets/d/1CQm7sV-ETn0w-FFe7nOCRKrPIp3i5TQWhKw9B1IDT9s/edit?gid=0#gid=0)

## 🎬 **보러 가기**
- [최종 발표 PPT](https://www.canva.com/design/DAGhg82nibo/w6wIMRrulPlgRl9aj5KqSA/view?utm_content=DAGhg82nibo&utm_campaign=designshare&utm_medium=link2&utm_source=uniquelinks&utlId=haeeb4a3bb7)
- [최종 발표 시연 영상]()
- [팀 브로슈어](https://www.notion.so/3-Cheer-ha-1b300cdad765800e9582c3251531bde9)


---

</aside>

<aside>

## 🛠️ 기술 스택                                                                          

**🖥 Language**    
![Java](https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white) ![Kotlin](https://img.shields.io/badge/kotlin-7f52ff?style=for-the-badge&logo=kotlin&logoColor=white)


**📲 Interface Description Language** 
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ_IDEA-000000?style=for-the-badge&logo=intellijidea&logoColor=white)

                                                                                                    
**🧑🏻‍💻 Backend** 
![Spring Framework](https://img.shields.io/badge/Spring_Framework-6DB33F?style=for-the-badge&logo=spring&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)

 
**🗃 Data Base & Optimization**
![MySQL](https://img.shields.io/badge/MySQL-00758F?style=for-the-badge&logo=mysql&logoColor=white) ![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white) ![Elasticsearch](https://img.shields.io/badge/Elasticsearch-005571?style=for-the-badge&logo=elasticsearch&logoColor=white)

 
**🔐 Security** 
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white)


**🚢 Deployment & Distribution** 
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white) ![AWS](https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white) ![EC2](https://img.shields.io/badge/EC2-FF6C37?style=for-the-badge&logo=amazon&logoColor=white) ![Route 53](https://img.shields.io/badge/Route_53-365E02?style=for-the-badge&logo=amazon&logoColor=white) ![Application Load Balancer](https://img.shields.io/badge/Application_Load_Balancer-00A1E4?style=for-the-badge&logo=amazon&logoColor=white) ![ASG](https://img.shields.io/badge/ASG-00B4D6?style=for-the-badge&logo=amazon&logoColor=white) ![RDS](https://img.shields.io/badge/RDS-FF6C37?style=for-the-badge&logo=amazon&logoColor=white) ![ElasticCache](https://img.shields.io/badge/ElasticCache-FF9900?style=for-the-badge&logo=amazon&logoColor=white) ![Certificate Manager](https://img.shields.io/badge/Certificate_Manager-FF9900?style=for-the-badge&logo=amazon&logoColor=white)  ![Github Actions](https://img.shields.io/badge/Github_Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white) ![Elastic Cloud](https://img.shields.io/badge/Elastic_Cloud-005571?style=for-the-badge&logo=elasticsearch&logoColor=white) ![Ubuntu Linux](https://img.shields.io/badge/Ubuntu-263238?style=for-the-badge&logo=ubuntu&logoColor=white)  ![WAF](https://img.shields.io/badge/WAF-000000?style=for-the-badge&logo=cloudflare&logoColor=white) 

 
**📟 Test**
![JMeter](https://img.shields.io/badge/JMeter-D20C0E?style=for-the-badge&logo=jmeter&logoColor=white) ![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white) 

 
**👥 Collaboration Tool** 
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white) ![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white) ![Jira](https://img.shields.io/badge/Jira-0052CC?style=for-the-badge&logo=jira&logoColor=white) ![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white) ![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white) ![Figma](https://img.shields.io/badge/Figma-000000?style=for-the-badge&logo=figma&logoColor=white) ![Canva](https://img.shields.io/badge/Canva-00C4CC?style=for-the-badge&logo=canva&logoColor=white) ![ERD Cloud](https://img.shields.io/badge/ERD_Cloud-00B8D9?style=for-the-badge&logo=cloud&logoColor=white)


**📧 Notification Service**
![SendGrid](https://img.shields.io/badge/SendGrid-00B3E6?style=for-the-badge&logo=sendgrid&logoColor=white) 


**📈 Logging & Monitoring & Analytics** 
![Kibana](https://img.shields.io/badge/Kibana-005571?style=for-the-badge&logo=kibana&logoColor=white) ![Prometheus](https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=prometheus&logoColor=white) ![Grafana](https://img.shields.io/badge/Grafana-F46800?style=for-the-badge&logo=grafana&logoColor=white) ![Fluentd](https://img.shields.io/badge/Fluentd-2B64B0?style=for-the-badge&logo=fluentd&logoColor=white) 

---

</aside>

<aside>
  
## 🔧 기술적 의사결정

### **💡** 사용자 인증 및 인가를 어떤 방식으로 해야 할까요?

<details>
  <summary> JWT만 사용하기로 했습니다! 그런데 어떻게 해야 안전하게 사용할 수 있을까요? </summary>

  ### 1. 배경

  세션 방식은 다중 인스턴스 환경에서 관리가 까다롭고, 확장성 측면에서 트레이드오프가 존재했습니다.

  세션이 주는 이점 중에는 ‘사용자의 상태 관리가 가능하다는 점’ 이 있는데, 채용 공고 조회 서비스에서는 불필요하다고 느껴졌습니다.

  따라서 현재 프로젝트의 인증 방식으로 JWT만을 사용하고 있고, 이 방식은 보안과 상태 유지-변경에 허점이 존재합니다.
    
  ---

  ### 2. 요구사항

- **보안:** 토큰 탈취 방지 및 안전한 저장 방식 필요
- **성능:** 인증 처리 속도 최적화 및 데이터베이스 서버 부하 최소화
- **상태 변경에 취약함:** 무상태 인증 방식이므로 로그아웃 및 세션 무효화가 어려움

    ---

  ### 3. 구현 전략

 1. **AccessToken의 만료시간을 짧게 설정하고, 로그아웃 시 BlackList를 Redis에 저장함**
 2. **RefreshToken을 Redis에서 관리하고, Rotation 전략을 사용함**
 3. **Token의 Payload를 AES256으로 암호화**
 4. **RefreshToken을 HttpOnly Cookie에 저장**

    ---

  ### 4. 구현 상세

  ![jwt 이미지](https://github.com/llRosell/sparta/blob/main/jwt.png?raw=true)

  1. **JWT 사용 방식**
      - AccessToken의 유효기간을 짧게 설정하여, 토큰 탈취 시 피해를 최소화 (30분)
      - RefreshToken을 사용하여 재발급 로직을 적용해 장기적으로 인증이 유지되게 함 (7일)
      - RefreshToken을 Redis에 저장하여 DB와의 통신 최소화
  2. **로그아웃 처리**
      - Redis를 활용해 만료된 AccessToken을 BlackList 형식으로 차단
      - RefreshToken을 Redis와 Cookie에서 삭제하여 재발급 방지
  3. **보안 강화**
      - RefreshToken을 HttpOnly & Secure Cookie에 저장하여 XSS공격 방지
      - CSRF 방어를 위해 SameSite=Strict 설정 적용하여 외부 요청에서 쿠키 자동 전송 차단
      - Rotation 전략으로 한번 사용한 RefreshToken은 즉시 만료시켜 재사용 금지, 보안 강화
      - Token의 Payload를 AES256으로 암호화하여 데이터 보호 및 변조 방지
      - HTTPS를 강제 적용해 네트워크에서 토큰 유출 방지

       | **Refresh Token 저장 위치** | ❌ **Authorization Header** | ✅ **HttpOnly Cookie** |
       |-------------------------|----------------------------|-----------------------|
       | **XSS 공격**              | ❌ 취약함 (JavaScript 접근가능)    | ✅ 안전함 (Http Only)     |
       | **CSRF 공격**             | ✅ 안전함 (자동 전송 X)            | ❌ 취약함 (브라우저 자동 전송)    |
       | **클라이언트 편의성**           | ❌ 불편함 (헤더에 직접 포함해야 함)      | ✅ 편리함 (브라우저 자동 전송)    |

 ---

  ### 5. 향후 고려 사항

  - **Spring Security 연결 검토**: 현재는 OAuth 연동이 어렵고 추가 설정이 필요하여 까다로움
  - **JWT Secret Key Rotation 전략 검토**: 정기적으로 암호화에 사용되는 key를 변경
  - **RefreshToken에 고유 정보 추가 검토**: AccessToken이 함부로 재발급되지 않게 하는 전략 검토
</details>

### **💡 원하는 채용 공고만 간편하게 검색할 수는 없을까요?**

<details>
  <summary> MySQL vs Elasticsearch, 어떤 기술을 적용해야 할까요? </summary>
 
  ### 1. 배경

  MySQL 기반으로 검색 시 **`LIKE`** 연산자와 패턴 매칭을 사용하는 비정형 검색에서 성능 저하가 발생했습니다.

  한글 텍스트 검색과 관련된 **`Full Table Scan`**이 문제였고, 이는 검색 속도와 TPS에 큰 영향을 미쳤습니다.

  검색 기능을 개선하고자 한글 텍스트 처리 가능 여부, 검색 성능 같은 요구사항을 고려하여 기술적 의사결정을 내렸습니다.
    
  ---

  ### 2. 요구사항

  - **검색 성능**: 대규모 데이터셋에 대한 빠르고 효율적인 검색을 제공해야 함
  - **한글 텍스트 처리**: 한글 형태소 분석을 통한 정확한 검색 필요
  - **확장성**: 서버의 부하를 분산하고 대규모 트래픽도 원활하게 처리하는 수평 확장이 가능해야 함
  - **자동 완성 및 부분 일치**: 사용자 경험을 향상시킬 수 있는 기능 구현이 가능해야 함
  - **성능 최적화**: 검색 성능을 최적화하여 효율적인 데이터 처리 및 빠른 응답 시간을 보장해야 함

    ---

  ### 3. 고려한 대안

  | **비교 항목** | **MySQL**                 | **Elasticsearch**                |
  | --- |---------------------------|----------------------------------|
  | **검색 성능** | ❌ 비효율적, `Full Table Scan` | ✅ 빠르고 효율적, 인덱스 기반                |
  | **한글 텍스트 처리** | ❌ 비효율적 (형태소 분석 부족)        | ✅ `Nori Analyzer`로 가능함           |
  | **확장성** | ❌ 수직 확장만 가능               | ✅ 분산 처리로 수평 확장이 가능함              |
  | **자동 완성 및 부분 일치** | ❌ 어려움                     | ✅ 가능                             |
  | **성능 최적화** | ❌ 제한적                     | ✅ `Ngram`, `Edge-Ngram`으로 최적화 가능 |
    
  ---

  ### 4. 결정 및 근거

  **✅ Elasticsearch 선택**

  - 분산형 아키텍처와 인덱싱을 활용해 빠르고 효율적인 검색 성능을 제공함
  - **`Nori Analyzer`**로 한글 형태소 분석이 가능함

    ---

  ### 5. 영향

  - **긍정적 영향**
      - **검색 속도 및 처리량 개선 →** 사용자 경험이 향상
      - **수평 확장 →** 향후 증가할 수 있는 트래픽에 효율적으로 대응 가능
      - **정확한 검색 및 한글 텍스트 처리 →** 사용자가 원하는 정보를 더 빠르고 정확하게 찾을 수 있음
  - **부정적 영향**
      - **초기 학습 및 설정 필요 →** 시간이 소요됨
      - **Elasticsearch의 복잡한 구조 →** 이해가 필요

    ---

  ### 6. 구현 전략

  1. **Elasticsearch 클러스터 설정 및 최적화**
      - 분산형 클러스터를 설정하여 데이터 처리 성능을 최적화
      - 인덱싱 및 검색 쿼리를 최적화하여 성능을 개선
 2. **검색 성능 테스트 및 최적화**
      - JMeter를 사용하여 MySQL vs Elasticsearch 성능 테스트 진행
<details>
    <summary> 테스트 환경 펼치기 </summary>
              - Elasticsearch 버전: 8.17.2
              - QueryDSL: MySQL에서 데이터를 직접 조회하여 처리
              - 테스트 도구: Apache JMeter
              - 테스트 요청: HTTP 요청 (GET)
              - 동시 요청: 200개 (100개씩 총 2번 요청)
              - 서버 환경: 로컬 서버 (localhost)
              - 클라이언트 환경: JMeter 클라이언트
</details>

<details>
    <summary> 테스트 결과 펼치기 </summary> 

| **테스트 항목** | **MySQL 조회(QueryDSL)** | **Elasticsearch 조회** | **성능 향상** |
| --- | --- | --- | --- |
| **평균 응답 시간** | **48ms** | **14ms** | **70.83%** |
| **TPS** | **8.63/sec** | **9.5/sec** | **+10.1%** |
| **최소 응답 시간** | 39ms | 9ms | 77% |
| **최대 응답 시간** | 117ms | 41ms | 65% |
| **표준편차** | 9.37ms | 3.28ms | 64% |
| **수신량 (KB/sec)** | 8.63KB | 65.01KB | +653.5% |
| **전송된 데이터 (KB)** | 0.57KB | 3.94KB | +591.2% |
| **평균 바이트 (Byte)** | 6300.9 Byte | 6971.9 Byte | +10.7% |
</details>

  <details>
    <summary> 테스트 결과 분석 펼치기 </summary> 

- MySQL:
  - 전통적인 디스크 기반 데이터 저장 방식
  - 대규모 데이터 조회 시 상대적으로 속도가 느림
- Elasticsearch:
  - 분산형 검색엔진
  - 데이터를 인덱싱하고 검색하는 데 최적화됨
- 대규모 데이터를 MySQL보다 빠르게 처리할 수 있음 
  </details>

 - 성능 데이터를 기반으로 추가 최적화 작업을 진행 

    ---
    
    ### 7. 향후 고려 사항
    
    - **Elasticsearch 튜닝 및 데이터 최적화:** 쿼리 및 인덱스 최적화, 데이터 정제 작업으로 성능 개선
</details>

<details>
    <summary> 기능을 완성하기까지 또 어떤 고민이 있었을까요? </summary>

<details>
    <summary> `n-gram` vs `Nori`, 부분 일치 검색 기능에는 어떤 분석기가 더 적합할까요? </summary>

### 1. 배경

   기존 검색 기능은 완전 일치 기반으로 동작했기 때문에, 검색어를 정확히 입력해야만 결과가 반환되었습니다.
   일부 단어만 입력하면 검색이 제대로 되지 않았습니다.
   검색의 유연성이 부족하고 사용자 편의성이 낮았습니다.
   이러한 문제를 해결하고자 검색 방식, 검색 정확도 같은 요구사항을 고려하여 기술적 의사 결정을 내렸습니다.
        
---

### 2. 요구사항

- **부분 검색 가능:** 부분 일치 검색 기능을 지원해야 함
- **한글 텍스트 처리 가능:** 한글 특성에 맞게 단어 또는 형태소 단위로 텍스트를 분석할 수 있어야 함
- **높은 검색 정확도:** 단어 일부만 입력했을 때 관련성이 높은 결과를 우선 반환해야 함

 ---

### 3. 고려한 대안

| **비교 항목** | **`n-gram` 분석기** | **`Nori` 분석기** |
| --- | --- | --- |
| **검색 방식** | 단어를 작은 조각으로 나누어 검색 | 형태소 단위로 분석하여 검색 |
| **부분 검색 가능** | ✅ 가능 | ❌ 불가능 |
| **한글 텍스트 처리 가능** | ❌단어를 일정한 크기의 연속된 문자로 쪼개어 분석하므로 부적합 | ✅형태소 단위로 분석하므로 적합 |
| **검색 정확도** | ❌불필요한 검색 결과가 포함되어 정확도가 떨어짐 | ✅의미 단위로 분석하므로 정확도가 높음 |
| **색인 크기** | ❌단어 조각이 많아져 크기가 큼 | ✅비교적 작음 |
| **적합한 상황** | 부분 검색, 자동 완성 | 의미 기반 검색, 자연어 검색 |
        
---

### 4. 결정 및 근거

✅ **`Nori`** 분석기 선택

- 단순 문자 조합으로 검색하는 **`n-gram`** 분석기와 달리, 의미 단위 검색이 가능함
- 한글의 특성을 고려하여 보다 자연스러운 검색 결과 제공 가능
- 색인 크기를 줄이면서 검색의 정확도를 유지할 수 있음

---

### 5. 영향

- **긍정적 영향**
     - **검색 정확도 개선 →** 형태소 분석을 활용하여 보다 정교한 검색 가능
     - **사용자 편의성 증가 →** 단어 일부 입력만으로 검색 가능
     - **색인 효율 개선 →** **`n-gram`** 보다 색인 크기가 줄어들어 불필요한 데이터 저장을 방지할 수 있음
- **부정적 영향**
     - **처리 속도 저하 가능 →** 형태소 분석 과정이 추가되며 검색 속도가 느려질 수 있음
     - **설정 최소화 필요 →** 분석 결과가 예상대로 나오지 않을 시 추가 설정 필요

---

### 6. 구현 전략

1. **한글 형태소 분석기 적용**
    - **`Nori Analyzer`**를 활용하여 한글 텍스트를 형태소 단위로 분석
    - **`Nori Analyzer`** 설정 JSON 코드

   ```json
   PUT job-opening
   {
     "settings": {
       "analysis": {
         "filter": {
           "edge_ngram_filter": {
             "type": "edge_ngram",
             "min_gram": 1,
             "max_gram": 10
           }
         },
         "analyzer": {
           "nori_analyzer": {
             "type": "custom",
             "tokenizer": "nori_tokenizer",
             "filter": ["lowercase"]
           },
           "autocomplete_analyzer": { 
             "type": "custom",
             "tokenizer": "standard",
             "filter": ["lowercase", "edge_ngram_filter"]
           }
         }
       }
     },
     "mappings": {
       "properties": {
         "_class": {
           "type": "keyword",
           "index": false,
           "doc_values": false
         },
         "company": { 
           "type": "text",
           "fields": {
             "morph": {
               "type": "text",
               "analyzer": "nori_analyzer"
             },
             "autocomplete": {
               "type": "text",
               "analyzer": "autocomplete_analyzer"
             }
           }
         },
         "title": {  
           "type": "text",
           "fields": {
             "morph": {
               "type": "text",
               "analyzer": "nori_analyzer"
             },
             "autocomplete": {
               "type": "text",
               "analyzer": "autocomplete_analyzer"
             }
           }
         }
       }
     }
   }
   ```
   
</details>
    
<details>
    <summary> 인덱싱 vs 캐싱, 즐겨찾기한 채용 공고를 더 빠르게 조회하는 방법은 무엇일까요? </summary> 
        
---
        
### 1. 배경
        
기존 MySQL 기반의 채용 공고 검색은 북마크 조회 성능에 제약이 있었습니다. 
        
특히 반복적인 조회에서 데이터베이스 부담이 컸고 빠른 조회가 어려웠습니다. 
        
이러한 문제를 해결하고자, MySQL 인덱싱과 Redis 캐싱을 비교하여 기술적 의사결정을 내렸습니다.
        
---
        
### 2. 요구사항
        
- **성능 향상:** 빠른 조회가 가능해야 함
        
---
        
### 3. 고려한 대안
        
| **비교 항목** | **MySQL** | **Redis** |
| --- | --- | --- |
| **인덱싱 제공 여부** | ✅ 인덱스 기능 제공 | ❌ 인덱싱 기능 없음 |
| **캐싱 제공 여부** | ❌ 캐싱 미제공 | ✅ 캐싱 제공 |
| **성능 향상** | ✅ 인덱스 적용 시 향상 가능 | ✅ 캐싱 적용 시 향상 가능 |
        
---
        
### 4. 결정 및 근거
        
**✅MySQL 인덱싱과 Redis 캐싱 함께 사용**
        
- 함께 사용하여 레디스와 데이터베이스의 과부하 방지
         - 즐겨찾기 1페이지: 가장 많이 조회되므로 Redis 캐싱 적용
         - 즐겨찾기 2페이지 이후: MySQL 인덱싱 적용
- 참고) 인덱싱 vs Redis캐싱 성능 비교
<details>
    <summary> 테스트 환경 </summary> 
              - 테스트 도구: Apache JMeter
               - 테스트 요청: HTTP 요청 (GET)
               - 동시 요청: 200개 (10초에 100개씩 총 2번 요청)
               - 서버 환경: 로컬 서버 (localhost)
               - 클라이언트 환경: JMeter 클라이언트
</details>

<details>
    <summary> 성능 비교 </summary> 
                
                
| **테스트 항목** | **MySQL 조회 (인덱싱)** | **Redis 캐싱** | **조회 성능 향상률** |
| --- | --- | --- | --- |
| **평균 응답 시간** | 28ms | 12ms | 62.5% 향상 |
| **최소 응답 시간** | 17ms | 6ms | 64.71% 향상 |
| **최대 응답 시간** | 291ms | 37ms | 87.32% 향상 |
| **표준편차** | 26.12ms | 4.26ms | 83.69% 향상 |
| **TPS** | 9.5 요청/초 | 9.5 요청/초 | - |
| **수신량**  | 29.70KB | 28.28KB | 4.78% 감소 |
| **전송량** | 3.76KB | 3.75KB | 0.27% 감소 |
| **평균 바이트** | 3208.9 Byte | 3060.0 Byte | 4.64% 감소 |
</details>

- 테스트 환경 및 결과 분석

---
        
### 5. 영향
        
- **긍정적 영향**
      - Redis 캐싱 → 1페이지 조회에서 빠른 응답을 제공 / 데이터베이스 부하 감소
      - MySQL 인덱싱 → 효율적인 데이터 조회
      - 성능 향상 → 평균 응답 시간이 62.5% 향상
- **부정적 영향**
      - 캐시 유지 관리 → 추가적인 TTL 설정 및 캐시 갱신 전략 필요
      - 캐시와 데이터베이스 간 일관성 유지 → 데이터 변경 시 캐시를 갱신하는 방법 구현 필요
        
---
        
### 6. 구현 전략
        
1. **1페이지 조회 시 Redis 캐싱을 적용하여 빠른 응답 제공**
- 페이지네이션에 적용하여 데이터 10개만 반환
2. **2페이지 조회부터는 MySQL의 인덱싱을 활용하여 데이터베이스에서 효율적으로 조회**
- 인덱싱을 적용하여 대규모 데이터 조회 시 Full Table Scan 방지
        
---
        
### 7. 향후 고려 사항
        
- **캐시 일관성 문제 해결:** 데이터베이스와 캐시 간 일관성 문제를 해결할 방법 고안
</details>
</details>

### **💡원하는 기술 요건이 담긴 채용 공고만 이메일로 받을 수는 없을까요?**

<details>
    <summary> @Async vs ThreadPoolTaskScheduler, 어떤 방법으로 비동기 처리를 할까요? </summary>

  ### 1. 배경

  이메일 알림 발송 작업을 비동기로 처리하여 발송 속도를 높이고, 다른 작업에 영향을 주지 않도록 하고 싶었습니다.

  리소스 점유 정도, 사용 편의성, 관리 난이도를 검토하여 기술적 의사결정을 내렸습니다.
    
  ---

  ### 2. 요구사항

  - **리소스 점유 최소화**: 이메일 알림 기능이 다른 작업과 스레드 자원을 공유하지 않아야 함
  - **쉬운 관리**: 스레드 풀을 간단하게 관리할 수 있어야 함
  - **사용 편의성**: 설정이나 관리가 직관적이고 복잡하지 않아야 함

    ---

  ### 3. 고려한 대안

  | **비교 항목** | **@Async 어노테이션** | **ThreadPoolTaskScheduler** |
  | --- | --- | --- |
  | **리소스 점유 정도** | ❌ 별도 설정이 없을 시 기본 스레드 풀 사용 | ❌ 기본 스레드를 덮어쓰거나 공유 |
  | **관리 난이도** | ✅ 설정이 간단하고 스프링이 관리 | ❌ 별도의 스케줄러 관리 및 설정 필요 |
  | **사용 편의성** | ✅ 직관적이고 간편한 설정 | ❌ 설정과 관리가 다소 복잡할 수 있음 |
  | **확장성** | ❌ 대량 발송 시 성능이 저하될 수 있음 | ✅ 대량 발송에 적합하고 안정적임 |
  | **단점** | ⚠️ 대량 발송 시 제약이 있을 수 있음 | ⚠️ 초기 설정이 복잡하고 관리가 어려움 |
    
  ---

  ### 4. 결정 및 근거

  ✅ **`@Async` 어노테이션 선택**

  - 별도의 스레드 풀 설정 없이 간편하게 비동기 적용 가능
  - 스프링이 내부적으로 스레드 풀을 관리하여 관리 부담이 적음
  - 다른 비동기 작업에도 어노테이션을 사용해서 쉽게 비동기 처리 가능

    ---

  ### 5. 영향

- **긍정적 영향**
    - **빠른 응답 속도** → 비동기 처리로 이메일 발송 작업을 빠르게 처리할 수 있음
    - **확장성 보장** → 여러 비동기 작업을 쉽게 처리하며, 시스템 확장 시 유연하게 대응 가능
    - **간편한 관리** → 별도로 스레드 풀을 설정하거나 관리할 필요 없음
- **부정적 영향**
    - **스레드 리소스 낭비** → 별도로 스레드 풀을 정의하지 않아서 매번 새로운 스레드 생성
    - **성능 저하 가능성 있음** → 스레드를 매번 새로 생성해서 대량 발송 시 성능이 저하될 수 있음

    ---

  ### 6. 구현 전략

    1. **Application에 `@EnableAsync` 적용**
    2. **이메일 알림 발송 메서드에 `@Async` 적용**
        - sendNotificationEmails() 펼치기

            ```java
            @Slf4j
            @Service
            @RequiredArgsConstructor
            public class NotificationEmailSender {
            
                private final NotificationRepository notificationRepository;
                private final EmailSender emailSender;
            
                **@Async**
                public void sendNotificationEmails() {
                    // key: 알림 받을 이메일, value: Notification Set
                    Map<String, Set<Notification>> emailToNotificationSet = new HashMap<>();
            
                    // (1) 이메일로 전송되지 않은 알림 객체(Notification) 조회
                    // (2) 이메일별로 해당 알림 집합(Notification Set)을 그룹화하여 저장
                    notificationRepository.findByIsEmailSentFalse().forEach(
                        notification -> emailToNotificationSet
                            .computeIfAbsent(
                                notification.getEmail(),
                                emailAsKey -> new HashSet<>()
                            ).add(notification)
                    );
            
                    // 각 이메일로 알림 발송
                    emailToNotificationSet.forEach(this::sendNotificationEmail);
                }
            }
            ```


  ---
    
  ### 7. 향후 고려 사항
    
  - **스레드 풀 명시적 설정**: 명시적으로 스레드 풀을 설정하여 스레드 재사용
  - **대량 발송 최적화**:
      - 서비스 내 모든 작업의 우선순위를 파악하여 특정 작업이 스레드 풀을 독점하지 않도록 설정
      - 테스트를 거쳐 이메일 대량 발송에 적합한 스레드 개수를 찾아 성능 최적화
</details>

<details>
    <summary> 기능을 완성하기까지 또 어떤 고민이 있었을까요? </summary>

<details>
    <summary> 기능을 3일 안에 구현하려면 어떤 서비스를 사용해야 할까요? </summary>

### 1. 배경

이메일 알림 발송 기능을 3일 안에 구현하여 성능 개선 기간을 더 길게 확보하고자 다양한 이메일 발송 서비스를 비교하였습니다.

각 서비스의 적용 절차, 비용, 사용 편의성을 검토하여 기술적 의사결정을 내렸습니다.

---

### 2. 요구사항

- **간단한 가입 절차**: 복잡한 절차 없이 빠르게 프로젝트에 서비스를 적용할 수 있어야 함
- **비용 무료**: 개발 과정에서 테스트를 많이 할 예정이므로 비용이 발생하지 않아야 함
- **테스트 용이성**: 실제 업무에 사용하지 않고 테스트 환경에서 쉽게 활용할 수 있어야 함
- **사용 편의성**: 설정이나 관리가 직관적이고 복잡하지 않아야 함

---

### 3. 고려한 대안

| **비교 항목** | **Gmail** | **SendGrid** |
| --- | --- | --- |
| **가입 절차** | ✅ 가장 간단함 | ❌ API 키 발급 및 설정 필요 |
| **비용** | ✅ 제한적 무료 | ✅ 제한적 무료 |
| **테스트 용이성** | ✅ 일반 이메일처럼 테스트 가능 | ✅ API 기반 테스트 가능 |
| **사용 편의성** | ✅ 간편한 SMTP 설정 | ❌ API 사용에 추가 설정 필요 |
| **확장성** | ❌ 대량 발송 시 제한될 가능성 있음 | ✅ 대량 발송 가능 |
| **단점** | ⚠️ 대량 발송 시 차단 위험 | ⚠️ 초기 설정이 복잡함 |
        
---

### 4. 결정 및 근거

✅ **초기에는 `Gmail` 선택, 기능 구현 이후 `SendGrid`로 전환**

- 프로젝트 일정 초기에 이메일 알림 발송 기능 구현 시간을 줄일 수 있음
- 추후 문제가 발생하더라도 기존 기능을 유지한 채 서비스를 전환하여 서비스 중단 없이 개선 가능

---

## 5. 영향

- **긍정적 영향**
    - **사용 편의성 및 테스트 용이성 →** 개발 속도 증가 및 빠르게 기능 적용 가능
    - **기능 개선 시 서비스 유지 가능 →** 서비스 중단 없이 기능 개선 가능
- **부정적 영향**
    - **외부 API 의존 →** API에서 문제 발생 시 해결이 어려울 수 있음
    - **API 적용 러닝 커브 →** 초기 설정을 하는 데 시간이 필요함

---

### 6. 구현 전략

   1. **`Gmail`**로 활용하여 이메일 알림 발송 기능 구현
   2. **`SendGrid`**로 전환

---

### 7. 향후 고려 사항

   - **대량 발송 처리 강화**:  **`Amazon SES` ,** **`Mailgun`** 같은 고성능 대량 발송 서비스 검토
   - **도메인 사용**: 발신자를 기존의 Gmail 서버가 아니라 도메인으로 적용하는 가능성 검토
</details>

<details>
    <summary> 데이터베이스에는 어떤 알림 정보를 저장해야 할까요? </summary>

### 1. 배경

이메일 알림 기능을 구현하면서 두 가지 주요 고민이 있었습니다.

첫째, 이메일 발송에 실패했을 때 해당 알림을 재발송할 방법을 찾아야 했습니다.

둘째, 이미 발송된 알림이 중복으로 발송되지 않도록 처리해야 했습니다.

따라서 이러한 고민을 해결할 수 있도록 데이터베이스 구조를 설계하였습니다.
        
---

### 2. 요구사항

- **발송 상태 추적 시 필요**: 알림의 발송 상태를 추적할 때 필요해야 함
- **재발송 처리 시 필요**: 발송에 실패한 알림을 다시 보낼 때 필요해야 함
- **영속성 여부**: 데이터베이스에 저장되어야 할 필요가 있어야 함
- **확장성**: 이메일 외에 다른 형태로 알림을 발송할 때도 활용할 수 있어야 함

---

### 3. 고려한 후보

| **후보 항목** | **발송 상태 추적 시** | **재발송 시** | **영속성 여부** | **확장성** |
| --- | --- | --- | --- | --- |
| **isSent** | ✅ 필요 | ✅ 필요 | ❌ 발송 후 저장할 필요 없음 | ✅ 다양한 알림에 적용 가능 |
| **jobOpeningUrl** | ✅ 필요 | ✅ 필요 | ❌ 발송 후 저장할 필요 없음 | ✅ 다양한 알림에 적용 가능 |
| **email** | ✅ 필요 | ✅ 필요 | ❌ 발송 후 저장할 필요 없음 | ✅ 다양한 알림에 적용 가능 |
| **readAt** | ❌ 선택 | ❌ 선택 | ✅ 기능의 유용성 확인 시 필요 | ✅ 다양한 알림에 적용 가능 |
        
---

### 4. 결정 및 근거

✅ **`isSent`  + `jobOpeningUrl` + `email` 선택**

- 발송 상태를 추적하고 발송 실패 시 재발송 시도 가능
- 동일한 정보를 가진 알림이 중복으로 발송되는 일 방지 가능
- 데이터베이스에 저장된 알림 정보로 다른 유형의 알림을 보내는 등 재사용 가능

---

### 5. 영향

- **긍정적 영향**
    - **중복 발송 방지 →** 같은 알림이 중복으로 발송되지 일을 방지할 수 있음
    - **재발송 가능 →** 데이터베이스에 저장된 알림 정보로 발송 실패 시 재발송 가능
    - **확장성 보장** **→** 데이터베이스에 저장된 정보을 푸시 같은 다른 방식으로 발송 가능
- **부정적 영향**
    - **데이터베이스 용량 증가 →** 알림 정보를 저장할수록 데이터베이스 사용량이 커짐
    - **성능 저하 가능성 있음 →**  저장된 데이터양이 늘어날수록 성능이 저하될 수 있음
    - **복잡성 증가 →** 알림 발송 상태 추적 및 재발송 로직이 추가되며 시스템이 복잡해짐

---

### 6. 구현 전략

1. **`email`** + **`jobOpeningUrl`** + **`isEmailSent`를 추가하고 알림 객체 생성**
    - 이메일 알림 관련 정보를 객체 속성으로 설정
    - 알림 정보를 관리하는 **`Notification`** 클래스 생성
    - **`uniqueContraints`** 설정

      → **`email`**과 **`jobOpeningUrl`** 조합이 중복으로 저장되지 않도록 처리

    - **`isEmailSent`** 기본값은 false로 설정

2. **`isPushSent` 속성 추가**
    - 기본값은 false로 설정

3. **`isEmailSent` 및 `isPushSent` 값을 false에서 true로 변경하는 메서드 추가**
    - Notification 클래스 펼치기

      ```java
      @Entity
      @Getter
      @NoArgsConstructor
      @Table(
          name = "notification",
          uniqueConstraints = {@UniqueConstraint(
              columnNames = {"email", "job_opening_url"}
          )}
      )
      public class Notification {
 
          @Id
          @GeneratedValue(strategy = GenerationType.IDENTITY)
          private Long id;
 
          @Column(nullable = false)
          private String email;
 
          @Column(nullable = false)
          private String jobOpeningUrl;
 
          @Column
          private boolean isEmailSent;
 
          @Column
          private boolean isPushSent;
 
          public static Notification toEntity(
              String email,
              String jobOpeningUrl
          ) {
              Notification notification = new Notification();
              notification.email = email;
              notification.jobOpeningUrl = jobOpeningUrl;
              notification.isEmailSent = false;
              notification.isPushSent = false;
              return notification;
          }
 
          public void markEmailAsSent() {
              this.isEmailSent = true;
          }
 
          public void markPushAsSent() {
              this.isPushSent = true;
          }
      }
      ```

<details>
  <summary> Notification DDL 펼치기 </summary> 

  ```sql
      create table notification
      (
          id              bigint auto_increment
              primary key,
          email           varchar(255) not null,
          is_email_sent   bit          null,
          is_push_sent    bit          null,
          job_opening_url varchar(255) not null,
          constraint UK_email_job_opening_url
              unique (email, job_opening_url)
      );
  ```
</details>

---

### 7. 향후 고려 사항

- **주기적 데이터 삭제**: 일정 기간이 지난 알림 데이터를 정리하여 데이터베이스 용량 관리
- **메시지 큐 도입:** Kafka 또는 RabbitMQ 같은 메시지 큐로 알림을 발송하여 성능 향상
</details>

<details>
    <summary> 한 로직 유지 vs 로직 분리, 어떤 방향으로 나아가야 할까요? </summary>

### 1. 배경

기존에는 스케줄러 하나가 정보 조회와 이메일 알림 발송을 모두 담당했으나, 알림 정보를 저장하면서 로직이 복잡해졌습니다.

앞으로도 기능 개선 과정에서 주기가 다른 로직이 추가될 예정이었으므로, 기능 고도화 작업에 들어갈 시간을 절약해야 했습니다.

이러한 장기 계획을 반영하고자 작업의 독립성, 재사용성, 관리 용이성을 고려하여 기술적 의사결정을 내렸습니다.

---

### 2. 요구사항

- **작업의 독립성**: 이메일 알림을 발송하기까지 필요한 작업이 서로 영향을 주지 않아야 함
- **재사용성**: 푸시 알림 같은 기능을 추가할 예정이므로 로직을 재사용할 수 있어야 함
- **관리 용이성**: 발송 실패 같은 문제 발생 시 원인을 빠르게 찾아 해결할 수 있어야 함

---

### 3. 고려한 대안

| **비교 항목**     | **한 로직 유지** | **로직 분리** |
|------------------|----------------|-------------|
| **작업의 독립성**  | ❌ 작업 주기가 서로 영향을 주기 쉬움 | ✅ 작업이 독립적으로 이루어져서 서로 영향을 주지 않음 |
| **재사용성**       | ❌ 특정 로직만 재사용하기 어려움 | ✅ 원하는 로직만 떼어내어 재사용 가능 |
| **관리 용이성**    | ✅ 한 클래스 안에 모든 로직이 있어서 관리하기 쉬움 | ❌ 로직이 여러 계층으로 분리되어 관리 부담이 늘어남 |
| **단점**           | ⚠️ 작업 주기가 겹치거나 충돌 가능 | ⚠️ 코드 관리 및 디버깅이 어려워짐 |

---

### 4. 결정 및 근거

✅ **로직 분리 선택**

- 이메일 알림 외에 다른 알림 기능을 추가할 때 작업 속도를 줄일 수 있음
- 특정 로직을 수정할 때 전체 로직을 확인할 필요가 없으므로 높은 효율로 처리 가능

---

### 5. 영향

- **긍정적 영향**
    - **작업 효율성 증가 →** 새로운 알림 기능을 추가할 때 기존 로직을 재사용하여 작업 효율을 높일 수 있음
    - **유지보수 용이성 →** 분리된 로직을 독립적으로 관리하여 유지보수가 쉬워짐
- **부정적 영향**
    - **복잡성 증가 →** 여러 계층으로 로직이 분리되어 코드 관리 및 디버깅이 복잡해짐

---

### 6. 구현 전략

1. 알림 생성에 필요한 데이터 조회 전용 스케줄러 분리
    - NotificationDataFetchingTaskHandler 클래스 펼치기

      ```java
      @Component
      @RequiredArgsConstructor
      public class NotificationDataFetchingTaskHandler implements TaskHandler {
 
          private final NotificationService notificationService;
          private final NotificationDataProviderQuery notificationDataProviderQuery;
 
          @Override
          public String getTaskType() {
              return "fetchNotificationData";
          }
 
          // 주기적으로 알림 생성용 데이터 조회
          @Override
          public void handle(Map<String, Object> payload) {
              // 1시간 전 기준으로 데이터 조회 (UTC 기준)
              ZonedDateTime referenceTime = ZonedDateTime.now().minusHours(1L).withZoneSameInstant(ZoneId.of("UTC"));
              // key: 키워드 ID, value: 채용 공고 URL 목록
              Map<Long, List<String>> keywordIdToUrlList = notificationDataProviderQuery.findKeywordIdToUrlList(referenceTime);
              // 알림 받을 사용자의 이메일과 키워드 ID를 포함한 NotificationRecipientDto 목록
              List<NotificationRecipientDto> notificationRecipientDtoList = notificationDataProviderQuery.findNotificationRecipientDtoList();
              // 조회한 알림 생성용 데이터를 전달
              notificationService.createNotification(notificationRecipientDtoList, keywordIdToUrlList);
          }
 
          @Override
          public long getScheduleIntervalMillis() {
              return 3600000L; //1시간
          }
      }
      ```

2. 알림 객체를 생성하여 저장하는 서비스 레이어 분리

<details>
    <summary> NotificationService 클래스 펼치기 </summary>

```java
      @Service
      @RequiredArgsConstructor
      public class NotificationService {
 
          private final NotificationRepository notificationRepository;
 
          /**
           * 알림(Notification) 객체를 생성하여 저장하는 메서드
           * @param notificationRecipientDtoList 수신자의 이메일 및 키워드 ID 목록
           * @param keywordIdToUrlList key: 키워드 ID, value: 채용 공고 URL 목록
           */
          @Transactional
          public void createNotification(
              List<NotificationRecipientDto> notificationRecipientDtoList,
              Map<Long, List<String>> keywordIdToUrlList
          ) {
              Map<String, Set<String>> emailToUrlSet = new HashMap<>();
 
              // 사용자의 키워드 ID를 기반으로 이메일별 채용 공고 URL 매칭
              matchEmailToUrlSetByKeywordId(
                  notificationRecipientDtoList,
                  keywordIdToUrlList,
                  emailToUrlSet
              );
 
              // 이메일별로 알림 객체 생성 및 저장
              emailToUrlSet.forEach((email, urlSet) -> {
 
                  // 기존에 저장된 알림 조회
                  List<Notification> foundNotificationList = notificationRepository.findAllByEmailAndJobOpeningUrlIn(
                      email,
                      urlSet.stream().toList()
                  );
 
                  // 이미 존재하는 채용 공고 URL 추출
                  Set<String> existingUrlSet = findExistingUrlSet(foundNotificationList);
 
                  // 새로운 알림 객체 생성
                  List<Notification> notificationList = createNotificationList(
                      email,
                      urlSet,
                      existingUrlSet
                  );
 
                  // 알림 저장
                  notificationRepository.saveAll(notificationList);
              });
          }
 
          /**
           * 키워드 ID가 동일한 사용자 이메일과 채용 공고 URL을 매칭하는 메서드
           * @param notificationRecipientDtoList 수신자의 이메일 및 키워드 ID 목록
           * @param keywordIdToUrlList key: 키워드 ID, value: 채용 공고 URL 목록
           * @param emailToUrlSet key: 이메일, valye: 해당 이메일 주소로 받을 채용 공고 URL 집합
           */
          private void matchEmailToUrlSetByKeywordId(
              List<NotificationRecipientDto> notificationRecipientDtoList,
              Map<Long, List<String>> keywordIdToUrlList,
              Map<String, Set<String>> emailToUrlSet
          ) {
              for (NotificationRecipientDto dto : notificationRecipientDtoList) {
                  List<String> matchingUrlList = keywordIdToUrlList.getOrDefault(
                      dto.keywordId(),
                      List.of()
                  );
 
                  if (!matchingUrlList.isEmpty()) {
                      emailToUrlSet.computeIfAbsent(
                          dto.email(),
                          email -> new HashSet<>()
                      ).addAll(matchingUrlList);
                  }
              }
          }
 
          /**
           * 기존에 저장된 알림에서 채용 공고 URL 목록을 조회하는 메서드
           * @param notificationList 기존에 저장된 알림 목록
           * @return 기존에 저장된 채용 공고 URL 집합
           */
          private Set<String> findExistingUrlSet(List<Notification> notificationList) {
              return notificationList.stream()
                  .map(Notification::getJobOpeningUrl)
                  .collect(Collectors.toSet());
          }
 
          /**
           * 새로운 알림 목록을 생성하는 메서드
           * @param email 알림을 받을 사용자의 이메일
           * @param urlSet 사용자가 고른 키워드로 매칭된 채용 공고 URL 집합
           * @param existingUrlSet 이미 저장된 채용 공고 URL 집합
           * @return 새로운 알림(Notification) 목록
           */
          private List<Notification> createNotificationList(
              String email,
              Set<String> urlSet,
              Set<String> existingUrlSet
          ) {
              return urlSet.stream()
                  .filter(jobOpeningUrl -> !existingUrlSet.contains(jobOpeningUrl)) // 기존 알림에 없는 URL 필터링
                  .map(jobOpeningUrl -> Notification.toEntity(email, jobOpeningUrl)) // 알림 객체 생성
                  .toList();
          }
      }
```
</details>


3. 데이터베이스에 저장된 알림을 조회하여 이메일로 전송하는 로직 분리

→ 이후 각 로직을 추가로 분리
---

### 7. 향후 고려 사항

- **메시지 큐 도입:** Kafka 또는 RabbitMQ 같은 메시지 큐로 알림을 발송하여 성능 향상
</details>
</details>


### 💡내가 검색한 내역을 빠르게 조회할 수 없을까요?

<details>
    <summary> MySQL 인덱스 vs Redis, 무엇을 적용해야 더 빠르게 조회할 수 있을까요? </summary>

  ### 1. 배경

  기존에는 사용자의 최근 검색어를 MySQL에서 직접 조회하여 제공하였는데, 검색어 조회 요청이 증가하면서 데이터베이스 부하가 커졌습니다.
  특히, 검색어 조회와 추가가 빈번하게 이루어지면서 반복되는 읽기 및 쓰기 작업이 MySQL의 성능을 저하했습니다.

  이러한 성능 저하 문제를 해결하고자, 데이터베이스 부하를 줄이는 방법을 검토하여 기술적 의사결정을 내렸습니다.
    
  ---

  ### 2. 요구사항

  - **사용자 검색어 저장**: 입력한 검색어를 저장하고 빠르게 조회할 수 있도록 관리해야 함
  - **최신 검색어 조회**: 사용자 별 최근 검색어를 최대 10개까지 최신순으로 정렬하여 제공해야 함
  - **빠른 응답 속도**: 대량의 요청에도 즉각적으로 검색어를 반환할 수 있어야 함
  - **불필요한 데이터 관리**: 오래된 검색어는 자동으로 제거하여 불필요하게 축적되지 않도록 해야 함

    ---

  ### 3. 고려한 대안

  | **비교 항목** | **MySQL 인덱스 적용**  | **Redis 적용** |
  | --- | --- | --- |
  | **저장 방식** | 테이블을 사용하여 인덱스로 검색 최적화 | Key-Value 방식으로 사용자별 검색어 관리 |
  | **조회 방식** | ❌ 인덱스를 활용하여 빠르게 조회가 가능하지만, 요청 증가 시 부하 증가 | ✅ 메모리 기반 저장으로 매우 빠른 조회 속도 제공 |
  | **데이터 관리** | ❌ 불필요한 데이터는 수동으로 정리 필요 | ✅ 최신 10개만 유지하며 초과 데이터는 자동 삭제 |
  | **데이터 최신성** | ✅ 실시간 데이터 반영 가능 | ✅ TTL 설정으로 자동 만료 관리 가능 |
  | **시스템 부하** | ❌ 검색 요청 증가 시 데이터베이스 부하 증가 | ✅ 데이터베이스 부하를 최소화하면서 빠른 응답 제공 |
  | **확장성** | ❌ 수직 확장(Scale-Up) 필요, 분산 환경에서 성능 저하 가능 | ✅ 수평 확장(Scale-Out) 가능, 분산 환경에서도 안정적으로 운영 가능 |
    
  ---

  ### 4. 결정 및 근거

  **✅ Redis 적용을 선택**

  - **빠른 응답 속도와 높은 처리량**
      - 메모리 기반 저장소로 빠른 데이터 조회 및 검색이 가능
  - **효율적인 데이터 관리**
      - TTL 기능을 활용하여 일정 시간이 지나면 검색어 데이터를 자동으로 삭제
      - 불필요한 데이터를 자동 정리하여 저장 공간을 효율적으로 활용
  - **정확한 검색 결과 제공**
      - Sorted Set 자료구조를 활용하여 검색어를 삽입할 때 자동 정렬 및 중복 제거 가능
  - **확장성 및 유지보수성 고려**
      - 수평 확장이 가능하여 대량의 요청을 안정적으로 처리
      - 분산 환경에서도 높은 성능과 안정성을 유지 가능

  ---

  ### 5. 영향

  - **긍정적 영향**
      - **빠른 데이터 접근 속도 →** MySQL 대비 조회 속도가 훨씬 빠름
      - **자동 정렬 및 중복 제거 →** Sorted Set을 활용하여 최신 검색어 관리 최적화
      - **TTL 설정으로 메모리 관리 용이  →** 불필요한 데이터 자동으로 삭제
      - **수평 확장 가능  →** 대량 트래픽 처리 가능
  - **부정적 영향**
      - **메모리 사용 증가  →** 메모리 저장 방식으로 많은 데이터를 저장할 경우 RAM 사용 증가
      - **운영 및 관리 부담 증가  →** 메모리 최적화, 장애 대응, 동기화 전략 필요

  ---

  ### 6. 구현 전략

  1. **Sorted Set을 활용하여 검색어 저장**
  2. **최근 검색어 조회**
  3. **자동 삭제 및 만료 설정**

 ---

  ### 7. 향후 고려 사항

  - **TTL 최적화:** 사용자 패턴을 분석하여 최적의 TTL 값 조정 필요
  - **Redis-MySQL 동기화 개선: `Kafka`** 또는 **`Redis Stream`**을 활용한 비동기 동기화 방식 고려
</details>

### **💡모니터링과 로깅이 필요한 순간이 찾아왔습니다!**

<details>
    <summary> 모니터링과 로깅이 필요한 이유, 그리고 적절한 기술은 무엇인가요? </summary>

  ### 1. 배경

  현재 클라우드 아키텍처는 각각 1개의 **`RDS`**, **`ElastiCache`**로 운용되는데, 서비스 규모가 커지면서 **`클러스터링`** 적용을 고려해야 했습니다.

  이에 기술적 의사결정을 내리는 데 필요한 지표를 모아보고, 비정상적인 패턴을 분석해야 했습니다.

  또한 배포 후 에러가 발생했을 때 에러 발생 지점을 쉽게 분석할 수 없는 문제가 있었습니다.

  해당 문제는 특히 전날 밤 정상 작동을 확인한 후 오전에 서버에 접속했을 때, 사진처럼 로그가 끝없이 출력되어 **`Gmail SMTP`** 서버 통신 오류 발생 지점을 찾지 못했을 때 크게 체감했습니다.

![image.png](https://github.com/llRosell/sparta/blob/main/gmail.png?raw=true)

  이러한 불편을 해소하고자 비용, 자원 재사용 가능 여부 같은 요구사항을 고려하여 기술적 의사결정을 내렸습니다.
    
  ---

  ### 2. 요구사항

  - **비용 최소화**: 모니터링-로깅이 프로젝트 전체의 비용을 높여선 안 됨
  - **자원 재사용:** 이미 프로젝트에서 사용하고 있는 자원을 최대한 재사용해야 함
  - **실시간 반영:** 문제에 빠르게 대응하려면 시스템이 가벼워야 함

  ---

  ### 3. 고려한 대안

  | **비교 항목: 모니터링** | **Prometheus + Grafana** | **VictoriaMetrics + Grafana** |
      | --- | --- | --- |
  | **성능** | 단일 노드에서 100만개 이상 처리 가능 | Prometheus보다 10배 빠름 |
  | **데이터 저장** | 메모리 기반 저장 후 디스크 저장 | 디스크 기반 저장 |
  | **확장성** | 수평 확장 어려움 | 수직,수평 확장에 용이함 |
  | **운영 비용** | 오픈소스이지만, 스토리지 비용 발생 | 저장 효율이 높아 스토리지 비용 절약 |
  | **난이도** | 설치와 설정이 간단함 | 단일 노드는 쉽지만 클러스터링은 복잡 |

  | **비교 항목: 로깅** | **Fluentd** | **Logstash** |
  | --- | --- | --- |
  | **성능** | 가볍고 빠름(멀티스레드 지원) | 무겁고 싱글스레드 기반, 튜닝 필요 |
  | **리소스 사용** | RAM, CPU 사용량 적음 | RAM, CPU 사용량 많음 |
  | **운영 난이도** | 설정이 직관적이고 가벼움 | 설정이 복잡하지만 고급 기능 제공 |
  | **확장성** | 컨테이너 친화적(Docker,Kubernetes) | 대용량 데이터 처리에 강함 |
  | **데이터 처리** | JSON 기반 스트리밍 처리(리소스 절약) | 이벤트 기반 파이프라인(JVM 튜닝 필요) |
    
  ---

  ### 4. 결정 및 근거

  ✅  **모니터링에 Prometheus + Grafana + Slack 선택**

![스크린샷 2025-03-12 오후 4.59.49.png](https://github.com/llRosell/sparta/blob/main/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202025-03-12%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%204.59.49.png?raw=true)

  - DataDog, CloudWatch 등 다른 대안이 많았지만, 오픈소스가 아니며 유료라 비용 문제 발생
  - VictoriaMetrics: 운영 효율이 좋아 고민했지만, 비교적 한국 문서가 많은 Prometheus 선택
  - 단일 노드에서 100만개 정도면 현재 서비스 기준으론 성능상 문제 없다고 판단함

  ✅  **로깅에 ElasticSearch + Fluentd + Kibana 선택**

![image.png](https://github.com/llRosell/sparta/blob/main/%E1%84%80%E1%85%B3%E1%84%85%E1%85%A1%E1%84%91%E1%85%A1%E1%84%82%E1%85%A1.png?raw=true)

  - 현재 프로젝트에서 Elasticsearch + Kibana를 이미 사용중이어서 ELK vs EFK 스택 중 고민
  - Fluentd 선택: C++ 기반으로 가볍고 빠르며, yaml 기반으로 설정이 편함
  - Logstash 선택 X:  JVM + 이벤트 기반이므로 추가적인 파이프라인 설정이 부담으로 다가옴

    ---

  ### 5. 운영 환경 선택 - 로컬 Ubuntu 노트북

   - **긍정적 영향**
       - **운영 비용 절감 →** 클라우드 서비스를 사용하지 않으므로 절약됨
       - **설정 편리 →** 직접 운영하므로 GUI기반, 운영에 용이함
   - **부정적 영향**
       - **가용성 문제 →** 노트북이 꺼진다면 가용성 보장할 수 없음
       - **네트워크 성능 저하 문제 →** EC2 내부의 private ip 기반 통신보다 느림
       - **보안 문제 →** 로컬 방화벽 외의 다른 보안 플랜 없음

    ---

  ### 6. 구현

  1. **Grafana + Prometheus / Fluentd를 함께 운용하나, 각각 다른 스택으로 띄워 네트워크 분리**

![image.png](https://github.com/llRosell/sparta/blob/main/image%20(16).png?raw=true)

  2. **모니터링 대상으로 `MySQLld`, `EC2`, `JVM`, `Redis` 선정**

![스크린샷 2025-03-12 오후 4.13.23.png](https://github.com/llRosell/sparta/blob/main/35.png?raw=true)

  3. **Logback Appender: `Console`, `Fluentd`만 사용하며, 배포 환경에서 info 이상인 애플리케이션 로그만 표시**
     - warn, error 로그만 사용하니 서비스 사용 패턴 분석과 스케줄러 모니터링 등이 힘들었음

![스크린샷 2025-03-12 오후 4.00.01.png](https://github.com/llRosell/sparta/blob/main/32.png?raw=true)

  4. **Grafana와 Slack 알림 연동하여 모니터링 효율 강화**
     - 애플리케이션의 CPU, RAM, 500에러 등 체크

![스크린샷 2025-03-13 오후 3.27.27.png](https://github.com/llRosell/sparta/blob/main/44.png?raw=true)


  ---
    
  ### 7. 문제 발생 및 해결 방법
    
  - **[문제 발생]** 네트워크 이슈 발생: 노트북 와이파이 문제로 가용성을 유지하지 못함
        
![image.png](https://github.com/llRosell/sparta/blob/main/image.png?raw=true)
        
![image.png](https://github.com/llRosell/sparta/blob/main/image%20(1).png?raw=true)
        
  - **[해결 방법]** 서버를 EC2로 옮김: Private IP 통신으로 실시간 모니터링, 로깅 가능
        
![스크린샷 2025-03-12 오후 4.08.26.png](https://github.com/llRosell/sparta/blob/main/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202025-03-12%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%204.08.26.png?raw=true)
        
![스크린샷 2025-03-12 오후 4.10.16.png](https://github.com/llRosell/sparta/blob/main/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202025-03-12%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%204.10.16.png?raw=true)
        
    
  ---
    
  ### 8. 향후 고려 사항
    
  - **애플리케이션 로그 이외에 다양한 로그 수집 고려:** MySQL의 **`slow query`** 같은 로그 수집 고려
  - **다양한 Logback Appender 사용 고려**
</details>
</details>

### **💡정합성을 지키면서 페이지 조회수를 빠르게 집계할 수 없을까요?**

<details>
    <summary> 어떤 락을 적용하여 동시성 문제를 해결하면 좋을까요? </summary>

  ### 1. 배경

  다수의 사용자가 동시에 페이지를 조회할 때, 조회수의 정합성을 유지하면서도 다른 업데이트에 영향을 주지 않고 빠른 응답 속도를 확보하고자 했습니다.

  다양한 Lock 기술을 직접 구현하며 동시성 문제를 해결하고, 각 방식의 장단점을 비교하여 기술적 의사결정을 내렸습니다.
    
  ---

  ### 2. 요구사항

  - **정합성 유지**: 다수의 사용자가 동시에 조회해도 정합성이 유지되어야 함
  - **조회 부하 감소**: 조회를 할 때 불필요한 연산을 하지 않아야 함
  - **응답 속도 안정성 유지**: 조회 할 때마다 응답 속도가 안정적이어야 함

    ---

  ### 3. 고려한 대안

  | **비교 항목** | **Optimistic Lock** | **Pessimistic Lock** | **집계 테이블 Pessimistic Lock** | **Redis와 Distributed Lock 스케줄러** |
  | --- | --- | --- | --- | --- |
  | **정합성** | ❌ 재시도해도 15% 미만 | ✅ 정합성 100% | ✅ 정합성 100% | ✅ 원자적 연산으로 정합성 100% |
  | **조회 시 부하 정도**  | ✅ 조회 시 업데이트로 인한 락이 없음 | ❌ 조회 시 락이 걸린 갱신으로 인한 부하 상승 | ✅ 조회 시 갱신하지 않으므로 부하 감소 | ✅ 조회는 RDB이고  집계는 Redis(인메모리)라서 부하 낮음 |
  | **응답 속도 안정성** | ✅ 업데이트 락이 없으므로 속도가 안정적임 | ❌ 락의 대기로 인해 응답 속도의 불규칙적 | ❌ 락의 대기로 인해 응답 속도가 불규칙적 | ✅ 집계에서 락을 사용하지 않으므로 응답 속도가 안정적 |
  | **단점** | ⚠️ 충돌 시 정합성이 매우 떨어짐 | ⚠️ DeadLock가능성이 높음 | ⚠️ 충돌 시 응답속도 차이가 있음 | ⚠️ 데이터의 휘발성 |
    
  ---

  ### 4. 결정 및 근거

  ✅ **Redis와 Distributed Lock 스케줄러 선택**

  - Redis의 원자적 연산 incr 사용으로 정합성 유지
  - 조회 시 RDB가 바로 갱신되는 것이 아니므로 부하가 낮음
  - 동시성 관리 자체에는 Lock이 사용되지 않으므로 응답 속도가 안정적임

  ---

  ### 5. 영향

  - **긍정적 영향**
      - **정합성 유지 →** Redis의 원자적인 연산 특성을 통해 정합성이 유지됨
      - **조회 시 부하 감소 →** 조회와 집계가 RDB와 인메모리로 완전히 분리되어 부하가 감소함
      - **안정적인 응답 속도 →** 집계 시 Lock이 존재하지 않아서 응답속도의 안정성 상승
  - **부정적 영향**
      - **데이터가 휘발될 수 있음 →** Redis의 연산 후 작업 스케줄러가 동작하기 전에 서버가 다운되는 등 문제 발생 시 Redis에 집계된 조회수가 유실될 수 있음

    ---

  ### 6. 구현 전략

  1. **Redis를 집계 테이블처럼 사용**
      - RedisViewCountManager 펼치기

      ```java
            @Component
            @RequiredArgsConstructor
            public class RedisViewCountManager {
            
                private final RedisTemplate<String, String> redisTemplate;
            
                // 조회수 키의 기본 Prefix
                private static final String VIEW_COUNT_KEY_PREFIX = "viewCount:";
            
                /**
                 * 특정 채용 공고의 조회수를 증가시키는 메서드
                 * @param jobOpeningId 조회수를 증가시킬 채용 공고 ID
                 */
                public void increaseViewCount(Long jobOpeningId) {
                    String key = VIEW_COUNT_KEY_PREFIX + jobOpeningId; // Redis 키 생성
            
                    if (redisTemplate.opsForValue().get(key) == null) {
                        redisTemplate.opsForValue().set(key, "0", Duration.ofMinutes(30));  // 30분 TTL 설정
                    }
                    redisTemplate.opsForValue().increment(key);
                }
            
                /**
                 * 조회수 동기화 후 RDB에 값이 반영된 조회수는 Redis에서 삭제하는 메서드
                 * @param jobOpeningId 키를 삭제할 채용 공고 Id 값
                 */
                public void resetViewCount(Long jobOpeningId) {
                    String key = VIEW_COUNT_KEY_PREFIX + jobOpeningId;
                    redisTemplate.delete(key);
                }
            
                /**
                 * 채용 공고의 조회수를 전체 가져오는 메서드
                 * 동기화를 위한 메서드
                 * @return 현재 Redis에 저장된 조회수 전부
                 */
                public List<ViewCountResponseDto> findAllViewCount() {
                    Set<String> keys = redisTemplate.keys(VIEW_COUNT_KEY_PREFIX + "*"); // 모든 조회수 키 찾기
                    List<ViewCountResponseDto> result = new ArrayList<>();
            
                    for(String key : keys) {
                        Long jobOpeningId = Long.parseLong(key.replace(VIEW_COUNT_KEY_PREFIX, ""));
                        Long count = Long.valueOf(Objects.requireNonNull(redisTemplate.opsForValue().get(key)));
                        result.add(new ViewCountResponseDto(jobOpeningId, count));
                    }
                    return result;
                }
            }
      ```

  2. **분산 락이 적용된 작업 스케줄러로 RDB에 조회수 반영**
    - JobOpeningViewCountTaskHandler 펼치기

  ```java
            @Slf4j
            @Component
            @RequiredArgsConstructor
            public class JobOpeningViewCountTaskHandler implements TaskHandler {
            
                private final JobOpeningRepository jobOpeningRepository;
                private final RedisViewCountManager redisViewCountManager;
            
                @Override
                public String getTaskType() {
                    return "syncJobOpeningViewCount";
                }
            
                @Override
                @Transactional
                public void handle(Map<String, Object> payload) {
                    List<ViewCountResponseDto> redisJobOpeningViewCountList = redisViewCountManager.findAllViewCount();
                    for (ViewCountResponseDto jobOpening : redisJobOpeningViewCountList) {
                        Long jobOpeningId = jobOpening.key();
                        Long viewCount = jobOpening.Value();
                        if (viewCount != null && viewCount > 0) {
                            jobOpeningRepository.updateViewCount(jobOpeningId, viewCount);
                            redisViewCountManager.resetViewCount(jobOpeningId);
                        }
                    }
                    log.info("조회수 동기화 완료");
                }
            
                @Override
                public long getScheduleIntervalMillis() {
                    return 3600000L; // 1시간
                }
  ```


 ---
    
 ### 7. 향후 고려 사항
    
 - **Redis의 휘발성 문제 개선:** Redis 서버를 여러 개 띄워서 백업용으로 사용하는 방안 고려
</details>
</details>
</aside>

---

## 🚨 트러블슈팅

<details>
<summary> Elasticsearch를 적용해서 응답 속도는 빨라졌는데, TPS는 감소했습니다!</summary> 
    
---

### 1. **문제 정의**

- Elasticsearch를 도입한 다음, 스레드를 5,000개로 설정했을 때 평균 응답 속도는 단축되었으나, TPS는 오히려 감소하는 문제가 발생했습니다.

| **비교 항목** | **Elasticsearch 적용 전** | **Elasticsearch 적용 후** | **배율** |
| --- | --- | --- | --- |
| **응답 속도** | 28ms | 7ms | **4배 단축** |
| **TPS** | 249.1건/sec | 35.1건/sec | **7.1배 감소** |

![초당처리량 감소 이미지](https://github.com/llRosell/sparta/blob/main/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202025-03-16%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%207.22.01.png?raw=true)

---

### 2. **원인 파악**

- 너무 많은 동시 요청이 들어와서 CPU와 메모리 같은 자원이 한계에 도달하는 병목 현상이 발생함

---

### 3. **해결 과정**

- 스레드 수를 5,000개에서 200개로 줄인 다음 테스트를 다시 진행함

---

### 4. 결과

- 스레드 수를 200개로 조정하여 성능이 안정되면서 TPS가 증가함

![초당처리량 증가 이미지](https://github.com/llRosell/sparta/blob/main/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202025-03-12%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%207.14.59.png?raw=true)

| **비교 항목** | **Elasticsearch 적용 전** | **Elasticsearch 적용 후** | **배율** |
| --- | --- | --- | --- |
| **응답 속도** | 48ms | 14ms | **3.4배 단축** |
| **TPS** | 1.4건/sec | 9.5건/sec | **6.8배 증가** |

</details>

<details>
<summary> 많은 사용자가 동시에 채용 공고를 조회할 때 응답 시간이 너무 다릅니다!</summary>
    
---

### 1. 문제 정의

채용 공고 페이지 조회 API에서 동시에 다수의 사용자가 페이지를 조회할 때 조회수 집계 과정에서 동시성 문제가 발생했습니다.

이러한 동시성 문제를 해결하고자 집계 테이블을 별도로 작성하여 비관적 락을 적용하였는데도, 충돌이 발생하면 사용자마다 응답 시간의 변동 폭이 크다는 문제를 추가로 발견했습니다.

부하 테스트를 진행한 결과, 스레드가 늘어나면서 응답 시간도 증가했는데 이때 응답 시간 변동성이 매우 커진다는 점을 파악했습니다.

![image.png](https://github.com/llRosell/sparta/blob/main/%E1%84%8C%E1%85%B5%E1%84%80%E1%85%B3%E1%84%8C%E1%85%A2%E1%84%80%E1%85%B3.png?raw=true)

---

### 2. 원인 파악

- 비관적 락 때문에 병목 현상 또는 경쟁 상태(Race Condition) 발생
    - 집계 테이블에서 비관적 락 때문에 대기가 발생하여 병목 현상이 발생했을 가능성 존재
    - 메인테이블과 분리했는데도 비관적 락을 사용해서 경쟁 상태가 발생했을 수 있음

---
### 3 해결 과정

- 비관적 락을 배제한 동시성 제어 방법을 검토

  (1) Redis의 원자적 연산을 이용하여 동시성 제어 시도

  (2) Elasticsearch에 Post 요청을 보내 Document를 전송하여 동시성 제어 시도


→ Redis로 원자적 연산 적용

- Elasticsearch 사용 시, 조회수 관련 검색은 Elasticsearch만 사용해야 하는 단점 존재
    - 유지 보수 시 접근성이 낮아짐
    - 유지 보수 복잡도가 증가함
- Redis를 집계 테이블처럼 사용하면서, 분산 락이 적용된 작업 스케줄러로 조회수를 정기적으로 채용 공고 테이블에 업데이트하는 방식 적용
<details>
<summary> increaseViewCount 메서드 펼치기 </summary> 

![image.png](https://github.com/llRosell/sparta/blob/main/image%20(3).png?raw=true)
</details>

---

### 4 . 결과

- 레디스 적용 후, 응답 속도가 매우 안정적으로 바뀜
<details>
<summary> 부하 테스트 결과 그래프 펼치기 </summary> 
        - 스레드 증가에 따라 응답 시간이 늘어나지만, 변동 폭이 매우 규칙적으로 바뀜

![image.png](https://github.com/llRosell/sparta/blob/main/image%20(4).png?raw=true)

- 응답 시간이 대폭 감소하면서 TPS도 대폭 상승함
</details>

<details>
<summary> 테스트 결과 사진 펼치기 </summary> 

![image.png](https://github.com/llRosell/sparta/blob/main/image%20(5).png?raw=true)

![image.png](https://github.com/llRosell/sparta/blob/main/image%20(6).png?raw=true)
</details>

| **비교 항목** | **Redis 적용 전** | **Redis 적용 후** | **배율** |
| --- | --- | --- | --- |
| **평균 응답 시간** | 431.42ms | 4.28ms | 약 100배 단축 |
| **최악 응답 시간** | 1s 153.94ms | 6.00ms | 약 192배 단축 |
| **TPS** | 210.66  | 398.33  | 약 1.89배 증가 |

</details>

<details>
<summary> 다중 인스턴스 환경에서 스케줄러가 인스턴스 개수만큼 실행됩니다!</summary> 
    
---

### 1. **문제 정의**

현재는 단일 인스턴스로 운용되고 있지만, 단일 인스턴스만을 사용한다고 단정 지을 수 없었습니다.

서버에 일정 수준 이상의 부하가 감지되면, Auto Scaling Group이 자동으로 인스턴스를 추가로 생성하고 롤링 업데이트 시에도 신버전과 구버전이 혼재했기 때문입니다.

이러한 이유로 다중 인스턴스 환경을 가정하고 개발을 진행해야 했습니다.

여기서 문제가 발생했습니다.

인스턴스 내부 애플리케이션별로 스케줄러가 실행되는데, 다중 인스턴스 환경이라면 이메일 알림 발송 스케줄러가 동시에 실행될 수 있었습니다.

같은 이메일 알림이 중복으로 발송된다면 사용자 경험도 떨어지고 DB 서버 부하도 피할 수 없었습니다.

따라서 다중 인스턴스 환경용 스케줄러를 개발해야 했습니다.
    
---

### 2. **원인 파악**

- 애플리케이션에 등록된 스케줄러가 인스턴스의 개수만큼 실행됨

![스크린샷 2025-03-12 오후 5.19.12.png](https://github.com/llRosell/sparta/blob/main/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202025-03-12%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%205.19.12.png?raw=true)


---

### 3. **해결 과정**

1. 일반적인 분산 락으로는 해결이 불가능
    - 분산 락, DB 락 → ‘동시성 문제’를 해결하기 위한 ‘순차 실행 플랜’
    - 순차 실행이 필요한 게 아니라, 중복 실행 방지 대책이 필요
2. Redis 캐시를 일종의 분산 락처럼 사용

    ```java
    @Slf4j
    @Component
    @RequiredArgsConstructor
    public class SchedulerLockUtil {
    
        private final RedisTemplate<String, String> redisTemplate;
    
        private static final String LOCK_VALUE = InstanceUtil.getInstanceId();
    
        /**
         * 인스턴스 단위로 스케줄링을 관리하는 유틸메서드입니다.
         * key 와 ttl 을 인자로 받아 redis 에서 관리합니다.
         */
        public void lock(String keyName, long ttl) {
            //setIfAbsent 로 원자적으로 락을 잡으려 시도
            Boolean acquired = redisTemplate
                    .opsForValue()
                    .setIfAbsent(keyName, LOCK_VALUE, ttl, TimeUnit.MINUTES);
    
            //락을 못 잡았다면 내 인스턴스의 락인지 확인
            if (Boolean.FALSE.equals(acquired)) {
                String currentValue = redisTemplate.opsForValue().get(keyName);
                //다른 인스턴스의 소유라면 그냥 리턴
                if (!LOCK_VALUE.equals(currentValue)) {
                    log.info("다른 인스턴스에서 실행중인 스케줄러입니다.");
                    throw new IllegalStatusException(ServerErrorCode.ALREADY_RUNNING_SCHEDULER);
                }
                //만약 내 인스턴스라면 이전 스케줄이 남긴 락이거나 ttl 이 안 끝난 상황일 수 있으므로 ttl 재갱신
                redisTemplate.expire(keyName, ttl, TimeUnit.MINUTES);
            }
        }
    }
    ```

    - 스케줄러의 식별자를 key로 사용 (email_scheduler, …)
    - 현재 실행 중인 jvm의 고유 아이디를 value로 사용
    - keyname과 ttl을 파라미터로, 유틸 메서드로 분리해 어디에서든 재사용하게끔 설계

---

### 4. 새로운 문제 발생

![스크린샷 2025-03-12 오후 5.36.12.png](https://github.com/llRosell/sparta/blob/main/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202025-03-12%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%205.36.12.png?raw=true)

- 롤링 업데이트 시 새로운 인스턴스가 생성되고 기존 인스턴스는 삭제되는데, 기존 인스턴스에서의 스케줄링 텀을 그대로 이어나가지 못하는 문제가 발생함
- 최악의 상황에서는, 이메일 알림, elasticsearch migration 등의 작업을 한 텀 건너뛰게 됨

---

### 5. 문제 해결

1. **Redis 활용**
    - 스케줄링 실행 자체를 ‘단일 인스턴스 환경’에 맡겨서, 중앙에서 제어하는 방식이 필요
    - Redis를 이용하여 작업들을 queue에 넣고, 하나의 인스턴스만 실행되는 구조로 변경
    - 스케줄링을 짧은 주기로 확인해야 하는 만큼, 빠른 읽기 I/O가 요구되는 구조이므로 Redis 사용
2. **다중 인스턴스용 스케줄러 아키텍처 적용**

![스크린샷 2025-03-12 오후 5.38.37.png](https://github.com/llRosell/sparta/blob/main/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202025-03-12%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%205.38.37.png?raw=true)

   (1) 애플리케이션이 최초 실행되면, 모든 스케줄러가 자동으로 실행됨

   (2) Redis에서 해당 스케줄러의 고유 Key와 실행된 시간을 기록

   (3) 일정 주기마다 애플리케이션에 등록된 스케줄러 주기와 이전 실행 시간을 비교

   (4) 주기 및 이전 실행 시간을 더한 값이 현재 시간보다 같거나 크다면 해당 작업을 queue에 등록

    ```java
    /**
     * 5초에 한번 새 작업을 스케줄링(등록)합니다.
     */
    @Scheduled(fixedDelay = 5000)
    public void scheduleTasks() {
           instanceManager.updateLatestInstance();
           if (!instanceManager.isLatestInstance()) {
               return;
           }
           for (TaskHandler handler : schedulerTaskHandlers) {
               taskRegister.register(handler);
           }
    }
    ```

   (5) queue에 등록되어 있는 작업 또한 일정 주기 마다 확인하여 하나씩 꺼내 실행

   → 실행 시 락 획득하여 원자성 보장

    ```java
    @Scheduled(fixedDelay = 5000)
    public void processDueTasks() {
        instanceManager.updateLatestInstance();
        //최신 인스턴스가 아니면 작업 실행을 건너뛰기
        if (!instanceManager.isLatestInstance()) {
            return;
        }
        if (!isRunning) return;
    
        String taskDataString = taskRepository.getDueTask(SORTED_SET_KEY);
            if (taskDataString == null) {
                return;
            }
    
        try {
            Map<String, Object> taskData = objectMapper.readValue(taskDataString, Map.class);
            String taskType = (String) taskData.get("taskType");
            Map<String, Object> payload = (Map<String, Object>) taskData.get("payload");
    
            TaskHandler handler = handlers.get(taskType);
            if (handler != null) {
                long scheduleIntervalMillis = handler.getScheduleIntervalMillis();
                if (scheduleIntervalMillis <= 0) return;
                String lockKey = "scheduler:lock:consumer" + handler.getTaskType();
                try {
                    if (redissonRepository.tryLock(lockKey, Math.min(2000, scheduleIntervalMillis / 2), scheduleIntervalMillis / 2, TimeUnit.MILLISECONDS)) {
                        try {
                            handler.handle(payload);
                            log.info("TaskConsumer: 작업 완료: {} at {}", taskType, Instant.now());
                        } catch (Exception e) {
                            log.error("TaskConsumer: 작업 실행 중 오류: {}", e.getMessage(), e);
                        } finally {
                            redissonRepository.unlock(lockKey);
                        }
                    } else {
                        log.warn("TaskConsumer: 락 획득 실패, 작업 스킵: {}", taskType);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("TaskConsumer: 락 획득 중 인터럽트 발생: {}", e.getMessage());
                }
           }
        } catch (Exception e) {
            log.error("TaskConsumer: 데이터 파싱 중 오류: {}", e.getMessage(), e);
        }
    }
    ```

   (6) 작업을 queue에 등록하거나, queue에 등록된 작업을 확인하는 작업은 ‘최신의 인스턴스’만 실행

   Q. 왜 ‘최신의 인스턴스’에서만 실행되도록 했는가?

   → 기존 인스턴스는 롤링 업데이트 과정 때문에 작업이 종료될 수 있음

   → 작업을 등록 및 확인할 때 어떤 인스턴스가 가장 최신인지 판단함

   → 빈(Bean) 생성 후 초기화 시 애플리케이션의 시작 시간을 final로 기록

   → 최신 인스턴스가 ‘unhealthy’하여 롤백해야 하는 상황도 고려하여 설계함

    ```java
    /**
     * 현재 인스턴스의 헬스 체크를 수행해서 actuator/health 가 UP 상태인지 확인
     */
    public static boolean isInstanceHealthy() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String healthUrl = "http://localhost:8080/actuator/health";
            ResponseEntity<Map> response = restTemplate.getForEntity(healthUrl, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Object statusObj = response.getBody().get("status");
                if (statusObj != null && "UP".equalsIgnoreCase(statusObj.toString())) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("헬스 체크 실패", e);
        }
        return false;
    }
    ```

   (7) ‘일정한 시간에만’ 실행되는 스케줄러를 추가로 구현

    - 전략패턴을 사용하여 고정 주기 및 특정 시간에 실행하는 스케줄러 두 가지 구현

    ```java
    public class SpecificTimeStrategy implements ScheduleStrategy {
    
        private final LocalTime specificTime;
    
        /**
         * @param specificTime 매일 실행할 특정 시간
         */
        public SpecificTimeStrategy(LocalTime specificTime) {
            this.specificTime = specificTime;
        }
    
        @Override
        public Instant getNextExecutionTime(Instant lastExecutionTime, long scheduleIntervalMillis) {
            ZoneId zone = ZoneId.of("Asia/Seoul");
            LocalDateTime now = LocalDateTime.now(zone);
            LocalDateTime candidate = LocalDateTime.of(now.toLocalDate(), specificTime);
            if (candidate.isBefore(now) || candidate.isEqual(now)) {
                candidate = candidate.plusDays(1);
            }
            return candidate.atZone(zone).toInstant();
        }
    }
    ```

    - Redis에 저장되는 최종 형태
        - 예악된 작업은 ScoredSortedSet에 들어감
        - 특정 시간대에 실행되는 스케줄러는 항상 다음 스케줄링 시간을 Score로 가지고 예약 상태로 존재

          ![스크린샷 2025-03-12 오후 8.27.50.png](attachment:82080312-6d25-4349-8906-24adc1cfa702:스ᄏ린샷_2025-03-12_오후_8.27.50.png)

3. **누구나 쉽게 사용할 수 있도록 인터페이스 구현**
<details>
<summary> TaskHandler 인터페이스 펼치기 </summary>

```java
    public interface TaskHandler {
    
        String getTaskType(); //처리할 작업 유형
        void handle(Map<String, Object> payload); //작업 실행 로직
    
        //스케줄링 주기(고정 주기 예약일 경우에만 구현)
        default long getScheduleIntervalMillis() {
            return 60000L;
        }
    
        /**
         *  예약 전략(특정 주기 예약하고 싶은 경우 new SpecificTimeStrategy(); 로 구현)
         *  예시: @Override
         *  public ScheduleStrategy getScheduleStrategy() {
         *      return new SpecificTimeStrategy(LocalTime.of(00, 00, 0)); 매일 자정에 실행
         *  }
         */
        default ScheduleStrategy getScheduleStrategy() {
            return new FixedIntervalStrategy();
        }
    
        //스케줄러에 사용하는 변수(default 는 변수 없음)
        default Map<String, Object> getDefaultPayload() {
            return null;
        }
    }
```
</details>

### 5. **회고**
    
- **긍정적 영향**
    - **문제 해결 완료 →** 다중 인스턴스 환경에서 스케줄러가 인스턴스 개수만큼 실행되는 문제 해결
    - **구현이 편리한 구조 구축 →**  누구나 인터페이스만 구현하면 쉽게 구현할 수 있음
- **부정적 영향**
    - **리소스 낭비 →** Redis에 5초마다 읽기 요청을 보내기 때문에 꾸준한 모니터링이 필수적임
    - **스케줄러가 동작하지 않을 수 있음 →** Redis가 다운되면 스케줄러가 동작하지 않을 수 있음
    
### **6. 향후 고려 사항**
    
- **Quartz 라이브러리 사용 고려:** 현재 오버엔지니어링이라 판단되어 도입하지 않았지만, 추후 활용하여 다중 인스턴스 환경에서 스케줄러를 더 강력하게 제어할 수 있음
- 오버엔지니어링 판단 근거: RDBMS에 quartz 클러스터링 전용 테이블이 10개 이상 필요
    
---
</details>

<details>
<summary> 채용 공고 데이터 크롤링 시 마주한 문제를 소개합니다! </summary> 

<details>
<summary> 사이트마다 프론트도 데이터 형식도 다른데 어떻게 하나로 모을 수 있을까요? </summary>
        
---

### 1. **문제 정의**

문제 소개에 앞서, 크롤링에는 Kotlin을 사용했습니다.

따라서 크롤링 탭에서 보여드릴 코드는 모두 Kotlin 코드입니다.

두 채용 공고 사이트 **`사람인`**과 **`잡코리아`**를 크롤링했을 때, 두 사이트의 html 태그와 Javascript 형식, 데이터 생김새가 너무나 달라서 해당 문제가 발생했습니다.

Kotlin의 **`Coroutine`**을 사용해 크롤링과 데이터 정형화를 빠르게 하고 싶었으나, 크롤링 트러블슈팅에서 후술할 문제들 때문에 **`Coroutine`**을 사용하지 못했습니다.
        
---

### 2. **원인**

- 잡코리아의 개발자 채용 공고 목록 페이지를 **`RequestParam`**으로 접근할 수 없음
- 사람인의 페이지가 Javascript 랜더링을 사용함
- 학력, 회사 이름 등 데이터가 정리되지 않음

![image.png](https://github.com/llRosell/sparta/blob/main/image%20(7).png?raw=true)
        
---

### 3. **해결 과정**

1. 잡코리아
- 목록에서는 **`Selenium`**으로 버튼을 클릭하여 ‘개발자 채용 공고’ 탭으로 접근 후 URL 크롤링 진행
<details>
<summary> 버튼 클릭용 Filter 코드 </summary> 

```kotlin
                    object JobKoreaFilters {
                        fun apply(wait: WebDriverWait) {
                            //"직무" 필터 클릭
                            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("p.btn_tit")))
                                .apply { click() }
                                .also { Thread.sleep(3000) }
                    
                            //"개발 / 데이터" 필터 클릭
                            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("label[for='duty_step1_10031']")))
                                .also { element ->
                                    wait.until(ExpectedConditions.elementToBeClickable(element)).click()
                                }
                                .also { Thread.sleep(3000) }
                    
                            //세부 직무에서 요소 체크
                            listOf("1000229", "1000230", "1000231", "1000232").forEach { jobValue ->
                                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("label[for='duty_step2_$jobValue']")))
                                    .also { wait.until(ExpectedConditions.elementToBeClickable(it)).click() }
                                Thread.sleep(1000)
                            }
                    
                            //"검색" 버튼 클릭
                            wait.until(ExpectedConditions.elementToBeClickable(By.id("dev-btn-search")))
                                .apply { click() }
                                .also { Thread.sleep(3000) }
                    
                            //정렬 기준 선택 "최신업데이트순"
                            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("orderTab")))
                                .let { Select(it) }
                                .apply { selectByValue("3") }
                                .also { Thread.sleep(3000) }
                        }
                    }
```
</details>

- 세부 페이지는 목록에서 가져온 URL로 **`jsoup`** 크롤링 진행
2. 사람인
- 사용할 모든 페이지에 Javascript 렌더링이 걸림
- 목록에서는 **`selenium`**으로 페이지 끝까지 스크롤 후 URL 크롤링 진행
<details>
<summary> 페이지 스크롤러 코드 </summary> 

```kotlin
                    object SaraminScroller {
                    
                        fun toBottom(driver: WebDriver) {
                            val js = driver as JavascriptExecutor
                            var prevHeight = js.executeScript("return document.body.scrollHeight") as Long
                            while (true) {
                                js.executeScript("window.scrollTo(0, document.body.scrollHeight);")
                                Thread.sleep(2000)
                                val newHeight = js.executeScript("return document.body.scrollHeight") as Long
                                if (newHeight == prevHeight) break
                                prevHeight = newHeight
                            }
                        }
                    }
```
</details>

- 세부 페이지 또한 **`selenium`**으로 크롤링 진행
3. 데이터 정형화
- 데이터 분석 전문 분야이므로, 기술적인 한계로 제대로 된 정형화는 불가능

 → OpenAI API 사용

![스크린샷 2025-03-12 오후 9.20.19.png](https://github.com/llRosell/sparta/blob/main/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202025-03-12%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%209.20.19.png?raw=true)

---

### 4. 결과

![스크린샷 2025-03-12 오후 8.55.40.png](https://github.com/llRosell/sparta/blob/main/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202025-03-12%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%208.55.40%20(1).png?raw=true)

</details>

<details>
<summary> 크롤링하다가 오류가 생기면 채용 공고가 하나도 저장되지 않습니다! </summary> 
        
---
        
### 1. **문제 정의**
        
크롤링을 진행하다가 네트워크 문제로 예상치 못한 오류가 발생했습니다.
        
‘오류 이전의 데이터는 저장이 되었으려나?’ 하였지만 반복문 전체에 트랜잭션을 걸어버린 탓에 데이터가 모두 유실되었습니다.
        
원래는 데이터 전체의 원자성을 보장하고자 반복문 전체에 트랜잭션을 걸었는데, 이 때문에 데이터가 모두 유실되는 문제가 발생했습니다. 
        
크롤링을 최대한 윤리적으로 하려다 보니 몇 시간 단위의 작업이 되었고, 이는 곧 서비스 불안정으로 이어졌습니다.
        
---
        
### 2. **원인**
        
- 트랜잭션 범위가 너무 큼 ▼
        
```kotlin
        @Transactional
        override fun crawl(maxPages: Int) {
            val driver = webDriverFactory.createDriver()
            val wait = WebDriverWait(driver, Duration.ofSeconds(5))
            val baseUrl = "https://www.jobkorea.co.kr/recruit/joblist?menucode=search#anchorGICnt_1"
        
            driver.get(baseUrl)
            try {
                JobKoreaFilters.apply(wait)
        
                (1..maxPages).forEach { currentPage ->
                    val pageUrl = "https://www.jobkorea.co.kr/recruit/joblist?menucode=search#anchorGICnt_$currentPage"
                    driver.get(pageUrl)
                    println("현재 페이지: $currentPage")
        
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("strong a.link.normalLog")))
                    val jobListings = driver.findElements(By.cssSelector("strong a.link.normalLog")).take(40)
                    if (jobListings.isEmpty()) {
                        println("채용 공고를 찾을 수 없으므로 크롤링 종료")
                        return@crawl
                    }
```
        
 **[문제 1]** 페이지를 순회하는 **`forEach`**문 전체를 한 트랜잭션으로 감싸버림
        
→ 오류 원인 자체는 트랜잭션과 상관없지만, 오류에 대응할 수 있는 코드가 필요해짐 
        
**[문제 2]** 긴 트랜잭션은 다양한 문제를 일으킬 수 있음 
        
(1) 동시성 저하와 처리량 감소
        
   - 긴 트랜잭션은 데이터에 오랜 시간 락을 유지
            
       → 다른 트랜잭션들은 필요한 데이터에 접근하지 못하고 대기 상태에 놓임
            
       → 대기 상태가 길어질수록 시스템의 전체 처리량(throwghput)이 감소함
            
   1. 시스템 리소스 낭비
       - 대기 중인 트랜잭션들은 여전히 시스템 리소스를 점유함
   2. 데드락 발생 가능성 존재
       - 긴 트랜잭션이 더 많은 자원을 더 오래 점유하기 때문에 데드락이 발생할 수 있음
   3. 락 에스컬레이션 발생 가능성 존재 
       - 긴 트랜잭션이 많은 수의 행에 락을 설정해서 락 에스컬레이션 발생 확률이 높아짐
       - 테이블 전체에 락이 설정되어 동시성이 저하될 수 있음
        
---
        
### 3. **해결 과정**
        
- 트랜잭션 전파 속성을 사용함
- forEach문 내부의 반복되는 코드를 메서드로 분리
- 트랜잭션 전파 속성 중 하나인 **`REQUIRES_NEW`** 사용
- 페이지마다 새로운 트랜잭션이 생성되도록 리팩토링 진행
        
    → 한 페이지에서 예외 발생 시
        
    - 해당 페이지의 작업만 롤백됨
    - 다른 페이지의 크롤링 작업은 정상적으로 진행되도록 함
        
```kotlin
        @Transactional(propagation = Propagation.REQUIRES_NEW)
        fun processPage(currentPage: Int, driver: org.openqa.selenium.WebDriver, wait: WebDriverWait) {
            val pageUrl = "https://www.jobkorea.co.kr/recruit/joblist?menucode=search#anchorGICnt_$currentPage"
            driver.get(pageUrl)
        
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("strong a.link.normalLog")))
            val jobListings = driver.findElements(By.cssSelector("strong a.link.normalLog")).take(40)
            if (jobListings.isEmpty()) {
                return
            }
        
            jobListings.forEach { job ->
                processJob(job)
            }
        }
```
        
---
        
### 4. **회고**
        
- 페이지 단위로 안정성이 보장되나, 전체 작업의 원자성이 떨어지는 문제 존재
            
     → 윤리적인 문제로 프록시, VPN을 사용하여 크롤링을 아주 빠르게 진행하기는 불가능
            
     → 어쩔 수 없이 네트워크 연결을 아주 오랫동안 유지해야 하므로, 현재 방식이 피해를 최소화한다고 판단됨
            
- 아쉬운 점: 다른 웹사이트의 리소스를 사용하여 성능 테스트를 진행하기 어려움
     - 예시) 페이지마다 새로운 트랜잭션을 생성하고, 커밋/롤백하는 과정에서 성능 오버헤드가 있을 듯한데, 이를 네트워크 i/o까지 고려하여 테스트하기는 쉽지 않음
        
     → 이러한 이유로 안정성이 보장되지 않은 시스템은 서버에 업로드할 수 없다고 판단함
        
     → 크롤링 작업은 로컬DB, 로컬 서버 환경에서 진행함

</details>
</details>

<details>
<summary> 이메일 알림으로 보낼 채용 공고가 중복되어 조회됩니다! </summary> 
    
  ---

  ### 1. **문제 정의**

- 채용 공고가 중복 조회를 방지하고자 생성일을 **`ZonedDateTime`**으로 추가했는데도 여전히 중복으로 조회되었습니다.
- 로그를 확인한 결과, 조회 시간은 **`한국 표준시 KST`** 기준으로 출력되지만, 생성일은 **`협정 세계시 UTC`** 기준으로 저장되는 문제가 발생했습니다.

---

### 2. **원인 파악**

 - **`ZonedDateTime`** 사용 시 MySQL에서 서버 시간과 무관하게 자동으로 **`UTC`**로 저장함
    - MySQL은 **`TIMESTAMP`** 값을 현재 시간대에서 **`UTC`**로 변환하여 저장하고, 저장된 값을 다시 **`UTC`**에서 현재 시간대로 변환하여 조회함
<details>
<summary> 공식 문서 설명 펼치기 </summary> 

> *MySQL converts TIMESTAMP values from the current time zone to UTC for storage, and back from UTC to the current time zone for retrieval.*
>
</details>

---

### 3. **해결 과정**

- 조회 시간을 UTC로 변경함

```java
        ZonedDateTime referenceTime = ZonedDateTime.now()
            .minusSeconds(30L)
            **.withZoneSameInstant(ZoneId.of("UTC"));**
```

- 이전 조회 시간 이후에 올라온 채용 공고만 조회하는 로직은 변경하지 않음

```java
        @Override
        public Map<Long, List<String>> findKeywordIdToUrlList(
            ZonedDateTime referenceTime
        ) {
            return queryFactory
                .from(jobOpeningKeyword)
                .join(jobOpeningKeyword.jobOpening, jobOpening)
                **.where(jobOpening.createdAt.after(referenceTime))** 
                .transform(
                    groupBy(jobOpeningKeyword.keyword.id) 
                        .as(list(jobOpening.jobOpeningUrl)) 
                );
        }
```


---
    
### 4. 결과

- 조회 시간이 **`UTC`**로 변환되어 출력됨
- 한 번 조회된 채용 공고가 중복으로 조회되지 않음
    
![다운로드 (17).png](https://github.com/llRosell/sparta/blob/main/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202025-03-17%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%202.03.28.png?raw=true)

</details>

<details>
<summary> 이메일 알림을 발송하려는데 ‘Too many login attempts’ 오류가 발생했습니다! </summary> 
    
---

### 1. **문제 정의**

- **`Gmail`**로 이메일 알림을 발송하려고 할 때, 대량 발송이 아니었는데도 **`Too many login attempts`** 오류가 발생하여 서버 운영에 차질이 생겼습니다.

![다운로드 (8) (1).png](https://github.com/llRosell/sparta/blob/main/%E1%84%83%E1%85%A1%E1%84%8B%E1%85%AE%E1%86%AB%E1%84%85%E1%85%A9%E1%84%83%E1%85%B3%20(8)%20(1).png?raw=true)

![다운로드 (9) (1).png](https://github.com/llRosell/sparta/blob/main/%E1%84%83%E1%85%A1%E1%84%8B%E1%85%AE%E1%86%AB%E1%84%85%E1%85%A9%E1%84%83%E1%85%B3%20(9)%20(1).png?raw=true)


---
    
### 2. **원인 파악**
    
- 이메일 알림을 짧은 시간 안에 여러 번 발송하면, **`Gmail`** 서버가 이를 무차별 대입 공격으로 간주하여 **`Too many login attempts`** 오류가 발생함
    
---
    
### 3. **해결 과정**
    
- 일주일 동안 이메일을 총 200개 미만 보냈는데도 해당 오류가 발생하여, 서버 변경이 불가피해짐
- **`Gmail`**을 **`SendGrid`**로 대체함
    - SendGrid 설정 및 빈(Bean) 등록
<details>
<summary> SendGridConfig 펼치기 </summary>
                
 ```java
                @Configuration
                public class SendGridConfig {
                
                    @Value("${SENDGRID_API_KEY}")
                    private String sendGridApiKey;
                
                    @Bean
                    public SendGrid sendGrid() {
                        return new SendGrid(sendGridApiKey);
                    }
                }
 ```
</details>
                
- 기존의 **`Gmail SMTP`**를 **`SendGrid API`**로 대체
<details>
<summary> 대체된 네 가지 주요 부분 펼치기 </summary>

![다운로드 (11) (1).png](https://github.com/llRosell/sparta/blob/main/%E1%84%83%E1%85%A1%E1%84%8B%E1%85%AE%E1%86%AB%E1%84%85%E1%85%A9%E1%84%83%E1%85%B3%20(11)%20(1).png?raw=true)
                
![다운로드 (12) (1).png](https://github.com/llRosell/sparta/blob/main/%E1%84%83%E1%85%A1%E1%84%8B%E1%85%AE%E1%86%AB%E1%84%85%E1%85%A9%E1%84%83%E1%85%B3%20(12)%20(1).png?raw=true)
                
![다운로드 (13) (1).png](https://github.com/llRosell/sparta/blob/main/%E1%84%83%E1%85%A1%E1%84%8B%E1%85%AE%E1%86%AB%E1%84%85%E1%85%A9%E1%84%83%E1%85%B3%20(13)%20(1).png?raw=true)
                
![다운로드 (14) (1).png](https://github.com/llRosell/sparta/blob/main/%E1%84%83%E1%85%A1%E1%84%8B%E1%85%AE%E1%86%AB%E1%84%85%E1%85%A9%E1%84%83%E1%85%B3%20(14)%20(1).png?raw=true)

</details>
    
---
    
### 4. 결과
    
- **`Too many login attempts`** 오류 없이 이메일 알림 발송 성공
        
![다운로드 (15) (1).png](https://github.com/llRosell/sparta/blob/main/%E1%84%83%E1%85%A1%E1%84%8B%E1%85%AE%E1%86%AB%E1%84%85%E1%85%A9%E1%84%83%E1%85%B3%20(15)%20(1).png?raw=true)

</details>
</aside>

<aside>

## 🏦비즈니스적 의사결정

<details>
<summary> 사용자 관리를 어떻게 하면 좋을까요? 이메일 인증과 밴 기능을 추가합시다! </summary>
    
  ---

  ### 1. 문제 정의

  현재 서비스의 회원가입 및 로그인 절차가 너무 간단하여, 누구나 쉽게 가입하고 로그인할 수 있습니다.

  이는 애플리케이션 성능에도 직접적인 영향을 미칠 수 있습니다.

  한 가지 예시를 들자면, 서비스에서 제공하는 이메일 알림 기능은 비동기 **`AsyncAwait`** 메커니즘을 활용하여 진행됩니다.

  가입된 모든 사용자에게 알림을 보내면 ‘비동기 + 블로킹 i/o (네트워크 i/o)’ 작업 때문에 가용 스레드가 고갈될 수 있습니다.

  또한, 서비스에서 사용자의 나이와 경력처럼 민감한 정보를 다루기 때문에, 무분별한 아이디-비밀번호 입력 시도를 방지하는 보안 조치도 필요합니다.
    
  ---

  ### 2. 기능 설계, 구현

1. 조건 없는 이메일 알림 발송 → 이메일 알림 인증을 받은 사용자에게만 발송하도록 개선
        - 유효하지 않은 이메일로 발송하는 경우를 차단
2. 회원가입/로그인이 너무 쉬운 구조 → 회원가입 시 이메일 인증을 받도록 개선
        - 이메일 서비스 남용 방지 위해 한 이메일 당 하루 최대 3번으로 제한
        - 인증코드 실패 횟수도 체크하고, 3회 이상 실패 시 코드 무효화
3. 해커의 무분별한 아이디/비밀번호 입력 시도 → 아이피 밴, 이메일 밴 도입
        - 아이피 밴
            - 한 IP에서 4개 이상의 계정에 로그인 시도 시 밴, 시도 실패 정보는 Redis에 저장
            - IPv6을 우선적으로 수집
            - 공유 네트워크를 고려하여 30초만 Redis에 밴 정보 저장
<details>
<summary> AOP 코드 </summary>

```java
                @Slf4j
                @Aspect
                @Component
                @RequiredArgsConstructor
                public class IpBlockingAspect {
                
                    private final KeyValueCommandRepository keyValueCommandRepository;
                    private final KeyValueQueryRepository keyValueQueryRepository;
                
                    private static final String BLOCK_PREFIX = "block:ip:";
                    private static final String LOGIN_ATTEMPT_PREFIX = "attempt:ip:";
                    private static final long IP_BLOCK_DURATION = 30;  //ip 30초 차단
                    private static final long ATTEMPT_TTL = 15;        //15분 동안 시도 기록 유지
                    private static final int MAX_DIFFERENT_EMAILS = 3; //서로 다른 이메일 4개 이상 감지되면 차단(3개까지 허용)
                
                    /**
                     * 서로 다른 이메일 4개 이상 로그인 시도 시 해당 사용자의 ip 를 차단합니다.
                     * 로그인 성공 시에도 시도 기록이 남아있습니다.(로그인 후 재로그인 악용 방지)
                     */
                    @Around("execution(* com.project.cheerha.domain.auth.controller.AuthController.login(..))")
                    public Object blockAbnormalIp(ProceedingJoinPoint joinPoint) throws Throwable {
                        HttpServletRequest request = getRequest();
                        if (request == null) {
                            return joinPoint.proceed();
                        }
                
                        Object[] args = joinPoint.getArgs();
                        CreateLoginRequestDto dto = (CreateLoginRequestDto) args[0];
                        String email = dto.email();
                
                        String ip = IpUtil.getClientIp(request);
                        String redisBlockKey = BLOCK_PREFIX + ip;
                        String redisAttemptKey = LOGIN_ATTEMPT_PREFIX + ip;
                
                        try {
                            return joinPoint.proceed();
                        } catch (Exception e) {
                            //해당 ip 에서 로그인 시도한 이메일 리스트 가져오기
                            List<String> attemptedEmails = keyValueQueryRepository.getListRange(redisAttemptKey, 0, -1);
                            if (attemptedEmails == null || !attemptedEmails.contains(email)) {
                                keyValueCommandRepository.pushToListLeft(redisAttemptKey, email);
                            }
                
                            //추가 시 ttl 설정
                            keyValueCommandRepository.expireValue(redisAttemptKey, ATTEMPT_TTL, TimeUnit.MINUTES);
                
                            //서로 다른 이메일이 3개 이상이면 차단
                            if (!Objects.requireNonNull(attemptedEmails).contains(email) && attemptedEmails.size() >= MAX_DIFFERENT_EMAILS) {
                                keyValueCommandRepository.setValue(redisBlockKey, "blocked", IP_BLOCK_DURATION, TimeUnit.SECONDS);
                                log.warn("IP {} 차단됨 (서로 다른 {}개 이메일 감지됨)", ip, MAX_DIFFERENT_EMAILS + 1);
                                keyValueCommandRepository.removeValue(redisAttemptKey);
                            }
                            throw e;
                        }
                    }
                
                    private HttpServletRequest getRequest() {
                        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                        return attributes != null ? attributes.getRequest() : null;
                    }
                
                }
                
```
</details>

 - 이메일 밴
            - 3일 내에 로그인 5회 이상 실패하면 해당 이메일 밴
            - 실패 정보는 Redis, 밴 정보는 DB에 저장
<details>
<summary> AOP 코드 </summary>

```java
                @Slf4j
                @Aspect
                @Component
                @RequiredArgsConstructor
                public class EmailBlockingAspect {
                
                    private final KeyValueCommandRepository keyValueCommandRepository;
                    private final BannedEmailRepository bannedEmailRepository;
                
                    private static final String FAIL_PREFIX = "fail:email:";
                    private static final long EMAIL_FAIL_DURATION = 3;    //로그인 실패 시 실패데이터 3일간 유지
                    private static final int MAX_FAILED_COUNT = 5;  //5회 실패 시 차단
                    private static final String BAN_MASSAGE = "비밀번호 입력 5회 실패";    //db에 저장되고, 로그에 출력되는 메세지
                
                    /**
                     * 같은 이메일로 5회 이상 로그인 실패한 경우 밴하는 메서드입니다.
                     */
                    @Around("execution(* com.project.cheerha.domain.auth.controller.AuthController.login(..))")
                    public Object blockAbnormalEmail(ProceedingJoinPoint joinPoint) throws Throwable {
                        Object[] args = joinPoint.getArgs();
                        CreateLoginRequestDto dto = (CreateLoginRequestDto) args[0];
                        String email = dto.email();
                        String failCountKey = FAIL_PREFIX + email;
                
                        //차단된 이메일인지 확인
                        if (bannedEmailRepository.existsByEmail(email)) {
                            log.warn("임시차단된 사용자의 로그인 요청: {}", email);
                            throw new UnAuthorizedException(AuthErrorCode.BANNED_EMAIL);
                        }
                
                        try {
                            Object result = joinPoint.proceed(args);
                            //로그인 성공 시 failCount 삭제
                            keyValueCommandRepository.removeValue(failCountKey);
                            return result;
                        } catch (Exception e) {
                            if(Objects.equals(e.getMessage(), "패스워드가 잘못되었습니다.")){
                                //잘못된 비밀번호 입력 시 count 1회 추가, 첫 추가 시 ttl 설정
                                long failedAttempts = keyValueCommandRepository.incrementValue(failCountKey);
                                if (failedAttempts == 1) {
                                    keyValueCommandRepository.expireValue(failCountKey, EMAIL_FAIL_DURATION, TimeUnit.DAYS);
                                }
                
                                //잘못된 시도 5회 시 이메일 차단
                                if (failedAttempts >= MAX_FAILED_COUNT) {
                                    String message = BAN_MASSAGE;
                                    BannedEmail bannedEmail = BannedEmail.toEntity(
                                            email,
                                            message
                                    );
                                    log.warn("email {} 차단 완료 : {}", email, message);
                                    bannedEmailRepository.save(bannedEmail);
                                    keyValueCommandRepository.removeValue(failCountKey);
                                }
                            }
                            throw e;
                        }
                    }
                }
                
```
</details>

4. 이메일 밴을 당한 사용자는 어떻게 재접근할 수 있을까? → 비밀번호 리셋 기능 도입
        - 이메일 인증을 통해 비밀번호 리셋 가능
5. 밴 당한 IP는 모든 API를 사용할 수 없게 전체 차단

    ---

  ### 3. 기능 구현 시 트러블슈팅

  **[문제 발생]** IP 전역 밴을 **`Interceptor`**로 구현하다가 문제가 발생함

![image.png](https://github.com/llRosell/sparta/blob/main/50.png?raw=true)

![image.png](https://github.com/llRosell/sparta/blob/main/52.png?raw=true)

- JWT토큰 관련 필터가 걸린 API에서는 토큰 Exception이 먼저 출력됨

   -. JWT Filter가 인터셉터보다 앞단에 있음을 의미함


![image.png](https://github.com/llRosell/sparta/blob/main/51.png?raw=true)
    
  **[해결 방법]** IP Blocking은 필터체인 사용
    
  - 해당 문제는 **`filter`**는 웹MVC에 존재하고 스프링MVC에 존재하는 **`Interceptor`**보다 전방에 위치했기 때문에 발생함
    
  → IP Blocking을 필터체인에서 가장 우선순위에 두어서 문제를 해결함 

<details>
<summary> IP Blocking Filter </summary>
        
```java
        @Slf4j
        @Component
        @RequiredArgsConstructor
        public class IpBlockingFilter implements Filter {
        
            private final FilterExceptionHandler filterExceptionHandler;
            private final KeyValueQueryRepository keyValueQueryRepository;
        
            private static final String BLOCK_PREFIX = "block:ip:";
        
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
        
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                String ip = IpUtil.getClientIp(httpRequest);
                String redisBlockKey = BLOCK_PREFIX + ip;
        
                if (Boolean.TRUE.equals(keyValueQueryRepository.hasKey(redisBlockKey))) {
                    log.warn("차단된 IP 로그인 시도: {}", ip);
                    filterExceptionHandler.sendErrorResponse(httpResponse, HttpStatus.FORBIDDEN, "30초간 차단된 IP입니다.");
                    return;
                }
        
                chain.doFilter(request, response);
            }
        }
        
```
</details>
    
---
    
### 4. 구현 완료
    
1. 이메일 인증으로 회원가입, 이메일 알림 구독, 비밀번호 리셋 가능
    
![스크린샷 2025-03-12 오후 11.16.03.png](https://github.com/llRosell/sparta/blob/main/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202025-03-12%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%2011.16.03.png?raw=true)
    
2. Kibana의 로그 대시보드로 이상 사용자의 IP를 확인하고, 지속적으로 올바르지 않은 요청을 보낸 IP는 WAF(Web Application Firewall)에서 차단 가능
    
![스크린샷 2025-03-12 오후 10.35.06.png](https://github.com/llRosell/sparta/blob/main/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202025-03-12%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%2010.35.06.png?raw=trueE)
    
---
    
### 5. 영향
    
- **긍정적 영향**
        - **무분별한 공격에 대응 가능 →** 완벽하지는 않으나, 해커의 무분별한 공격을 막을 수 있음
        - **SendGrid API 횟수 조절 가능 →** SendGrid API 사용 횟수를 합리적으로 조절할 수 있음
- **부정적 영향**
        - **단체 IP 밴 가능성 존재 →** 공용 IP에서 동시에 4명 이상 접속 시 단체로 IP 밴에 걸릴 수 있음
    
### **6. 향후 고려 사항**
    
    - **추가 대책 마련:** IP 밴은 30초밖에 안 되므로, 해커가 공용 IP를 사용할 때 대응책 마련 필요

</details>

<details>
<summary> 채용 공고에서 중요한 제목을 최우선으로, 검색순위를 설정하면 어떨까요? </summary> 
    
  ---

  ### 1. 배경

  기존 채용 공고 검색 시스템에서는 채용 공고의 제목, 회사명, 자격요건 등의 필드에 동일한 우선순위가 적용되어 있었습니다.

  이로 인해 검색 결과에서 중요한 정보인 제목이 검색 우선순위에서 낮아질 수 있었고, 사용자가 원하는 결과를 빠르게 찾기 어려웠습니다.

  따라서 검색 결과에서 중요한 정보인 제목을 더 높은 우선순위로 설정하여, 사용자가 보다 효율적으로 원하는 정보를 찾을 수 있도록 개선이 필요했습니다.
    
  ---

  ### 2. 요구사항

  - 검색 결과의 우선순위 설정: 채용 공고 검색 시, 중요한 필드인 제목을 가장 높은 우선순위로 설정
  - 검색 품질 향상: 중요한 필드들이 상위에 노출되어 사용자가 보다 빠르게 원하는 검색 결과를 찾을 수 있도록 함

    ---

  ### 3. 고려한 대안

  | **비교 항목** | **기존 검색 시스템** | **Boost 기능 적용 후** |
  | --- | --- | --- |
  | **우선순위 설정** | ❌ 모든 필드에 동일한 우선순위 적용 | ✅ 각 필드에 점수를 부여하여 우선순위 적용 |
  | **검색 품질** | ❌ 동일한 우선순위로 인해 중요한 정보 노출 부족 | ✅ 중요한 정보(제목)가 먼저 노출되어 검색 품질 향상 |
    
  ---

  ### 4. 결정 및 근거

  ✅Elasticsearch의 Boost 기능을 적용

  - Title 필드
        - `boost(2.0f)`를 사용하여 다른 필드들보다 더 높은 우선순위를 부여했습니다.
        - 제목이 가장 중요한 정보로 취급되도록 설정했습니다.
  - Company 필드
        - `boost(1.5f)`를 적용하여 제목보다 낮지만 여전히 중요한 역할을 할 수 있도록 했습니다.
        - 회사명이 검색에 포함될 때 해당 필드는 중요한 정보를 제공하게 됩니다.
  - RequiredSkills 필드
        - `boost(1.0f)`를 설정하여 회사명보다 낮은 우선순위지만 여전히 검색에 중요한 요소로 취급되도록 했습니다.
<details>
<summary> 우선순위 검색 예시 사진 </summary>

![Spring 으로 검색 시](https://github.com/llRosell/sparta/blob/main/Spring%20%E1%84%8B%E1%85%B3%E1%84%85%E1%85%A9%20%E1%84%80%E1%85%A5%E1%86%B7%E1%84%89%E1%85%A2%E1%86%A8%20%E1%84%89%E1%85%B5.png?raw=true)

Spring 으로 검색 시

![Java 로 검색 시](https://github.com/llRosell/sparta/blob/main/Java%20%E1%84%85%E1%85%A9%20%E1%84%80%E1%85%A5%E1%86%B7%E1%84%89%E1%85%A2%E1%86%A8%20%E1%84%89%E1%85%B5.png?raw=true)

Java 로 검색 시

</details>

  ---
    
  ### 5. 코드
    
   - 1순위 제목/ 2순위 회사명/3순위 자격요건 순으로 우선순위를 처리했습니다.
        
![수정 전 서치텀에서 제목만 검색이 되는 코드](https://github.com/llRosell/sparta/blob/main/%E1%84%89%E1%85%AE%E1%84%8C%E1%85%A5%E1%86%BC%20%E1%84%8C%E1%85%A5%E1%86%AB%20%E1%84%89%E1%85%A5%E1%84%8E%E1%85%B5%E1%84%90%E1%85%A5%E1%86%B7%E1%84%8B%E1%85%A6%E1%84%89%E1%85%A5%20%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8%E1%84%86%E1%85%A1%E1%86%AB%20%E1%84%80%E1%85%A5%E1%86%B7%E1%84%89%E1%85%A2%E1%86%A8%E1%84%8B%E1%85%B5%20%E1%84%83%E1%85%AC%E1%84%82%E1%85%B3%E1%86%AB%20%E1%84%8F%E1%85%A9%E1%84%83%E1%85%B3.png?raw=true)
        
   수정 전 서치텀에서 제목만 검색이 되는 코드
        
![수정 후 우선순위를 매겨 서치텀에서 제목, 회사명, 자격요건이 함께 검색되도록 함](https://github.com/llRosell/sparta/blob/main/%E1%84%89%E1%85%AE%E1%84%8C%E1%85%A5%E1%86%BC%20%E1%84%92%E1%85%AE%20%E1%84%8B%E1%85%AE%E1%84%89%E1%85%A5%E1%86%AB%E1%84%89%E1%85%AE%E1%86%AB%E1%84%8B%E1%85%B1%E1%84%85%E1%85%B3%E1%86%AF%20%E1%84%86%E1%85%A2%E1%84%80%E1%85%A7%20%E1%84%89%E1%85%A5%E1%84%8E%E1%85%B5%E1%84%90%E1%85%A5%E1%86%B7%E1%84%8B%E1%85%A6%E1%84%89%E1%85%A5%20%E1%84%8C%E1%85%A6%E1%84%86%E1%85%A9%E1%86%A8,%20%E1%84%92%E1%85%AC%E1%84%89%E1%85%A1%E1%84%86%E1%85%A7%E1%86%BC,%20%E1%84%8C%E1%85%A1%E1%84%80%E1%85%A7%E1%86%A8%E1%84%8B%E1%85%AD%E1%84%80%E1%85%A5%E1%86%AB%E1%84%8B%E1%85%B5%20%E1%84%92%E1%85%A1%E1%86%B7%E1%84%81%E1%85%A6%20%E1%84%80%E1%85%A5%E1%86%B7%E1%84%89%E1%85%A2%E1%86%A8%E1%84%83%E1%85%AC%E1%84%83%E1%85%A9%E1%84%85%E1%85%A9%E1%86%A8%20%E1%84%92%E1%85%A1%E1%86%B7.png?raw=true)
        
  수정 후 우선순위를 매겨 서치텀에서 제목, 회사명, 자격요건이 함께 검색되도록 함
        
    
  ---
    
  ### 6. 향후 고려 사항
    
  - 추가 필드 확장: 다른 필드들에 대해 우선순위를 추가로 설정하여 검색 정확도와 효율성을 높일 수 있습니다. 예를 들어, 채용 공고 날짜나 급여 조건을 필터링하여 우선순위를 설정할 수 있습니다.

</details>

<details>
<summary> 비정형 데이터로도 정확한 검색 결과를 얻을 수 있으면 어떨까요? </summary> 
    
  ---

  ### 1. 배경

  MySQL 기반의 채용 공고 검색 시스템은 잘못된 단어를 입력하면 제대로 된 검색 결과를 제공하지 못하는 문제가 발생하여 사용자 경험이 저하되었습니다.

  따라서 오타(철자 오류, 띄어쓰기 실수)나 변형된  단어(복수형, 어미 변화) 등의 비정형 데이터도 검색이 되도록 하여 사용자 편의를 개선할 필요가 있었습니다.
    
  ---

  ### 2. 요구사항

  - **오타 처리 기능:** 사용자가 오타가 포함된 검색어를 입력해도 정확한 검색 결과를 제공할 수 있어야 함
  - **검색 편의성 향상:** 오타나 변형된 단어를 포함한 유사 단어도 검색 결과에 포함시킬 수 있어야 함
  - **사용자 편의성:** 사용자가 오타나 변형된 단어를 포함한 검색어로도 원하는 결과를 빠르게 얻을 수 있도록 해야 함

    ---

  ### 3. 고려한 대안

  | **비교 항목** | **기존 검색 시스템 (정확한 일치)** | **Fuzzy 기능 적용 (오타 검색)** |
  | --- | --- | --- |
  | **비정형 데이터 처리** | ❌ 오타나 변형된 단어가 있으면 검색 결과 미제공 | ✅ 오타나 변형된 단어가 있어도 유사한 단어로 검색 가능 |
  | **검색 편의성** | ❌ 정확한 일치만 처리, 편의성 저하 | ✅ 오타 및 변형된 단어와 유사한 단어를 포함한 검색 결과 제공 |
  | **사용자 편의성** | ❌ 오타나 복수형 단어의 데이터를 검색 결과에 포함시키지 못함 | ✅ 오타나 복수형 단어를 검색어에 포함시켜 정확한 검색 결과 제공 |
    
  ---

  ### 4. 결정 및 근거

  **✅Elasticsearch의 Fuzzy 기능 적용**

  - 사용자가 실수로 오타(철자 오류, 띄어쓰기 실수)를 입력해도 원하는 결과를 얻을 수 있음
  - 사용자가 변형된 단어(복수형, 어미변화)를 입력해도 원하는 결과를 얻을 수 있음

<details>
<summary> 예시 사진 모음 </summary>

   (1) 머비스로 검색 시 모비스를 찾아옴

![](https://github.com/llRosell/sparta/blob/main/11.png?raw=true)

   (2) 모부스로 검색 시 모비스를 찾아옴

![](https://github.com/llRosell/sparta/blob/main/12.png?raw=true)

   (3) 쿠카오로 검색 시 카카오를 찾아옴

![](https://github.com/llRosell/sparta/blob/main/13.png?raw=true)

   (4) 나이버로 검색 시 네이버를 찾아옴

![](https://github.com/llRosell/sparta/blob/main/14.png?raw=true)

   (5) 네이바로 검색 시 네이버를 찾아옴

![](https://github.com/llRosell/sparta/blob/main/15.png?raw=true)

</details>

  ---
    
  ### 5. 코드
    
  - 기본 검색 (must 사용)
        
![](https://github.com/llRosell/sparta/blob/main/16.png?raw=true)
        
  - **`Fuzzy`** 기능 사용
        - **`fuzziness`**: 자동으로 오타 및 변형된 단어를 감지하고 유사한 단어를 검색 결과에 포함하도록 설정
        - **`should`** 조건: 하나 이상의 조건이 만족되면 결과가 반환되도록 조건 추가
        
![](https://github.com/llRosell/sparta/blob/main/17.png?raw=true)
        
    
  ---
    
  ### 6. 향후 고려 사항
    
  - **고급 오타 분석:** 사용자의 오타 유형을 분석하여 검색 성능 개선 방법 마련
        - 예시) 특정 단어의 오타 패턴을 미리 정의
  - **다양한 언어 지원 고려:** 다국어 형태소 분석 및 언어별 오타 처리 고려

</details>

<details>
<summary> 자동 완성 기능을 분리하면 원하는 채용 공고만 조회할 수 있을까요? </summary> 

  ---
    
  ### 1. 배경
    
  부분 검색과 자동 완성 검색이 동일한 로직에서 수행되면서 불필요한 검색 결과가 반환되었습니다. 
  사용자의 의도와 맞지 않는 검색 결과가 포함되었습니다.
  자동 완성 검색을 독립적인 API로 분리하여 더 정확한 검색 결과를 제공할 필요가 있었습니다.
    
  ---
    
  ### 2. 요구사항
    
  - **검색 성능 최적화**: 불필요한 검색 결과 제거해야 함
  - **자동 완성 기능 개선**: 입력된 검색어와 관련된 검색 결과를 빠르게 제공해야 함
  - **사용자 경험 개선**: 정확한 검색어를 입력하지 않아도 원하는 검색 결과를 제공해야 함
    
  ---
    
   ### 3. 고려한 대안
    
   | **비교 항목** | **현재 로직 유지** | **자동 완성 검색 로직 분리** |
   | --- | --- | --- |
   | **검색 성능 최적화** | ❌ 부분 검색과 자동 완성이 동일한 로직에서 수행되어 불필요한 결과 포함 | ✅ 자동 완성 전용 API를 통해 더 정확한 검색 결과 제공 |
   | **자동 완성 기능 개선** | ❌ 자동 완성 기능이 부분 검색과 섞여 있어 추천 정확도 낮음 | ✅ 검색어 입력하면 적절한 자동 완성 검색 결과 제공 |
   | **사용자 경험 개선** | ❌ 검색어 입력 시 원하는 결과를 찾기 어려움 | ✅ 검색어를 입력하면 적절한 자동 완성 검색 결과 제공 가능 |
    
  ---
    
  ### 4. 결정 및 근거
    
  **✅ 자동 완성 검색 로직 분리 결정**
    
  - 자동 완성 기능 개선으로 검색 시 적절한 자동 완성 검색 결과 제공 가능
  - 검색어 입력 시 적절한 검색 결과를 제공으로 사용자 경험 개선 가능
<details>
<summary> 예시 사진 모음 </summary>
            
   (1) “구”로 검색 시 “구”로 시작하는 채용 공고를 찾아옴
            
![image.png](https://github.com/llRosell/sparta/blob/main/18.png?raw=true)
            
   (2) “하”로 검색 시 “하”로 시작하는 채용 공고를 찾아옴
            
![image.png](https://github.com/llRosell/sparta/blob/main/19.png?raw=true)

</details>
  ---
    
  ### 5. 코드

<details>
<summary> JobOpeningDocumentController </summary>
        
```java
        // 부분 검색 API
        @GetMapping("/search/elastic/filters")
        public ResponseEntity<ApiResponseDto<Page<ReadJobOpeningElasticResponseDto>>> readJobOpeningElasticsearch(
            @ModelAttribute ReadJobOpeningElasticRequestDto requestDto,
            @Auth AuthUser authUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
        ) {
            Pageable pageable = validatePageSize(page, size);
            Long userId = authUser.id();
        
            Page<ReadJobOpeningElasticResponseDto> jobOpeningElasticResponseDtoPage = jobOpeningDocumentService.readJobOpeningUsingElasticSearchFilter(requestDto, userId, pageable);
        
            return ApiResponseDto.success(jobOpeningElasticResponseDtoPage);
        }
        
        // 자동 완성 검색 API
        @GetMapping("/search/elastic/auto")
        public ResponseEntity<ElasticApiResponseDto<Page<ReadJobOpeningElasticAutoResponseDto>>> readJobOpeningElasticAuto(
            @ModelAttribute ReadJobOpeningElasticAutoRequestDto requestDto,
            @Auth AuthUser authUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
        ) {
            Pageable pageable = validatePageSize(page, size);
            Long userId = authUser.id();
            Page<ReadJobOpeningElasticAutoResponseDto> jobOpeningElasticResponseDtoPage = jobOpeningDocumentService.readJobOpeningElasticAuto(requestDto, userId, pageable);
            int totalItems = (int) jobOpeningElasticResponseDtoPage.getTotalElements();
            String message = "채용공고 " + totalItems + "개가 조회되었습니다.";
            return ElasticApiResponseDto.success(jobOpeningElasticResponseDtoPage, message);
        }
```
</details>
    
   ---
    
   ### 6. 향후 고려 사항
    
  - **`edge n-gram` 분석기의 최적 범위 조정:** 불필요한 토큰이 생성되지 않도록 적절한 크기 설정

</details>

<details>
<summary> 스팸처럼 보이지 않도록 채용 공고는 딱 20개만! 선정 기준은 무엇이 좋을까요? </summary>

  ### 1. 배경

  기존 로직은 기술 요건이 하나라도 일치하는 채용 공고를 전부 이메일 안에 포함하여 사용자가 이메일을 읽을 때 불편함을 느낄 수 있었습니다.

  채용 공고 목록이 지나치게 길어질수록 이메일이 스팸으로 여겨질 위험이 커졌습니다.

  사용자에게 의미 있는 이메일 알림을 발송할 수 있도록 채용 공고 수를 제한해야 했습니다.

  이러한 이유로 시간 복잡도, 수치화 가능 여부, 사용자 맞춤화 정도를 고려하여 선정 기준을 정했습니다.
    
  ---

  ### 2. 요구사항

  - **작은 시간 복잡도**: 채용 공고를 20개 추출하는 데 시간 복잡도가 작아야 함
  - **수치화 가능 여부**: 내림차순으로 정렬해야 하므로 숫자로 변환할 수 있어야 함
  - **사용자 맞춤화**: 사용자의 수요를 잘 반영할 수 있어야 함

    ---

  ### 3. 고려한 대안

  | **비교 항목** | **기술 요건이 겹치는 순** | **연봉이 높은 순** | **무작위 순** |
  | --- | --- | --- | --- |
  | **시간 복잡도** | ❌ 세제곱까지 커질 수 있음 | ✅ 이미 저장된 값을 조회해오기 때문에 상대적으로 작음 | ✅ 가장 작음 |
  | **수치화 가능 여부** | ✅ 가능 | ❌ ‘면접 후 결정’이 대부분이라 수치화 | ✅ 수치화할 필요 없음 |
  | **사용자 맞춤화** | ✅ 사용자가 선택한 기술 요건 반영 가능 | ✅ 인기 있는 채용 공고 전달 가능 | ❌ 사용자의 선호 반영 불가능 |
  | **단점** | ⚠️ 겹치는 횟수를 계산하는 로직이 복잡할 수 있음 | ⚠️ 희망 연봉과 맞지 않는 채용 공고가 포함될 수 있음 | ⚠️ 사용자 관심 충족이 어려움 |
    
  ---

  ### 4. 결정 및 근거

  **✅ 무작위 순 선택**

  - 수치화할 필요가 없고 이메일 알림 기능에 빠르게 적용 가능
  - 시간 복잡도가 가장 작아서 서버에 부담을 주지 않음
  - 사용자 맞춤화는 부족하지만, 대기업부터 중견 기업까지 모든 채용 공고에 알림으로 전달될 기회를 공평하게 제공할 수 있음

---

  ### 5. 코드

<details>
<summary> 1. 데이터베이스에 저장된 알림을 무작위로 섞음 </summary>

```java
      private void sendNotificationEmail(
          String recipientEmail,
          Set<Notification> notificationSet
      ) {
          try {
              List<Notification> notificationList = new ArrayList<>(notificationSet);
                  
              Collections.shuffle(notificationList);
```
</details>

<details>
<summary> 2. 무작위로 섞인 알림 목록을 20개로 제한 </summary>

```java
              notificationList = notificationList.stream()
                  .limit(20)
                  .toList();
```

</details>

<details>
<summary> 3. 알림 20개로 이메일 내용 구성 후 발송 </summary>

```java
              String[] emailData = NotificationFormat.createEmailNotification(notificationList);
              String subject = emailData[0];
              String content = emailData[1];
      
              emailSender.send(recipientEmail, subject, content);
```

</details>

<details>
<summary> 4. 발송된 알림 상태를 false에서 true로 변경 </summary>

```java
              notificationList.forEach(notification -> {
                  notification.markEmailAsSent();
                  notificationRepository.save(notification);
              });
      
          } catch (IOException e) {
              log.error("이메일 전송 실패: {}", recipientEmail, e);
          }
        }
      }
```
</details>

  ---
    
  ### 6. 향후 고려 사항
    
  - **기술 요건이 겹치는 순 적용**: 시간 복잡도를 줄인 로직 구현

<details>
<summary> 코드 펼치기 </summary>
            
```java
            private Map<String, Set<Long>> invertKeywordIdToUrlList(
                Map<Long, List<String>> keywordIdToUrlList) {
                Map<String, Set<Long>> urlToKeywordIdSet = new HashMap<>();
            
                keywordIdToUrlList.forEach((keywordId, urlList) -> {
                    for (String url : urlList) {
                        urlToKeywordIdSet
                            .computeIfAbsent(url, urlAsKey -> new HashSet<>())
                            .add(keywordId);
                    }
                });
            
                return urlToKeywordIdSet;
            }
            
            private Map<String, Set<Long>> invertEmailToKeywordIdList(
                List<NotificationRecipientDto> notificationRecipientDtoList) {
                Map<String, Set<Long>> emailToKeywordIdSet = new HashMap<>();
            
                notificationRecipientDtoList.forEach(dto -> {
                    emailToKeywordIdSet
                        .computeIfAbsent(dto.email(), emailAsKey -> new HashSet<>())
                        .add(dto.keywordId());
                });
            
                return emailToKeywordIdSet;
            }
            
            private Map<String, Map<String, Long>> compareKeywordOverlap(
                Map<String, Set<Long>> emailToKeywordIdSet,
                Map<String, Set<Long>> urlToKeywordIdSet
            ) {
                Map<String, Map<String, Long>> emailToUrlToOverlapCount = new HashMap<>();
            
                Set<String> emailSet = emailToKeywordIdSet.keySet();
                Set<String> urlSet = urlToKeywordIdSet.keySet();
            
                for (String email : emailSet) {
                    Set<Long> userKeywords = emailToKeywordIdSet.get(email);
            
                    for (String url : urlSet) {
                        Set<Long> jobOpeningKeywords = urlToKeywordIdSet.get(url);
            
                        long overlapCount = userKeywords.stream()
                            .filter(jobOpeningKeywords::contains)
                            .count();
            
                        if (overlapCount > 0) {
                            emailToUrlToOverlapCount
                                .computeIfAbsent(email, emailAsKey -> new HashMap<>())
                                .put(url, overlapCount);
                        }
                    }
                }
            
                return emailToUrlToOverlapCount;
            }
```
</details>

  - **AI 적용**: 연봉이나 지역, 경력 같은 필터링 기준을 도입
</details>
</aside>

<aside>

## 🏭’취하여’ 팀의 리팩토링

<details>
<summary> 객체 지향 프로그래밍 </summary> 

<details>
<summary>  이메일 발송 서비스 리팩토링! </summary>

## 1. 리팩토링 전

<details>
<summary> 리팩토링 전 코드 </summary>

```java
            @Slf4j
            @Service
            @RequiredArgsConstructor
            public class EmailSender {
            
                private final UserToJobOpeningMappingRepository userToJobOpeningMappingRepository;
            
                @Value("${SENDGRID_API_KEY}")
                private String sendGridApiKey;
            
                @Value("${SENDGRID_FROM_EMAIL}")
                private String senderEmail;
            
                @Async
                public void sendEmails() {
                    // 이메일별로 해당하는 Mapping들을 묶는 Map
                    Map<String, Set<UserToJobOpeningMapping>> emailToMappings = new HashMap<>();
            
                    // (1) 이메일로 발송되지 않은 Mapping 목록 조회
                    // (2) 이메일별로 emailToMappings에 Mapping을 묶음
                    userToJobOpeningMappingRepository.findByIsEmailSentFalse()
                        .forEach(mapping -> {
                            emailToMappings.computeIfAbsent(
                                mapping.getEmail(),
                                emailAsKey -> new HashSet<>()
                            ).add(mapping);
                            // 해당 이메일에 맞는 Mapping을 Set<Mapping>에 추가
                        });
            
                    // 이메일 주소별로 이메일 발송
                    emailToMappings.forEach(this::sendMail);
                }
            
                // 이메일 발송
                private void sendMail(
                    String recipientEmail,
                    Set<UserToJobOpeningMapping> userToJobOpeningMappings
                ) {
                    try {
                        // 이메일 발송에 필요한 정보 설정
                        // from: 보내는 사람 이메일 주소
                        // to: 받는 사람 이메일 주소
                        Email from = new Email(senderEmail);
                        Email to = new Email(recipientEmail);
                        String subject = "📢 새로운 맞춤 채용 공고가 도착했어요!";
                        StringBuilder content = new StringBuilder();
            
                        // 내용 추가
                        content.append("<h1>🚀 새로운 채용 공고가 준비됐어요! 🎉</h1>");
                        content.append("<p>맞춤형 채용 공고가 도착했답니다! 💼</p>");
                        content.append("<p>아래 링크에서 확인해보세요! ⬇️</p>");
                        content.append("<ul>");
            
                        // Mapping에 저장된 채용 공고 URL 목록을 내용에 추가
                        for (UserToJobOpeningMapping userToJobOpeningMapping : userToJobOpeningMappings) {
                            content.append("<li>👉 <a href=\"")
                                .append(userToJobOpeningMapping.getJobOpeningUrl())
                                .append("\" target=\"_blank\">")
                                .append("채용 공고 자세히 보기</a></li>");
                        }
            
                        content.append("</ul>");
                        content.append("<p>행운을 빕니다! 🙌</p>");
            
                        // 내용 설정
                        Content emailContent = new Content(
                            "text/html", // HTML 형식의 이메일
                            content.toString() // 작성된 내용
                        );
            
                        // SendGrid Mail 객체 생성
                        sendSendGridEmail(from, subject, to, emailContent);
            
                        log.info("이메일 전송 완료: {}", recipientEmail);
            
                        userToJobOpeningMappings.forEach(mapping -> {
                            mapping.markEmailAsSent(); // 발송 완료 상태로 변경
                            userToJobOpeningMappingRepository.save(mapping); // 변경된 상태 저장
                        });
            
                    } catch (IOException e) {
                        log.error("이메일 전송 실패: {}", recipientEmail, e);
                    }
                }
            
                public void sendVerificationEmail(String recipientEmail, String code) {
                    try {
                        Email from = new Email(senderEmail);
                        Email to = new Email(recipientEmail);
                        String subject = "이메일 인증 코드";
                        String content = "<p>인증 코드: <strong>" + code + "</strong></p>";
                        Content emailContent = new Content("text/html", content);
                        sendSendGridEmail(from, subject, to, emailContent);
            
                        log.info("인증 코드 이메일 전송 완료: {}", recipientEmail);
                    } catch (IOException e) {
                        log.error("인증 코드 이메일 전송 실패: {}", recipientEmail, e);
                    }
                }
            
                private void sendSendGridEmail(Email from, String subject, Email to, Content emailContent) throws IOException {
                    Mail mail = new Mail(from, subject, to, emailContent);
            
                    SendGrid sendGrid = new SendGrid(sendGridApiKey);
                    Request request = new Request();
                    request.setMethod(Method.POST);
                    request.setEndpoint("mail/send");
                    request.setBody(mail.build());
            
                    sendGrid.api(request);
                }
            }
 ```
</details>

 ---
        
## 2. 현재 코드의 문제점
        
### ⚠️**SRP 위반!**
        
        - 이메일 알림 생성, SendGrid API 호출 등 한 클래스가 너무 많은 책임을 집니다.
        - 알림 발송용과 사용자 인증용 이메일 발송을 한 클래스에서 동시에 수행합니다.
        
### ⚠️**OCP 위반!**
        
        - 다른 이메일 서비스를 추가하기 어렵습니다.
        
### ⚠️**DIP 위반!**
        
        - SendGrid와 강하게 결합되어 다른 서비스로 이전하기 힘듭니다.
        
---
        
 ## 3. 리팩토링 과정
        
♻️책임을 분리합시다!
        
- Notification, Verification Email Sender를 분리합시다!

<details>
<summary> VerificationEamilSender </summary>
                 
```java
                @Slf4j
                @Service
                @RequiredArgsConstructor
                public class VerificationEmailSender {
                
                    private final EmailSender emailSender;
                
                    public void sendVerificationEmail(String recipientEmail, String code) {
                        try {
                            String[] emailData = VerificationFormat.createVerification(code);
                            String subject = emailData[0];
                            String content = emailData[1];
                
                            emailSender.send(recipientEmail, subject, content);
                
                        } catch (IOException e) {
                            log.error("인증 코드 이메일 전송 실패: {}", recipientEmail, e);
                        }
                    }
                }
```
</details>

- Email 발송 내용도 static으로 분리합시다!

<details>
<summary> VerificationFormat </summary>
                
```java
                public class VerificationFormat {
                
                    public static String[] createVerification (String code) {
                        String subject = "이메일 인증 코드";
                        String content = "<p>인증 코드: <strong>" + code + "</strong></p>";
                
                        return new String[] {subject, content};
                    }
                }
```
</details>                
        
♻️다른 이메일 API를 사용해야 한다면?
        
- 이메일을 실제 발송하는 클래스를 추상화하고, 이를 API가 구현하게 해볼까요?
<details>
<summary> SendGridEmailSender </summary>
                
```java
                @Slf4j
                @Component
                @RequiredArgsConstructor
                public class SendGridEmailSender implements EmailSender {
                
                    @Value("${SENDGRID_API_KEY}")
                    private String sendGridApiKey;
                
                    @Value("${SENDGRID_FROM_EMAIL}")
                    private String senderEmail;
                
                    /**
                     * 이메일을 SendGrid로 전송
                     *
                     * @param recipientEmail 수신자 이메일
                     * @param subject        이메일 제목
                     * @param content        이메일 본문 (HTML)
                     * @throws IOException 이메일 전송 시 발생할 수 있는 예외
                     */
                    @Override
                    public void send(String recipientEmail, String subject, String content) throws IOException {
                        Email from = new Email(senderEmail);
                        Email to = new Email(recipientEmail);
                        Content emailContent = new Content("text/html", content);
                
                        Mail mail = new Mail(from, subject, to, emailContent);
                        SendGrid sendGrid = new SendGrid(sendGridApiKey);
                        Request request = new Request();
                        request.setMethod(Method.POST);
                        request.setEndpoint("mail/send");
                        request.setBody(mail.build());
                
                        sendGrid.api(request);
                
                        log.info("이메일 전송 완료: {}", recipientEmail);
                    }
                }
                
```
</details>           
        
♻️새로운 이메일 서비스를 추가한다면, 추상화 된 객체를 호출하면 돼요!

<details>
<summary> EmailSender </summary>
            
```java
            import java.io.IOException;
            
            public interface EmailSender {
                void send(String recipientEmail, String subject, String content) throws IOException;
            }
            
```
</details>        
        
---
        
## 4. 리팩토링 결과
        
✅새로운 이메일 서비스를 추가하기도, 다른 API를 사용하기도 쉬운 구조가 되었어요!
        
✅코드의 가독성이 좋아졌어요!
        
✅단위 테스트를 수행하기 편해졌어요!

</details>

<details>
<summary> 데이터 마이그레이션 서비스 리팩토링! </summary>

## 1. 리팩토링 전

<details>
<summary> 리팩토링 전 코드 </summary>
            
```java
            @Slf4j
            @Service
            @RequiredArgsConstructor
            public class MysqlToElasticsearchSyncService {
            
                private final JobOpeningRepository jobOpeningRepository;
                private final JobOpeningDocumentRepository jobOpeningDocumentRepository;
            
                private boolean isFirstExecutionDone = false;  // 최초 실행 여부를 체크하는 변수
            
                /**
                 * 매일 자정에 실행되도록 하는 스케줄러입니다.
                 * 이 메서드는 매일 자정(00:00)마다 주기적으로 실행됩니다.
                 *
                 * 최초 실행 시 데이터 동기화를 한 번 실행한 후, 이후에는 정해진 주기에 맞춰
                 * MySQL 데이터를 Elasticsearch로 동기화하는 작업이 계속해서 실행됩니다.
                 *
                 * @Scheduled(cron = "0 0 0 * * *")
                 * 매일 자정에 반복적으로 실행됩니다.
                 */
                @Scheduled(cron = "0 0 0 * * *")  // 매일 자정(00:00) 실행
                @Transactional
                public void syncDataToElasticsearch() {
                    try {
                        // 최초 실행 여부 확인
                        if (!isFirstExecutionDone) {
                            log.info("최초 한 번 실행되는 데이터 동기화 시작...");
                            // 최초 실행 시 데이터 동기화
                            syncDataOnce();
                            isFirstExecutionDone = true;  // 최초 실행 완료 플래그 설정
                        } else {
                            log.info("주기적으로 실행되는 데이터 동기화 시작...");
                            // 이후 매일 자정에 주기적으로 실행
                            syncDataOnce();
                        }
                    } catch (Exception e) {
                        log.error("데이터 동기화 중 오류 발생", e);
                    }
                }
            
                /**
                 * MySQL에서 채용 공고 데이터를 조회하여 Elasticsearch에 동기화하는 메서드입니다.
                 *
                 * 이 메서드는 MySQL에서 채용 공고와 관련된 키워드를 포함한 데이터를 가져오고,
                 * 이를 Elasticsearch 형식에 맞게 변환하여 저장합니다.
                 *
                 * 1. MySQL에서 모든 채용 공고 데이터를 조회합니다.
                 * 2. 각 채용 공고에 대해 관련된 키워드를 추출하여 `requiredSkills` 리스트에 추가합니다.
                 * 3. `JobOpeningDocument` 객체로 변환하고 Elasticsearch에 저장합니다.
                 *
                 * @throws Exception 데이터 동기화 중 오류가 발생할 경우 예외를 발생시킵니다.
                 */
                private void syncDataOnce() {
                    try {
                        List<JobOpening> jobOpeningList = jobOpeningRepository.findAllWithJobOpeningKeywords();  // 적절한 ID 또는 쿼리 매개변수를 사용하여 조회
                        log.info("총 {}개의 JobOpening 데이터를 조회했습니다.", jobOpeningList.size());
            
                        List<JobOpeningDocument> jobOpeningDocuments = new ArrayList<>();
            
                        // 각 JobOpening에 대해 키워드 리스트를 추출하여 requiredSkills에 추가
                        for (JobOpening jobOpening : jobOpeningList) {
                            List<String> requiredSkills = new ArrayList<>();
                            for (JobOpeningKeyword jobOpeningKeyword : jobOpening.getJobOpeningKeywordList()) {
                                Keyword keyword = jobOpeningKeyword.getKeyword();
                                if (keyword != null) {
                                    requiredSkills.add(keyword.getName());
                                }
                            }
            
                            // JobOpening을 JobOpeningDocument로 변환
                            JobOpeningDocument jobOpeningDocument = JobOpeningDocument.create(jobOpening);
                            jobOpeningDocument.getRequiredSkills().addAll(requiredSkills);
                            jobOpeningDocuments.add(jobOpeningDocument);
                        }
            
                        // Elasticsearch에 데이터 저장
                        jobOpeningDocumentRepository.saveAll(jobOpeningDocuments);
                        log.info("Mysql 데이터가 Elasticsearch에 성공적으로 동기화되었습니다");
            
                    } catch (Exception e) {
                        log.error("데이터 동기화 중 오류 발생", e);
                    }
                }
            }
            
```
</details>        
        
---
        
## 2. 현재 코드의 문제점
        
### ⚠️**SRP 위반!**
        
- 스케줄러 및 실제로 데이터를 동기화하는 메서드가 한 클래스에 있습니다.
        
### ⚠️**DIP 위반!**
        
- ‘데이터를 동기화하는 작업’ 자체가 Elasticsearch와 강하게 결합되어 있습니다.
- 다른 서비스로 이전하기가 힘듭니다.
        
### ⚠️OCP, ISP 위반!
        
- 데이터 동기화는 스케줄러에서도, API 호출로도 가능하므로 추상화한다면 재사용하기 편리합니다.
        
---
        
## 3. 리팩토링 과정
        
♻️우선, 추상화합시다!
        
- 동기화 메서드를 추상화하면 어디서든 고수준의 인터페이스를 호출하므로 결합이 느슨해집니다.
<details>
<summary> DataSync </summary>
                
```java
                public interface DataSync {
                
                    void sync();
                }
```
</details> 

<details>
<summary> MysqlToElasticsearchDataSync </summary>
                
```java
                @Slf4j
                @Component
                @RequiredArgsConstructor
                public class MysqlToElasticsearchDataSync implements DataSync {
                
                    private final JobOpeningRepository jobOpeningRepository;
                    private final JobOpeningDocumentRepository jobOpeningDocumentRepository;
                
                    /**
                     * MySQL에서 채용 공고 데이터를 조회하여 Elasticsearch에 동기화하는 메서드입니다.
                     *
                     * 이 메서드는 MySQL에서 채용 공고와 관련된 키워드를 포함한 데이터를 가져오고,
                     * 이를 Elasticsearch 형식에 맞게 변환하여 저장합니다.
                     *
                     * 1. MySQL에서 모든 채용 공고 데이터를 조회합니다.
                     * 2. 각 채용 공고에 대해 관련된 키워드를 추출하여 `requiredSkills` 리스트에 추가합니다.
                     * 3. `JobOpeningDocument` 객체로 변환하고 Elasticsearch에 저장합니다.
                     */
                    @Override
                    public void sync() {
                        try {
                            List<JobOpening> jobOpeningList = jobOpeningRepository.findAllWithJobOpeningKeywords();  // 적절한 ID 또는 쿼리 매개변수를 사용하여 조회
                            log.info("총 {}개의 JobOpening 데이터를 조회했습니다.", jobOpeningList.size());
                
                            List<JobOpeningDocument> jobOpeningDocuments = new ArrayList<>();
                
                            // 각 JobOpening에 대해 키워드 리스트를 추출하여 requiredSkills에 추가
                            for (JobOpening jobOpening : jobOpeningList) {
                                List<String> requiredSkills = new ArrayList<>();
                                for (JobOpeningKeyword jobOpeningKeyword : jobOpening.getJobOpeningKeywordList()) {
                                    Keyword keyword = jobOpeningKeyword.getKeyword();
                                    if (keyword != null) {
                                        requiredSkills.add(keyword.getName());
                                    }
                                }
                
                                // JobOpening을 JobOpeningDocument로 변환
                                JobOpeningDocument jobOpeningDocument = JobOpeningDocument.create(jobOpening);
                                jobOpeningDocument.getRequiredSkills().addAll(requiredSkills);
                                jobOpeningDocuments.add(jobOpeningDocument);
                            }
                
                            // Elasticsearch에 데이터 저장
                            jobOpeningDocumentRepository.saveAll(jobOpeningDocuments);
                            log.info("Mysql 데이터가 Elasticsearch에 성공적으로 동기화되었습니다");
                
                        } catch (Exception e) {
                            log.error("데이터 동기화 중 오류 발생", e);
                        }
                    }
                }
                
```
</details>
        
♻️추상화된 인터페이스를 바탕으로 책임을 분리해 볼까요?
        
- Controller에서 호출하는 부분과 스케줄러를 나눕시다!

<details>
<summary> DataSyncService </summary>
                
```java
                @Slf4j
                @Service
                @RequiredArgsConstructor
                public class DataSyncService {
                
                    private final DataSync dataSync;
                
                    /**
                     * API 로 RDB 데이터를 검색 API 에 동기화 합니다.
                     */
                    @Transactional
                    public void syncData() {
                        try {
                            dataSync.sync();
                        } catch (Exception e) {
                            log.error("데이터 동기화 중 오류 발생", e);
                        }
                    }
                }
                
 ```
</details>

<details>
<summary> DataSyncTaskHandler (Scheduler) </summary>
                
   (다중인스턴스 스케줄러의 구현체입니다)
                
```java
                @Slf4j
                @Component
                @RequiredArgsConstructor
                public class DataSyncTaskHandler implements TaskHandler {
                
                    private final DataSync dataSync;
                
                    @Override
                    public String getTaskType() {
                        return "dataSync";
                    }
                
                    /**
                     * 데이터를 동기화하는 작업이 계속해서 실행됩니다.
                     */
                    @Override
                    @Transactional
                    public void handle(Map<String, Object> payload) {
                        try {
                            dataSync.sync();
                        } catch (Exception e) {
                            log.error("데이터 동기화 중 오류 발생", e);
                        }
                    }
                
                    @Override
                    public long getScheduleIntervalMillis() {
                        return 7200000L;    //2시간
                    }
                }
                
```
</details>          
        
---
        
## 4. 리팩토링 결과
        
✅Elasticsearch에서 다른 서비스로 이전한다면, DataSync의 구현체만 수정하면 돼요!
        
✅코드의 가독성이 좋아졌어요!
        
✅단위 테스트를 수행하기 편해졌어요!

</details>

<details>
<summary> RedisTemplate 의존성 리팩토링! </summary>

## 1. 리팩토링 전
        
![스크린샷 2025-03-13 오후 11.54.52.png](https://github.com/llRosell/sparta/blob/main/20.png?raw=true)
        
‘common/redis’ 안에 RedisTemplate에 의존하는 서비스가 너무 많이 존재합니다.

<details>
<summary> RedisRefreshTokenService </summary>
            
```java
            @Component
            @RequiredArgsConstructor
            public class RedisRefreshTokenService {
            
                private final RedisTemplate<String, String> redisTemplate;
                private final JwtSecurityProperties jwtSecurityProperties;
            
                /**
                 * RefreshToken 을 redis 저장소에 삽입하는 메서드
                 * @param userId 현재 사용자의 식별자
                 * @param refreshToken AuthService 에서 만들어진 현재 사용자의 refreshToken
                 */
                public void createRefreshToken(Long userId, String refreshToken) {
                    long expiration = jwtSecurityProperties.token().refreshExpiration();
                    String key = getKey(userId);
                    redisTemplate.opsForValue().set(key, refreshToken, expiration, TimeUnit.MILLISECONDS);
                }
            
                /**
                 * redis 저장소에서 RefreshToken 을 가져오는 메서드
                 * @param userId 현재 사용자의 식별자
                 * @return 토큰이 존재한다면 RefreshToken 반환, 아닐 시 빈 값 반환
                 */
                public String getRefreshToken(Long userId) {
                    String key = getKey(userId);
                    String token = redisTemplate.opsForValue().get(key);
                    if (token == null || token.isBlank()) {
                        throw new UnAuthorizedException(AuthErrorCode.TOKEN_UNAUTHORIZED);
                    }
                    return token;
                }
            
                /**
                 * 사용자의 식별자로 RefreshToken 을 삭제하는 메서드
                 * @param userId 현재 사용자의 식별자
                 */
                public void deleteRefreshToken(Long userId) {
                    String key = getKey(userId);
                    redisTemplate.delete(key);
                }
            
                private String getKey(Long userId) {
                    String prefix = jwtSecurityProperties.token().refreshPrefix();
                    return prefix + userId;
                }
            }
            
```
</details> 
        
---
        
## 2. 현재 코드의 문제점
        
### ⚠️**DIP 위반!**
        
- RedisTemplate을 직접 호출하다 보니 다른 Key-Value 저장소, 또는 **`Redisson`**과 **`Lettuce`**로 이전할 때 코드를 전부 바꿔야 합니다.
- 테스트 코드 작성 시 RedisTemplate Mocking 과정이 상당히 불편합니다.
        
---

## 3. 리팩토링 과정
        
♻️우선, 추상화합시다!

- RedisTemplate 호출을 추상화된 인터페이스의 구현체에 맡겨볼까요?

<details>
<summary> KeyValueRepository </summary>
                
```java
                public interface KeyValueRepository {
                
                    //Key-Value
                    String getValue(String key);
                
                    void setValue(String key, String number, Duration duration);
                    void setValue(String key, String value, long ttl, TimeUnit timeUnit);
                
                    void removeValue(String key);
                }
                
```
</details>

<details>
<summary> RedisKeyValueRepository </summary>
                
```java
                @Repository
                @RequiredArgsConstructor
                public class RedisKeyValueRepository implements KeyValueRepository {
                
                    private final RedisTemplate<String, String> redisTemplate;
                
                    @Override
                    public String getValue(String key) {
                        return redisTemplate.opsForValue().get(key);
                    }
                
                    @Override
                    public void setValue(String key, String value, Duration duration){
                        redisTemplate.opsForValue().set(key, value, duration);
                    }
                
                    @Override
                    public void setValue(String key, String value, long ttl, TimeUnit timeUnit){
                        redisTemplate.opsForValue().set(key, value, ttl);
                    }
                
                    @Override
                    public void removeValue(String key){
                        redisTemplate.delete(key);
                    }
                }

```
</details> 
        
♻️이제 Redis 결합도가 느슨해졌어요!

<details>
<summary> RefreshTokenService </summary>

```java
            @Component
            @RequiredArgsConstructor
            public class RefreshTokenService {
            
                private final KeyValueRepository keyValueRepository;
                private final JwtSecurityProperties jwtSecurityProperties;
            
                /**
                 * RefreshToken 을 저장소에 삽입하는 메서드
                 * @param userId 현재 사용자의 식별자
                 * @param refreshToken AuthService 에서 만들어진 현재 사용자의 refreshToken
                 */
                public void createRefreshToken(Long userId, String refreshToken) {
                    long expiration = jwtSecurityProperties.token().refreshExpiration();
                    String key = getKey(userId);
                    keyValueRepository.setValue(key, refreshToken, expiration, TimeUnit.MILLISECONDS);
                }
            
                /**
                 * 저장소에서 RefreshToken 을 가져오는 메서드
                 * @param userId 현재 사용자의 식별자
                 * @return 토큰이 존재한다면 RefreshToken 반환, 아닐 시 빈 값 반환
                 */
                public String getRefreshToken(Long userId) {
                    String key = getKey(userId);
                    String token = keyValueRepository.getValue(key);
                    if (token == null || token.isBlank()) {
                        throw new UnAuthorizedException(AuthErrorCode.TOKEN_UNAUTHORIZED);
                    }
                    return token;
                }
            
                /**
                 * 사용자의 식별자로 RefreshToken 을 삭제하는 메서드
                 * @param userId 현재 사용자의 식별자
                 */
                public void deleteRefreshToken(Long userId) {
                    String key = getKey(userId);
                    keyValueRepository.removeValue(key);
                }
            
                private String getKey(Long userId) {
                    String prefix = jwtSecurityProperties.token().refreshPrefix();
                    return prefix + userId;
                }
            }
            
```
</details> 
        
♻️Mocking도 훨씬 쉬워졌고요!
        
- 더는 **`@BeforeEach`**로 RedisTemplate 행동을 미리 정의하지 않아도 돼요!
<details>
<summary> 테스트 코드 (전) </summary>
                
```java
                @BeforeEach
                void setUp() {
                    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
                }
                
                @Test
                void incrementDailyLimit_첫번째요청_카운트_1증가_만료시간설정() {
                    when(valueOperations.get(REDIS_KEY)).thenReturn(null);
                
                    assertDoesNotThrow(() -> checkDailyEmailCount.incrementDailyLimit(TEST_EMAIL, OPERATION_KEY));
                
                    verify(valueOperations, times(1)).increment(REDIS_KEY);
                    verify(redisTemplate, times(1)).expire(eq(REDIS_KEY), anyLong(), eq(TimeUnit.SECONDS));
                }
```
</details>

<details>
<summary> 테스트 코드 (후) </summary>
                
```java
                @Test
                void incrementDailyLimit_첫번째요청_카운트_1증가_만료시간설정() {
                    when(keyValueRepository.getValue(REDIS_KEY)).thenReturn(null);
                
                    assertDoesNotThrow(() -> checkDailyEmailCount.incrementDailyLimit(TEST_EMAIL, OPERATION_KEY));
                
                    verify(keyValueRepository, times(1)).incrementValue(REDIS_KEY);
                    verify(keyValueRepository, times(1)).expireValue(eq(REDIS_KEY), anyLong(), eq(TimeUnit.SECONDS));
                }
```
</details>            
        
---
        
 ## 4. 리팩토링 결과
        
✅RedisTemplate에서 **`Redisson`**과 **`Lettuce`**를 사용할 때, Repository의 구현체만 수정하면 돼요!
        
✅Redis에서 다른 Key-Value 저장소로 이전하기 쉬워졌어요!
        
✅단위 테스트를 수행하기 편해졌어요!

</details>

<details>
<summary> 정말 많이 사용하는 Redis, 리팩토링! </summary>

## 1. 리팩토링 전

<details>
<summary> 리팩토링 전 코드 </summary>
            
 ```java
            public interface KeyValueRepository {
            
                //Key-Value
                String getValue(String key);
                Set<String> getKeys(String key);
            
                void setValue(String key, String value);
                void setValue(String key, String number, Duration duration);
                void setValue(String key, String value, long ttl, TimeUnit timeUnit);
            
                void removeValue(String key);
            
                long incrementValue(String key);
            
                void expireValue(String key, long ttl, TimeUnit timeUnit);
            
                Boolean hasKey(String key);
            
                List<String> opsForListRange(String key, long start, long end);
                void opsForListLeftPush(String key, String value);
            
                Set<String> opsForZSet(String key, long start, long end);
                void opsForZSetAdd(String key, String value, long score);
                Long opsForZSetCard(String key);
                Set<String> opsForZSetReverseRange(String key, long start, long end);
                void opsForZSetRemoveRange(String key, long start, long end);
            
            }
            
```
</details> 
        
---
        
## 2. 현재 코드의 문제점
        
### ⚠️**SRP 위반!**
        
- 하나의 Repository에 너무 많은 메서드가 있고, 이들의 공통점은 Key-value 저장소를 사용한다는 점뿐입니다.
- 새로운 팀원이 합류한다면, 어떤 메서드를 어디서 찾아서 사용해야 할지 헷갈립니다.

## 3. 리팩토링 과정

♻️CQRS 아이디어를 차용해 볼까요?
        
- 읽기 작업과 쓰기 작업으로 분리합시다!
<details>
<summary> 읽기(Query) 작업을 담는 KeyValueQueryRepository </summary>
                
 ```java
                public interface KeyValueQueryRepository {
                
                    //읽기 전용
                    String getValue(String key);
                    Set<String> getKeys(String key);
                    Boolean hasKey(String key);
                    List<String> getListRange(String key, long start, long end);
                    Set<String> getZSetRange(String key, long start, long end);
                    Long getZSetCard(String key);
                    Set<String> getZSetReverseRange(String key, long start, long end);
                }
                
```
</details>

<details>
<summary> 쓰기(Command) 작업을 담는 KeyValueCommandRepository </summary>
                
```java
                public interface KeyValueCommandRepository {
                
                    //쓰기 전용
                    void setValue(String key, String value);
                    void setValue(String key, String value, Duration duration);
                    void setValue(String key, String value, long ttl, TimeUnit timeUnit);
                    void removeValue(String key);
                    long incrementValue(String key);
                    void expireValue(String key, long ttl, TimeUnit timeUnit);
                    void pushToListLeft(String key, String value);
                    void addToZSet(String key, String value, long score);
                    void removeFromZSetRange(String key, long start, long end);
                }
                
 ```
</details>         
        
---
        
 ## 4. 리팩토링 결과
        
✅로직을 나누어 협업하기 쉬워졌어요!
        
✅유지 보수하기 편해졌어요!
        
✅패턴이 명확하여 다른 Key-Value 저장소로 이전하기 쉬워졌어요!

</details>
</details>

<details>
<summary> 함수형 프로그래밍 </summary>

<details>
<summary> 크롤러 코드 길이가 이게 맞나요? </summary>

## 1. 리팩토링 전

<details>
<summary> 리팩토링 전 코드 </summary>

```kotlin
            @Service
            class JobKoreaCrawler(
                private val jobOpeningRepository: JobOpeningRepository,
                private val jobOpeningKeywordService: JobOpeningKeywordService,
                private val webDriverFactory: WebDriverFactory
            ) : Crawler {
            
                @Transactional
                override fun crawl(maxPages: Int) {
                    val driver = webDriverFactory.createDriver()
            
                    //기본 페이지 url
                    val baseUrl = "https://www.jobkorea.co.kr/recruit/joblist?menucode=search#anchorGICnt_1"
                    driver.get(baseUrl)
            
                    try {
                        //첫 페이지 로딩 대기 (5초)
                        val wait = WebDriverWait(driver, Duration.ofSeconds(5))
            
                        //"직무" 필터 클릭
                        val jobButton = wait.until(
                            ExpectedConditions.elementToBeClickable(By.cssSelector("p.btn_tit"))
                        )
                        jobButton.click()
                        Thread.sleep(3000)  //UI 대기 (3초)
            
                        //"개발 / 데이터" 필터 클릭
                        val mainJobElement = wait.until(
                            ExpectedConditions.presenceOfElementLocated(By.cssSelector("label[for='duty_step1_10031']"))
                        )
                        wait.until(ExpectedConditions.elementToBeClickable(mainJobElement)).click()
                        Thread.sleep(3000) //UI 대기 (3초)
            
                        //세부 직무에서 요소 체크
                        val jobValues = listOf("1000229", "1000230", "1000231", "1000232") //백엔드, 프론트엔드, 웹, 앱 개발자
                        for (jobValue in jobValues) {
                            val jobElement = wait.until(
                                ExpectedConditions.presenceOfElementLocated(By.cssSelector("label[for='duty_step2_$jobValue']"))
                            )
                            wait.until(ExpectedConditions.elementToBeClickable(jobElement)).click()
                            Thread.sleep(1000) //UI 대기 (3초)
                        }
            
                        //"검색" 버튼 클릭
                        val searchButton = wait.until(
                            ExpectedConditions.elementToBeClickable(By.id("dev-btn-search"))
                        )
                        searchButton.click()
                        Thread.sleep(3000) //UI 대기 (3초)
            
                        //정렬 기준 선택 "최신업데이트순"
                        val orderSelect = wait.until(
                            ExpectedConditions.presenceOfElementLocated(By.id("orderTab"))
                        )
                        val select = Select(orderSelect)
                        select.selectByValue("3") //최신업데이트순 value="3"
                        Thread.sleep(3000) //UI 대기 (3초)
            
                        for (currentPage in 1..maxPages) {
                            val pageUrl = "https://www.jobkorea.co.kr/recruit/joblist?menucode=search#anchorGICnt_$currentPage"
                            driver.get(pageUrl)
                            println("현재 페이지: $currentPage")
            
                            //페이지 로딩 대기
                            WebDriverWait(driver, Duration.ofSeconds(5)).until(
                                ExpectedConditions.presenceOfElementLocated(By.cssSelector("strong a.link.normalLog"))
                            )
            
                            //해당 페이지의 채용공고 크롤링 (기본 정렬 개수인 40개만 가져옴)
                            val jobListings = driver.findElements(By.cssSelector("strong a.link.normalLog")).take(40)
                            if (jobListings.isEmpty()) {
                                println("채용공고를 찾을 수 없으므로 크롤링 종료")
                                break
                            }
            
                            for (job in jobListings) {
                                val title = job.text
                                val link = job.getAttribute("href")
                                println("채용공고: $title ($link)")
            
                                //채용공고가 이미 존재하는지 먼저 확인
                                if (jobOpeningRepository.existsByJobOpeningUrl(link)) {
                                    println("이미 존재하는 채용공고: 건너뜀")
                                    continue
                                }
            
                                //랜덤 대기 (봇 탐지 방어)
                                val randomDelay = Random.nextLong(500, 5000)
                                println("⏳ 잡코리아 랜덤 대기 중: ${randomDelay / 1000}초")
                                Thread.sleep(randomDelay)
            
                                //Jsoup 으로 상세 페이지 크롤링
                                val jobDoc: Document = Jsoup.connect(link).get()
            
                                val company = jobDoc.select("span.coName").text()
            
                                //지역
                                val location = jobDoc.select("dt:contains(지역) + dd a").text()
            
                                //고용형태 (<dd> 태그 안의 모든 <li> 요소를 , 로 분리해 가져옴)
                                val employmentType = jobDoc.select("dt:contains(고용형태) + dd ul.addList li strong").eachText().joinToString(", ")
                                val educationLevel = jobDoc.select("dt:contains(학력) + dd").text()
            
                                //일단 포지션은 개발자로 통일
                                val position = "개발자"
            
                                //급여 처리 (숫자가 없으면 -1)
                                val salaryText = jobDoc.select("dt:contains(급여) + dd").text()
                                val firstNumber = Regex("\\d{1,3}(,\\d{3})*").find(salaryText)?.value
                                    ?.replace(",", "")
                                    ?.toIntOrNull() ?: -1
                                val salary = if (firstNumber != -1) {
                                    if (salaryText.contains("연봉")) firstNumber else firstNumber * 12
                                } else -1
            
                                //경력 (최소 / 최대 구분, 최대 = 최소 + 3)
                                val experienceText = jobDoc.select("dt:contains(경력) + dd span.tahoma").text()
                                val experienceYears = experienceText.replace("[^0-9]".toRegex(), "").toIntOrNull() ?: 0
            
                                //채용 시작 & 마감일 (ZonedDateTime)
                                val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
                                val zoneId = ZoneId.of("Asia/Seoul")
            
                                val hiringStartText = jobDoc.select("dt:contains(시작일) + dd span.tahoma").text()
                                val hiringStartAt = runCatching {
                                    LocalDate.parse(hiringStartText, dateFormatter).atStartOfDay(zoneId)
                                }.getOrNull()
            
                                val hiringEndText = jobDoc.select("dt:contains(마감일) + dd span.tahoma").text()
                                val hiringEndAt = runCatching {
                                    LocalDate.parse(hiringEndText, dateFormatter).atStartOfDay(zoneId)
                                }.getOrNull()
            
                                //채용공고 저장
                                val jobOpening = JobOpening.toEntity(
                                    title,
                                    company,
                                    location,
                                    salary,
                                    employmentType,
                                    educationLevel,
                                    link,
                                    experienceYears + 3,
                                    experienceYears,
                                    position,
                                    hiringStartAt,
                                    hiringEndAt,
                                )
                                jobOpeningRepository.save(jobOpening)
            
                                //스킬 키워드 추출 및 저장
                                val skills = jobDoc.select("dt:contains(스킬) + dd").text().split(",").map { it.trim() }
                                jobOpeningKeywordService.saveKeywordList(skills, jobOpening)
                            }
                        }
            
                    } catch (e: Exception) {
                        println("크롤링 중 오류 발생: ${e.message}")
                    } finally {
                        driver.quit()
                    }
                }
            }
            
```
</details>

---
        
## 2. 현재 코드의 문제점
        
### ⚠️**SRP 위반!**
        
- 필터 클릭, 목록 페이지 크롤링, 상세 페이지 크롤링, 데이터 저장 등등 모두 한 클래스에 있습니다.
- 유지 보수하기 정말 어려운 구조입니다.
        
### ⚠️이거 코틀린 맞아?

- 현재는 잦은 변수 선언, 세로로 읽히는 코드 등 함수형 프로그래밍의 장점을 전혀 살리지 못합니다.
        
---
        
## 3. 리팩토링 과정
        
♻️우선 책임을 분리합시다!
        
- 상세 페이지 크롤링은 Data Class로 처리합시다!
- 버튼을 클릭하는 과정은 Object로 처리해 볼까요?
<details>
<summary> JobKoreaContentData </summary>
                
```java
                data class JobKoreaContentData(
                    val company: String,
                    val location: String,
                    val employmentType: String,
                    val educationLevel: String,
                    val salary: Int,
                    val experienceYears: Int,
                    val hiringStartAt: ZonedDateTime?,
                    val hiringEndAt: ZonedDateTime?,
                    val skills: List<String>
                ) {
                    companion object {
                        private val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
                        private val zoneId = ZoneId.of("Asia/Seoul")
                
                        fun from(jobDoc: Document): JobKoreaContentData {
                            //세부 페이지 크롤링
                            //위의 리팩토링 전과 로직 같음
                            return JobKoreaContentData(
                                company = company,
                                location = location,
                                employmentType = employmentType,
                                educationLevel = educationLevel,
                                salary = salary,
                                experienceYears = experienceYears,
                                hiringStartAt = hiringStartAt,
                                hiringEndAt = hiringEndAt,
                                skills = skills
                            )
                        }
                    }
                }
```
</details>
                
- 이제 조금 짧아졌군요!

<details>
<summary> JobKoreaCrawler </summary>
                
```java
                @Service
                class JobKoreaCrawler(
                    private val jobOpeningRepository: JobOpeningRepository,
                    private val jobOpeningKeywordService: JobOpeningKeywordService,
                    private val webDriverFactory: WebDriverFactory
                ) : Crawler {
                
                    @Transactional
                    override fun crawl(maxPages: Int) {
                        val driver = webDriverFactory.createDriver()
                        val wait = WebDriverWait(driver, Duration.ofSeconds(5))
                        val baseUrl = "https://www.jobkorea.co.kr/recruit/joblist?menucode=search#anchorGICnt_1"
                
                        driver.get(baseUrl)
                        try {
                		        //버튼 클릭
                            JobKoreaFilters.apply(wait)
                
                            (1..maxPages).forEach { currentPage ->
                                val pageUrl = "https://www.jobkorea.co.kr/recruit/joblist?menucode=search#anchorGICnt_$currentPage"
                                driver.get(pageUrl)
                                println("현재 페이지: $currentPage")
                
                                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("strong a.link.normalLog")))
                                val jobListings = driver.findElements(By.cssSelector("strong a.link.normalLog")).take(40)
                                if (jobListings.isEmpty()) {
                                    println("채용공고를 찾을 수 없으므로 크롤링 종료")
                                    return@crawl
                                }
                
                                jobListings.forEach { job ->
                                    val title = job.text
                                    val link = job.getAttribute("href")
                                    println("채용공고: $title ($link)")
                
                                    //채용공고가 이미 존재하는지 먼저 확인
                                    if (jobOpeningRepository.existsByJobOpeningUrl(link)) {
                                        println("이미 존재하는 채용공고: 건너뜀")
                                        return@forEach
                                    }
                
                                    //랜덤 대기 (봇 탐지 방어)
                                    Random.nextLong(500, 5000)
                                        .also { delay ->
                                            println("⏳ 잡코리아 랜덤 대기 중: ${delay / 1000}초")
                                            Thread.sleep(delay)
                                        }
                
                                    //Jsoup 으로 상세 페이지 크롤링
                                    val jobDoc = Jsoup.connect(link).get()
                                    val data = JobKoreaData.from(jobDoc)
                
                                    //채용공고 저장
                                    JobOpening.toEntity(
                                        title,
                                        data.company,
                                        data.location,
                                        data.salary,
                                        data.employmentType,
                                        data.educationLevel,
                                        link,
                                        data.experienceYears + 3,
                                        data.experienceYears,
                                        "개발자",
                                        data.hiringStartAt,
                                        data.hiringEndAt,
                                    ).also { jobOpening ->
                                        jobOpeningRepository.save(jobOpening)
                                        // 스킬 키워드 추출 및 저장
                                        jobDoc.select("dt:contains(스킬) + dd").text()
                                            .split(",")
                                            .map(String::trim)
                                            .let { skills ->
                                                jobOpeningKeywordService.saveKeywordList(skills, jobOpening)
                                            }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            println("크롤링 중 오류 발생: ${e.message}")
                        } finally {
                            driver.quit()
                        }
                    }
                }
```
</details>               
        
♻️함수형 프로그래밍의 장점을 살려봅시다!
        
- 변수 선언을 그만하고 로직을 줄여볼까요?

<details>
<summary> JobKoreaContentData </summary>
                
```java
                data class JobKoreaContentData(
                    val company: String,
                    val location: String,
                    val employmentType: String,
                    val educationLevel: String,
                    val salary: Int,
                    val experienceYears: Int,
                    val hiringStartAt: ZonedDateTime?,
                    val hiringEndAt: ZonedDateTime?,
                    val skills: List<String>
                ) {
                    companion object {
                        private val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
                        private val zoneId = ZoneId.of("Asia/Seoul")
                
                        fun from(jobDoc: Document): JobKoreaContentData = JobKoreaContentData(
                            company = jobDoc.select("span.coName").text(),
                            location = jobDoc.select("dt:contains(지역) + dd a").text(),
                            employmentType = jobDoc.select("dt:contains(고용형태) + dd ul.addList li strong")
                                .eachText().joinToString(", "),
                            educationLevel = jobDoc.select("dt:contains(학력) + dd").text(),
                            salary = jobDoc.select("dt:contains(급여) + dd").text().let { salaryText ->
                                val firstNumber = Regex("\\d{1,3}(,\\d{3})*")
                                    .find(salaryText)?.value
                                    ?.replace(",", "")
                                    ?.toIntOrNull() ?: -1
                                if (firstNumber != -1) if (salaryText.contains("연봉")) firstNumber else firstNumber * 12 else -1
                            },
                            experienceYears = jobDoc.select("dt:contains(경력) + dd span.tahoma").text()
                                .replace("[^0-9]".toRegex(), "").toIntOrNull() ?: 0,
                            hiringStartAt = jobDoc.select("dt:contains(시작일) + dd span.tahoma").text()
                                .takeIf { it.isNotEmpty() }
                                ?.let { runCatching { LocalDate.parse(it, dateFormatter).atStartOfDay(zoneId) }.getOrNull() },
                            hiringEndAt = jobDoc.select("dt:contains(마감일) + dd span.tahoma").text()
                                .takeIf { it.isNotEmpty() }
                                ?.let { runCatching { LocalDate.parse(it, dateFormatter).atStartOfDay(zoneId) }.getOrNull() },
                            skills = jobDoc.select("dt:contains(스킬) + dd").text()
                                .split(",").map(String::trim).filter { it.isNotEmpty() }
                        )
                    }
                }
                
```
                
        
---
        
## 4. 리팩토링 결과

✅코드의 가독성이 좋아졌어요!
        
✅유지 보수하기 편해졌어요!

</details>
</details>

</aside>


<aside>
</aside>

## 🤝 **팀 구성원 소개**
| **구성원** | **직책**  | **장점**                                       | **GitHub**                                | **Blog**                                      | **담당**                                                                                                                                                           |
|---------|---------|----------------------------------------------|-------------------------------------------|-----------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **이채영** | `👑팀장`  | `🎨아이디어 뱅크`<br> `❄️냉철한 시선`<br> `😄즐거움의 달인`   | [roqkfchqh](https://github.com/roqkfchqh) | [블로그](https://roqkfchqh.tistory.com/)         | 📌 JWT 인증-인가<br>📌 유저/이메일 인증 관련 CRD<br>📌 이상 사용자 차단 기능 구현<br>📌 클라우드 아키텍처 구성, CI/CD, 배포<br>📌 다중 인스턴스용 스케줄러 구현<br>📌 채용 공고 데이터 크롤러 구현<br>📌 발표회 총괄               |
| **신지현** | `👑부팀장` | `🔠오탈자 귀신`<br> `🎧인간GPT`<br> `💯MBTI J 100%` | [backswan0](https://github.com/backswan0) | [블로그](https://writingforever162.tistory.com/) | 📌 사용자 키워드 CRD 구현<br>📌 `Gmail`로 이메일 알림 기능 구현<br>📌 이메일 알림에 `SendGrid API` 적용<br>📌 이메일 알림 기능 성능 개선<br>📌 브로슈어(Brochure) 제작 총괄<br>📌 발표회 PPT 제작 및 검수             |
| **김리은** | `🎖️팀원` | `🐞버그헌터`<br> `☯️워라밸 마스터`<br> `👦바른어린이`       | [llRosell](https://github.com/llRosell)   | [블로그](https://carrot0911.tistory.com)         | 📌 `MySQL-Elasticsearch` 동기화 API 구현<br>📌 채용 공고 조회 사용자 편의 및 성능 개선<br>📌 채용 공고 즐겨찾기 CRD 구현<br>📌 채용 공고 즐겨찾기 CRD 성능 개선<br>📌 연령대별 인기 즐겨찾기 조회 성능 개선<br>📌 발표회 PPT 제작 |
| **이승찬** | `🏅팀원`  | `👂경청의 대가`<br> `🏆노력 끝판왕`<br> `✍️글의 조각가`     | [tmdcksdl](https://github.com/tmdcksdl)   | [블로그](https://carrot0911.tistory.com)         | 📌 사용자 최근 검색어 CRD 구현<br>📌 채용 공고 조회 구현<br>📌 채용 공고 조회 사용자 편의 및 성능 개선<br>📌 최근 검색어 조회 성능 개선<br>📌 연령대별 인기 키워드 조회 성능 개선<br>📌 중간 발표회 및 최종 발표회 발표                   |
| **진주양** | `🥇팀원`  | `❄️아이스 브레이커`<br> `🔥레벨업 장인`<br> `📅1일1독`     | [juyangjin](https://github.com/juyangjin) | [블로그](https://velog.io/@wndid2008/posts)      | 📌 Redis를 활용한 조회수 집계 동시성 제어<br>📌 즐겨찾기 및 키워드 검색 구현<br>📌 발표회 PPT 자료 수집                                                                    |
