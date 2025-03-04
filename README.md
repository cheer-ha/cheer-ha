## **취하여: 맞춤형 채용 공고 조회 & 알림 서비스**

## 🚀 **프로젝트 소개**
### 📌 **한 줄 소개**
'취하여'는 '취업을 위하여'의 줄임말로, 사용자가 원하는 채용 공고를 쉽고 빠르게 검색하고, 다양한 알림을 받아보며 자신에게 꼭 맞는 채용 공고에 지원하도록 돕는 서비스입니다.
### 💡 **기획 배경**
현재 다양한 채용 공고 사이트가 있지만, 사용자는 여러 가지 불편을 겪습니다.
- **일일이 검색해야 하는 번거로움**: 여러 채용 공고 사이트를 방문하여 원하는 채용 공고를 검색하는 데 시간이 오래 걸림
- **맞춤형 검색 기능 부족**: 특정 기술 스택으로 필터링된 공고를 제공하는 서비스가 적어, 원하는 정보를 쉽게 찾기 어려움
- **채용 공고 정보 놓침**: 새로운 채용 공고가 올라와도 실시간으로 확인하기 어려워서 적절한 시기에 지원하지 못할 수 있음

**취하여**는 이러한 불편을 해소하여 사용자들이 원하는 채용 공고를 더욱 쉽고 정확하게 찾을 수 있도록 기획되었습니다.

### ✨ **개선 사항**
본 서비스는 기존 문제를 해결하고 사용자들에게 원하는 채용 공고를 보다 정확하고 빠르게 제공하고자 다음과 같은 기능을 제공합니다.
1. **효율적인 채용 공고 검색 기능**
    - 사용자가 원하는 채용 공고를 검색 및 조회
    - 조회수를 집계하여 인기 있는 채용 공고 TOP 100 엄선
    - 관심 있는 채용 공고를 즐겨찾기에 추가 가능

2. **맞춤형 추천 및 알림**
    - 사용자가 선택한 기술 스택에 맞는 채용 공고를 하루에 1번 이메일 알림 전송

3. **다양한 검색 옵션 제공**
    - 학력이나 직무 등 다양한 기준으로 채용 공고 검색 및 조회
---

## 🔑 **주요 기능**
### 📦 **서비스에서 제공하는 핵심 요소**
1. **기술 키워드**
    - **정의**: 프로그래밍 언어, 프레임워크, 협업 툴 같은 기술 스택
    - **특징**: 백엔드 및 프론트엔드에서 주로 사용되는 스택 위주 
    - **예시**: Java, Python, Spring Boot, Kafka, AWS, Jira

2. **채용 공고**
    - **정의**: 여러 채용 사이트를 크롤링하여 가져온 개발자 채용 공고 목록  
      (*1~3주 차까지는 더미 데이터로 크롤링했다고 가정*)
    - **특징**: 고용 형태, 학력, 직무 등 자격 요건 및 URL 포함
    - **예시**: '토스 백엔드 개발자 모집', '카카오뱅크 서버 개발자 모집'

### ⚙️ **주요 기능 소개** 
<details>
  <summary>🔍 채용 공고 검색 및 조회</summary>

  - 사용자가 원하는 기술 스택을 포함한 채용 공고를 검색하고 상세 내용 조회
</details>

<details>
  <summary>⭐ 즐겨찾기 관리</summary>

  - 관심 있는 채용 공고를 즐겨찾기에 추가하여 언제든지 다시 확인 가능
</details>

<details>
  <summary>📜 검색 기록 조회</summary>
  
  - 사용자가 이전에 검색한 기록을 조회하고, 다시 검색 가능
</details>

<details>
  <summary>📧 관심 기술 키워드 등록 및 이메일 알림</summary>

   - 기술 키워드 총 45개 중 원하는 키워드 선택 가능 
   - 해당 키워드가 포함된 채용 공고 URL 목록을 추출하여 하루에 1번 이메일 알림 전송 
</details>

<details>
  <summary>🔥 인기 채용 공고 조회</summary>
  
  - 조회수를 집계하여 인기 채용 공고 TOP 100 정보 제공 
  - 비관적 락을 사용하여 조회수 동시성 제어 
</details>

---

## 🏆 **Cloud Architecture**
![스크린샷 2025-03-04 오후 12 47 08](https://github.com/user-attachments/assets/47637fc0-e844-49be-9ccb-96264604ed0e)

## 🏆 **CI/CD Pipeline**
![스크린샷 2025-03-04 오후 12 51 18](https://github.com/user-attachments/assets/df36f2d2-1002-4e25-8c3b-f68f69f2e469)

## 📝 **Wireframe**
![스크린샷 2025-03-04 오후 12 49 30](https://github.com/user-attachments/assets/a414c1f1-2d7b-4124-9b70-6b1b7442e48d)

## 💬 **ERD**
```mermaid
erDiagram
    job_opening_search_history {
        BIGINT id PK
        varchar(100) search_input
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
    job_opening_notification_status {
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
        boolean is_deleted
    }
    dev_keyword {
        BIGINT id PK
        varchar(50) name
    }
    user_subscribed_dev_keyword {
        BIGINT id PK
        BIGINT user_id FK
        BIGINT keyword_id FK
    }
    job_opening_bookmark {
        BIGINT id PK
        BIGINT user_id FK
        BIGINT job_opening_id FK
    }
    job_opening_dev_keyword {
        BIGINT id PK
        BIGINT keyword_id FK
        BIGINT job_opening_id FK
    }
    job_opening_view_count {
        BIGINT id PK
        BIGINT job_opening_id FK
        int view_count
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

    user ||--o{ job_opening_search_history : searched_by
    user ||--o{ job_opening_bookmark : bookmarked
    user ||--o{ user_subscribed_dev_keyword : subscribes_to
    job_opening ||--o{ job_opening_bookmark : has_bookmarks
    job_opening ||--o{ job_opening_view_count : has_views
    job_opening ||--o{ job_opening_dev_keyword : has_keywords
    dev_keyword ||--o{ job_opening_dev_keyword : relates_to
    dev_keyword ||--o{ user_subscribed_dev_keyword : subscribes_to

```

## 📑 **API 명세서**
- [API 명세서](https://docs.google.com/spreadsheets/d/1CQm7sV-ETn0w-FFe7nOCRKrPIp3i5TQWhKw9B1IDT9s/edit?gid=0#gid=0)

## 🎬 **보러 가기**
- [중간 발표 PPT](https://www.canva.com/design/DAGgQfVWdzw/1kASu1k27NP0T2rh9yUAew/edit)
- [중간 발표 시연 영상](https://www.youtube.com/watch?v=IpCdp2d5DV0)

---

## 🧰 **기술 스택**
### 배지 목록

1. ##### Back-end
![Java](https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white)
![Spring](https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Spring Boot](https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![Elasticsearch](https://img.shields.io/badge/elasticsearch-47A248?style=for-the-badge&logo=elasticsearch&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white
)

2. ##### AWS
![EC2](https://img.shields.io/badge/amazon_ec2-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white)
![RDS](https://img.shields.io/badge/amazon_rds-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white)
![Application Load Balancer](https://img.shields.io/badge/amazon_loadbalancer-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white)
![Route 53](https://img.shields.io/badge/route_53-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white)
![Auto Scaling Group](https://img.shields.io/badge/Auto_Scaling_Group-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white)
![ElasticCache](https://img.shields.io/badge/Amazon_ElastiCache-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white
)

3. ##### Tools
![JMeter](https://img.shields.io/badge/jmeter-F5A500?style=for-the-badge&logo=apachejmeter&logoColor=white)
![Docker](https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Git](https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white)
![Jira](https://img.shields.io/badge/Jira-blue?style=for-the-badge&logo=jira&logoColor=white)
![Kibana](https://img.shields.io/badge/Kibana-005C3C?style=for-the-badge&logo=kibana&logoColor=white)
![Prometheus](https://img.shields.io/badge/Prometheus-009E73?style=for-the-badge&logo=prometheus&logoColor=white)
![Grafana](https://img.shields.io/badge/Grafana-F46800?style=for-the-badge&logo=grafana&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ_IDEA-000000?style=for-the-badge&logo=intellijidea&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
![SendGrid](https://img.shields.io/badge/SendGrid-00B9F1?style=for-the-badge&logo=sendgrid&logoColor=white)

### 상세 설명
| 분류                           | 사용 기술 및 도구                                                     | Description                                                                                                                                                                                      |
|------------------------------|----------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Language**                 | Java 17                                                        | - Spring Boot 기반 서버 개발                                                                                                                                                                            |
| **IDE**                      | IntelliJ IDEA                                                  | - Java 개발에 최적화된 통합 개발 환경(IDE)                                                                                                                                                                      |
| **Back-end**                 | Spring <br> Spring Boot                                        | - 의존성 주입 및 AOP, 트랜잭션 관리 등 다양한 엔터프라이즈 기능을 제공하는 프레임워크                                                                                                                                                |
| **DB & Optimization**        | MySQL <br> Redis <br> Elasticsearch                            | - AWS RDS에서 제공되는 관계형 데이터베이스 <br> - 캐시 관리 및 분산 락을 통한 동시성 제어 <br> - 인기 검색어 순위 및 빠른 검색 기능 제공                                                                                                           |
| **Security**                 | JWT                                                            | - JSON Web Tokens를 사용한 인증 및 권한 관리                                                                                                                                                                 |
| **Deployment & Distribution** | EC2 <br> Route 53 <br> ALB <br> ASG <br> ElasticCache <br> RDS | - 애플리케이션 서버 운영 <br> - AWS Route 53을 통한 도메인 설정 <br> - 트래픽을 여러 EC2 인스턴스에 분산하여 처리 <br> - EC2 인스턴스의 자동 확장 및 축소를 통해 트래픽 변화에 대응 <br> - Redis와 Memcached를 지원하는 분산 캐시 서비스 제공     |
| **Test**                     | JMeter <br> Postman                                            | - 성능 테스트 및 로드 테스트를 통한 시스템 안정성 검증 <br> - API 테스트 및 개발을 지원하는 도구                                                                                                                                    |
| **Teamwork**                 | Git <br> GitHub <br> Jira                                      | - 버전 관리 시스템 <br> - GitHub을 사용한 협업 및 코드 관리 <br> - 프로젝트 관리 및 이슈 추적 도구                                                                                                                              |
| **Notification Service**     | SendGrid                                                       | - 이메일 발송 서비스 및 API 제공                                                                                                                                                                            |
| **Monitoring & Analytics**   | Kibana <br> Prometheus <br> Grafana                            | - Elasticsearch 데이터를 시각화하여 로그 분석 및 모니터링을 지원 <br> - 시계열 데이터를 수집하고 분석하는 시스템 모니터링 툴 <br> - 실시간 대시보드 제공                                                                                              |
| **Containerization**         | Docker                                                         | - 개발 및 배포 환경을 컨테이너화하여 일관성 있는 개발 환경 제공                                                                                                                                                            |

---
## 🛠️ **기술적 의사결정**
<details>
  <summary>🔩 이메일 알림 기능은 어떤 서버로 구현해야 할까요?</summary>

### 구현한 기능
사용자들이 선택한 기술 요건을 포함한 채용 공고 목록을 하루에 한 번 발송하는 이메일 알림 기능을 구현했습니다.

### 주요 로직
1. `Spring Scheduler`와 `queryDSL`을 사용하여 주기적으로 사용자 정보와 채용 공고 정보를 조회합니다.
2. 기술 요건이 동일한 사용자와 채용 공고를 매칭합니다.
3. 매칭된 정보를 곧바로 이메일로 전송합니다.

### 배경
‘취하여’ 프로젝트는 사용자가 기술 요건을 하나씩 검색해서 채용 공고를 조회해야 하는 불편을 해소하고자 진행되었습니다.
기획 의도에 따라, 사용자가 기술 요건을 선택하면 해당 요건을 포함한 채용 공고 목록을 쉽게 받아볼 수 있도록 해야 했습니다.  
또한, 사용자가 채용 공고 사이트로 바로 들어갈 수 있어야 하는 만큼, URL 클릭 같은 상호 작용을 할 수 있는 이메일 알림 기능이 필요했습니다.

### 요구 사항
1. 가입 절차가 간단해야 했습니다.
2. 실제 개인 업무로 쓰지 않아서 테스트하기 쉬워야 했습니다.
3. 기능이 잘 동작하는지 많이 테스트할 예정이었으므로 무료이어야 했습니다.

### 선택지
선택지로는 `Gmail`, `네이버의 Cloud Outbound Mailer`, 그리고 `SendGrid`가 있었습니다.

### 의사결정/사유
첫 번째 선택지 `Gmail`을 사용하기로 결정했습니다.  
모든 요구사항에 부합했기 때문입니다.  
그 대신 사용 중 문제가 발생한다면 나머지 선택지로 전환할 가능성을 염두에 두었습니다.

### 회고
**① 기술의 장단점**
- `Gmail`은 계정 생성 및 설정이 간편하여 빠르게 이메일 알림 기능을 구현할 수 있었습니다.
- 다만 모니터링과 통계 산출에는 한계가 있었습니다.
- 또 다른 단점은 ‘로그인 횟수를 줄이세요’와 같이 모호한 해결책만 제공되어 문제를 신속하게 해결하기 어려웠습니다.

**② 다시 시도한다면?**
- 모든 선택지로 이메일을 전송한 다음 가장 나은 선택지를 선택하고 싶습니다.
</details>

<details>
  <summary>🔩 이메일 알림을 보내기까지 필요한 로직을 분리해야 할까요?</summary>

### 구현한 기능
30초마다 사용자와 채용 공고 정보를 조회한 다음, 해당 정보를 기반으로 이메일 알림을 하루에 1번 전송하는 스케줄러를 구현했습니다.

### 주요 로직
1. 30초마다 스케줄러가 실행되어 사용자와 채용 공고 정보를 조회합니다.
2. 조회한 채용 공고와 사용자의 기술 요건을 비교하여 일치하는 공고를 매칭합니다.
3. 매칭된 정보는 알림 객체로 데이터베이스에 저장됩니다.
4. 하루에 한 번 데이터베이스에 저장된 객체를 조회하여 이메일로 전송합니다.

### 배경
현재 스케줄러는 정보 조회와 이메일 전송을 한 번에 처리했는데, 이럴 때 두 가지 문제가 발생했습니다.
- 정보 조회는 30초마다 실행되어야 하지만, 이메일 전송은 하루에 한 번이면 충분했습니다.
- 매칭된 정보를 저장하지 않기 때문에, 전송이 실패하면 복구할 방법이 없었습니다.

### 요구 사항
1. 주기가 다른 각 작업이 서로 영향을 주지 않아야 했습니다.
2. 전송에 실패한 알림을 다시 전송할 수 있도록 정보를 저장해야 했습니다.
3. 푸시 알림 등 다른 알림 기능을 짧은 시간 안에 추가할 수 있도록 확장성이 높아야 했습니다.

### 선택지
선택지로는 현재 로직을 유지하는 방안과 로직을 분리하는 방안이 있었습니다. 

### 의사결정/사유
로직을 여러 계층으로 분리하기로 했습니다.
- 데이터가 늘어나면 현재 스케줄러에 부담이 커질 수 있었습니다.
- 코드가 복잡해질 수 있지만, 로직을 분리해야 나중에 다른 알림 기능을 구현할 때 작업 속도를 단축할 수 있었습니다.
- 전송에 실패했을 때 저장된 데이터를 다시 전송하는 로직을 구현하면 복구가 가능했습니다.

### 회고
① 기술의 장단점
- 확장성이 높고 알림 전송에 실패했을 때도 복구가 가능했습니다.
- 다만 관리해야 할 계층과 데이터를 저장해야 한다는 부담 또한 늘어났습니다.

② 다시 시도한다면?
- 알림 기능을 구현할 때부터 로직을 나누고 싶습니다.
- 또한 아래와 같이 데이터베이스의 부담을 줄일 방법을 지금보다 더 일찍 적용하고 싶습니다.
</details>

<details> 
<summary>🔩 사용자 맞춤형 채용 공고 검색 시스템은 어떻게 구현할까요?</summary> 

## 주요 로직
- 사용자 맞춤형 채용 공고 검색 시스템을 구현했습니다. 이 시스템은 Elasticsearch를 활용하여 사용자 관점에서 편리한 검색기능을 제공합니다.
- 오타가 있어도 정확하게 검색할 수 있도록 합니다.
- 지역, 직무, 경력 등을 필터링하여 정확한 검색 결과를 제공합니다.

## 배경
기존 채용공고 조회 시스템에서는 사용자 맞춤형 검색기능이 부족했습니다. 사용자가 원하는 채용공고를 찾는 데 시간이 많이 소요되며, 다양한 필터를 사용한 검색이 어렵다는 피드백을 받았습니다. 이를 해결하기 위해, 사용자 맞춤형 채용공고 검색어 기능을 추가하여, 사용자가 더 빠르게 원하는 채용공고를 찾을 수 있도록 하는 시스템을 구현하게 되었습니다.

## 요구사항
- 사용자가 마음에 드는 채용공고를 즐겨찾기에 추가할 수 있어야 했습니다.
- 조회 수가 높은 인기 채용공고를 조회할 수 있어야 했습니다.
- 검색 시스템은 Elasticsearch의 빠른 검색 성능을 활용하여 빠른 결과를 제공해야 했습니다.

### 선택지
- MySQL 쿼리DSL로 검색하고 조회하면 향후 수직 확장 시 제약이 따랐습니다.
- Elasticsearch를 연동하여 검색하고 조회하면, 빠른 검색 성능과 다양한 검색 기능을 제공할 수 있었습니다.

### 의사결정/사유
최종적으로 Elasticsearch 기반의 맞춤형 추천 시스템을 선택했습니다.
- 빠른 검색 성능을 제공하여, 대규모 데이터에서도 빠른 응답을 받을 수 있었습니다.
- 수평 확장이 가능하여, 향후 트래픽 증가에 유연하게 대응할 수 있었습니다.
- 사용자 맞춤 추천을 위한 필터링 및 검색 기능을 쉽게 구현할 수 있었습니다.

### 회고
① 기술의 장단점:
- 장점: Elasticsearch는 고성능 검색 엔진으로, 대용량 데이터에서도 빠르게 쿼리를 처리할 수 있습니다. 또한, 수평 확장이 가능하여 향후 트래픽 증가에 유연하게 대응할 수 있습니다.
- 단점: 처음 설정과 학습이 필요하며, 데이터베이스의 복잡성을 관리하는 데 주의가 필요합니다.

② 다시 시도한다면?
- Elasticsearch는 매우 강력하지만, 머신러닝 기반 추천 시스템을 나중에 도입하여 추천의 정확도를 높이는 방향으로 시스템을 확장할 수 있습니다. 또한, Elasticsearch 튜닝과 데이터 최적화 작업을 통해 성능을 더 개선할 수 있을 것입니다.

</details> 

<details> 
<summary>🔩 조회수 집계는 어떻게 해야 할까요?</summary> 

### 구현한 기능
채용공고 페이지 조회 및 조회수 집계를 구현하였습니다.

### 주요 로직
채용공고를 조회할 때 집계 테이블에서 비관적 락을 걸어 조회 수를 집계한 뒤에 일정 주기마다 집계된 조회 수를 채용공고 테이블에 업데이트하는 방식을 사용했습니다.

### 배경
동시에 다수의 사용자가 페이지 조회 시 조회 수의 누락이 발생한다는 것을 알게 되었고 이에 대한 해결방안이 필요했습니다.

### 요구사항
1. 사용자가 조회를 누르면 해당 채용공고 페이지로 리다이렉트 되어야 했습니다.
2. 채용공고 페이지를 조회하는 만큼 자동으로 페이지가 업데이트 되어야 했습니다.
3. 동시에 여러 사용자가 접근하더라도 조회 수의 누락이나 조회의 딜레이 발생을 방지해야 했습니다.
4. 채용공고 페이지에 대한 접근이 조회와 무관하게 자유로워야 했습니다. 

### 선택지
- Optimistic Lock을 사용해 버전 관리하기
- Pessimistic Lock을 사용하여 조회수 누락을 없애기
- 비관적 락을 구현하되, 집계 테이블을 분리하기
- 레디스와 분산 락 이용하기

### 의사결정/사유
비관적 락을 구현하되, 집계 테이블을 분리했습니다.
- 낙관적 락을 구현하게 되면 충돌 발생 시 평균적으로 90%의 조회 수가 누락되기 때문에 정합성에 대한 문제를 걱정하지 않을 수 없기에 배제하였고, 비관적 락만 사용하여도 데드락의 가능성이 있었기에 집계 테이블을 분리하여서 데드락의 문제를 해결하였습니다.

### 회고
데드락의 문제를 해결하면서 조회 수의 정합성도 유지한다는 점은 좋지만, 로컬 환경과 달리 추후 배포 환경을 생각한다면 이것보다 더 나은 방식이 있을지도 모른다고 생각했습니다.  
그래서 다음 버전에서는 레디스와 분산 락을 적용해 볼 예정입니다.
</details>

## 🚨 **트러블슈팅**
<details> 
<summary>🧩 이메일 전송 속도를 7초에서 3초로 단축</summary> 

### 요약
이메일 1건 발송에 평균 7초가 걸려 속도 개선 작업을 했습니다.

### 문제 정의
사용자 2,000명에게 이메일 알림을 1개씩 전송하는 데 약 3시간 53분이 걸릴 수 있었습니다.

### 가설
Gmail을 사용하여 이메일 알림을 전송하는데, Gmail의 SMTP 서버가 이메일을 사용자에게 전달하는 데 시간이 오래 걸렸습니다.

### 해결 방안
Gmail의 SMTP 서버 속도를 직접 개선할 수 없었기에 두 가지 방면으로 문제 해결을 시도했습니다.

1. **이메일 보내기 전 작업 리팩토링**
    - 우선 queryDSL의 `transform()` 메서드를 사용하여 채용 공고 정보를 `Map` 형태로 조회했습니다.
    - 이때, Key는 기술 요건 ID, Value는 해당 요건에 매칭되는 채용 공고 URL 목록입니다.
    - 이렇게 원하는 형태로 데이터를 조회하여 추가 변환 없이 사용자가 선택한 기술 요건과 바로 비교할 수 있도록 했습니다.

```java
@Override
public Map<Long, List<String>> findKeywordIdToUrlList(
ZonedDateTime referenceTime
) {
return queryFactory
.from(jobOpeningKeyword)
.join(jobOpeningKeyword.jobOpening, jobOpening)
.where(jobOpening.createdAt.after(referenceTime)) 
.transform(
groupBy(jobOpeningKeyword.keyword.id) 
.as(list(jobOpening.jobOpeningUrl)) 
);
}
```
2. **이메일 발송 비동기 처리**
    - 이메일 발송을 비동기로 처리하여 해당 작업을 병렬로 실행했습니다.
    - `ThreadPoolTaskScheduler`를 10개로 설정하여 한 이메일이 전송되는 동안 다른 이메일 발송 작업이 기다리지 않도록 했습니다.

### 해결 완료
| **회차** | **채용 공고 조회** | **사용자 조회** | **개별 이메일 전송 (1)** | **개별 이메일 전송 (2)** | **개별 이메일 전송 (3)** | **전체 이메일 전송** | **전체 작업 완료** |
| --- | --- | --- | --- | --- | --- | --- | --- |
| **1** | 150 ms | 3 ms | 9 s 58 ms | 4 s 199 ms | 9 s 231 ms | 22 s 490 ms | 22 s 644 ms |
| **2** | - ms | - ms | 8 s 419 ms | 4 s 113 ms | 9 s 17 ms | 21 s 550 ms | 21 s 556 ms |
| **3** | - ms | - ms | 8 s 808 ms | 3 s 415 ms | 9 s 95 ms | 21 s 458 ms | 21 s 470 ms |
| **평균** | **150 ms** | **3 ms** | **8 s 808 ms** | **3 s 909 ms** | **9 s 114 ms** | **21 s 832 ms** | **21 s 890 ms** |

| **회차** | **채용 공고 조회** | **사용자 조회** | **개별 이메일 전송 (1)** | **개별 이메일 전송 (2)** | **개별 이메일 전송 (3)** | **전체 이메일 전송** | **전체 작업 완료** |
| --- | --- | --- | --- | --- | --- | --- | --- |
| **1** | 100 ms | 3 ms | 9 s 829 ms | 9 s 292 ms | 9 s 432 ms | 9 s 830 ms | 9 s 936 ms |
| **2** | - ms | - ms | 8 s 292 ms | 9 s 614 ms | 8 s 732 ms | 9 s 615 ms | 9 s 623 ms |
| **3** | - ms | - ms | 9 s 762 ms | 9 s 406 ms | 8 s 553 ms | 9 s 762 ms | 9 s 771 ms |
| **평균** | **100 ms** | **3 ms** | **9 s 294 ms** | **9 s 437 ms** | **8s 906 ms** | **9 s 736 ms** | **9 s 777 ms** |

| **항목** | **첫 번째 표 평균** | **두 번째 표 평균** | **개선된 부분**  | **배율** |
| --- | --- | --- | --- | --- |
| **채용 공고 조회** | 150 ms | 100 ms | **50 ms 단축** | **1.5배 향상** |
| **사용자 조회** | 3 ms | 3 ms | **-** | **-** |
| **전체 이메일 전송** | 21 s 832 ms | 9 s 736 ms | **12 s 096 ms 단축** | **2.24배 향상** |
| **전체 작업 완료** | 21 s 890 ms | 9 s 777 ms | **12 s 113 ms 단축** | **2.24배 향상** |

문제 해결을 시도한 결과, 이메일 3건을 보냈을 때 **발송 속도가 약 21초에서 9초로 단축**되었습니다. 
이메일 발송 속도를 **평균으로 계산하면, 약 7초에서 3초로 줄어**들었습니다.

### 회고
① 기술의 장단점
- 이메일 발송을 비동기로 처리하여 여러 이메일 발송 작업을 동시에 처리할 수 있었습니다.
- 다만, `Gmail`의 SMTP 서버 속도를 직접 제어할 수 없어, 서버 자체 한계는 여전히 존재합니다.
- 여러 작업을 병렬로 처리하기 때문에 CPU 사용량이 지나치게 증가하는 위험 또한 존재합니다.
② 다시 시도한다면?
- `Gmail`의 SMTP 서버의 제한을 고려하여 다른 이메일 서비스를 적용하고 싶습니다.
- 비동기 작업의 스레드 수를 조정하는 방식 등을 적용하여 과도한 CPU 사용을 방지하고 싶습니다.
- 스레드 수를 적절히 제한하거나, 스레드 풀 크기나 대기 시간을 조절하거나, 작업의 우선순위를 정하는 식으로 과도한 CPU 사용을 막을 수 있을 듯합니다.
- 데이터 조회 로직을 더 다듬어서 속도 개선뿐만 아니라 많은 데이터도 문제없이 조회하고 싶습니다.

</details> 

<details> 
<summary>🧩 Too many login attempts 오류 해결</summary> 

### 요약
`Too many login attempts` 오류가 발생하여 이를 해결하는 작업을 수행했습니다.

### 문제 정의
`Gmail`로 이메일을 대량으로 전송하려고 할 때 `Too many login attempts` 오류가 발생하여 서버 운영에 차질이 생겼습니다.

### 가설
- 이메일을 한 번에 너무 많이 보내면 구글에서 이를 무차별 대입 공격(brute force attack)으로 인식하여 `Too many login attempts` 오류가 발생했다고 가정했습니다.
- 이를 해결하려면 이메일을 보내는 방식을 변경해야 했습니다.

### 해결 방안
해결 방안은 크게 두 가지로 나뉘었습니다.
1. **Gmail 그대로 사용**  
    - 무차별 대입 공격으로 간주되지 않도록, 시간 간격을 두고 이메일을 보내는 방법이 있었습니다. 
    - 팀원마다 새로운 `Gmail` 계정을 생성하여 이메일을 발송하는 방법도 있었습니다. 
2. **SendGrid 사용**  
    - `SendGrid` API를 사용하여 이메일 발송을 대체하는 방법이 있었습니다.
일주일 동안 이메일을 200개 미만 보냈는데도 로그인 횟수 초과 오류가 발생한 점을 고려할 때, 이메일 발송에 특화된 API를 사용하는 방안이 앞으로 서버 운영에 더 유리해 보였습니다. 

<details>
  <summary>SendGridConfig 펼치기</summary>
    
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

<details>
  <summary>SendGrid로 대체된 EmailSender 펼치기</summary>

```java
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSender {

    private final MappingRepository mappingRepository;

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    @Value("${SENDGRID_FROM_EMAIL}")
    private String senderEmail;

    @Async
    public void sendEmails() {
        Map<String, Set<Mapping>> emailToMappings = new HashMap<>();

        mappingRepository.findByIsEmailSentFalse()
            .forEach(mapping -> {
                emailToMappings.computeIfAbsent(mapping.getEmail(), emailAsKey -> new HashSet<>()).add(mapping);
            });

        emailToMappings.forEach(this::sendMail);
    }

    private void sendMail(
        String recipientEmail,
        Set<Mapping> mappings
    ) {
        try {
            Email from = new Email(senderEmail); 
            Email to = new Email(recipientEmail);
            String subject = "📢 새로운 맞춤 채용 공고가 도착했어요!";
            StringBuilder content = new StringBuilder();

            content.append("<h1>🚀 새로운 채용 공고가 준비됐어요! 🎉</h1>");
            content.append("<p>맞춤형 채용 공고가 도착했답니다! 💼</p>");
            content.append("<p>아래 링크에서 확인해보세요! ⬇️</p>");
            content.append("<ul>");

            for (Mapping mapping : mappings) {
                content.append("<li>👉 <a href=\"")
                    .append(mapping.getJobOpeningUrl())
                    .append("\" target=\"_blank\">")
                    .append("채용 공고 자세히 보기</a></li>");
            }

            content.append("</ul>");
            content.append("<p>행운을 빕니다! 🙌</p>");

            Content emailContent = new Content("text/html", content.toString());

            Mail mail = new Mail(from, subject, to, emailContent);
            SendGrid sendGrid = new SendGrid(sendGridApiKey);

            Request request = new Request();
            request.setMethod(Method.POST); 
            request.setEndpoint("mail/send");
            request.setBody(mail.build()); 
            sendGrid.api(request);

            log.info("이메일 전송 완료: {}", recipientEmail);

            mappings.forEach(mapping -> {
                mapping.markEmailAsSent(); 
                mappingRepository.save(mapping);
            });

        } catch (IOException e) {
            log.error("이메일 전송 실패: {}", recipientEmail, e);
        }
    }
}
```
</details>

2번째 방안을 선택하여 `SendGrid` 회원가입 및 `Gmail` 대체를 마쳤습니다.

### 해결 완료 
`SendGrid API` 적용 후에는 `Too many login attempts` 오류 없이 이메일을 보내는 데 성공했습니다.

### 회고 
① 기술의 장단점
- `SendGrid`는 `Too many login attempts` 오류를 해결하는 데 도움이 되었고, 이메일 전송 상태를 통계 데이터로 확인할 수 있어 유용했습니다.
- 다만, `SendGrid`의 무료 계정은 하루에 100개의 이메일 전송 제한이 있다는 점이 아쉬웠습니다.
② 다시 시도한다면?
- `Gmail`과 `SendGrid`를 사용하면서 각 서비스의 장단점을 명확히 파악할 수 있었기 때문에, 다시 시도하더라도 동일한 순서로 작업을 진행할 듯합니다.
- API 적용을 처음부터 선택하지 않았기에 더 많은 시행착오를 겪으며 값진 경험을 쌓을 수 있었기 때문입니다.
</details>

<details> 
<summary>🧩이메일 알림 생성용 데이터 조회 시, 채용 공고 생성일이 UTC로 저장되어 발생한 중복 조회 해결</summary> 

### 요약
채용 공고 중복 조회를 방지하고자 생성일을 `ZonedDateTime`으로 추가했는데, 여전히 중복 조회되는 문제가 발생하여 해결해야 했습니다.

### 문제 정의
조회 시간이 로그를 찍을 때에는 `KST(한국 표준시)`로 출력되지만, 채용 공고 생성일은 `UTC(협정 세계시)`로 저장되었습니다. 

### 가설
- `ZonedDateTime`을 사용하면 `MySQL`에서 서버 시간과 무관하게 자동으로 `UTC`로 저장해서 해당 문제가 발생했다고 가정했습니다. 
- 그다음 공식 문서를 조회하여 가설을 검증했습니다. 
  - MySQL converts TIMESTAMP values from the current time zone to UTC for storage, and back from UTC to the current time zone for retrieval. 
  - 공식 문서에 나왔다시피, MySQL은 `TIMESTAMP` 값을 현재 시간대에서 `UTC`로 변환하여 저장하고, 저장된 값을 다시 `UTC`에서 현재 시간대로 변환하여 조회한다는 점을 알 수 있었습니다.

### 해결 방안
로그와 데이터베이스 시간 일관성 문제를 해결하고자, 조회 시간을 `UTC`로 변경했습니다. 

```java
ZonedDateTime referenceTime = ZonedDateTime.now()
    .minusSeconds(30L)
    .withZoneSameInstant(ZoneId.of("UTC"));
```

### 해결 완료 
조회 시간을 `UTC`로 통일한 결과, 한 번 조회된 채용 공고 목록이 다시 조회되지 않았습니다. 

### 회고 
① 기술의 장단점
- 조회 시간을 `UTC`로 바꾸어 국외 사용자를 고려한 `ZonedDateTime`을 계속 사용할 수 있었습니다.
- 다만 테스트할 때마다 `KST`를 `UTC`로 변환해야 한다는 한계가 있었습니다
② 다시 시도한다면?
- 데이터베이스 `Timezone` 설정을 `KST`로 변경하여 테스트 시 시간을 더 직관적으로 다룰 수 있도록 개선해 보고 싶습니다. 
</details>

<details> 
<summary>🧩 채용 공고 조회 속도 개선</summary> 

### 요약
Elasticsearch 기반 검색 시스템을 통해 사용자의 관심사에 맞는 맞춤형 채용공고 검색 기능을 제공하였고, 전체 채용공고를 빠르게 조회할 수 있도록 했습니다. 기존 MySQL로 조회할 때보다 조회 응답속도를 3.4배 빠르게 개선했습니다.

### 문제 정의
기존의 채용 공고 조회 시스템에서는 사용자의 관심사에 맞는 채용공고를 검색하는 데 한계가 있었습니다. 검색 성능이 부족하고, 사용자 맞춤형 검색기능이 없어서 사용자가 원하는 채용공고를 찾는 데 시간이 많이 소요되었습니다.

### 가설
Elasticsearch를 활용하면, 대규모 데이터에서 빠른 검색이 가능하므로, 사용자 맞춤형 검색 기능을 통해 사용자의 채용공고 검색 경험을 크게 향상시킬 수 있을 것이다.

### 해결 방안
- **문제 해결을 위한 의사결정 과정**
    - **Elasticsearch 도입 결정**: 기존 시스템에서는 성능 한계가 있었고, Elasticsearch는 대규모 데이터를 빠르게 처리할 수 있는 기능을 제공하므로 Elasticsearch를 도입하기로 결정했습니다.
    - **사용자 맞춤 추천 로직 구현**: Elasticsearch의 필터링 기능을 활용해, 지역, 직무, 경력을 기준으로 채용공고를 검색할 수 있었습니다.
- **해결 과정**
    - **Elasticsearch 설치 및 설정**: Elasticsearch를 설치하고, 클러스터 설정 및 데이터 인덱싱을 통해 검색 성능을 최적화했습니다. 이를 통해 빠른 검색이 가능하도록 시스템을 구성했습니다.
    - **사용자 맞춤 추천 기능**: 사용자의 검색 이력 및 필터링된 데이터를 바탕으로 적합한 채용공고를 추천하는 로직을 구현했습니다.

### 해결 완료
- **결과**
    - **검색 성능 향상**: 기존 MySQL 기반 시스템에서는 사용자가 원하는 채용공고를 찾는 데 평균 48ms가 소요되었으나, Elasticsearch 기반 추천 시스템을 도입한 후 평균 14ms로 채용공고를 찾을 수 있게 되었습니다.
    - **사용자 맞춤 검색 제공**: 사용자는 자신에게 맞는 채용공고를 더 빠르고 정확하게 찾을 수 있게 되었고, 사용자 만족도가 크게 향상되었습니다.
- **전후 데이터 비교**
    - 기존 MySQL 시스템에서는 평균 48ms가 소요되었으나, Elasticsearch 기반 시스템에서는 평균 14ms로 성능이 3.4배 향상되었습니다.

### 회고
① 기술의 장단점
- **장점**: Elasticsearch는 빠른 검색 성능을 제공하며, 수평 확장이 가능하여 대규모 트래픽을 처리할 수 있습니다. 또한, 사용자 맞춤형 검색을 쉽게 구현할 수 있어, 맞춤형 서비스 제공에 매우 유리합니다.
- **단점**: Elasticsearch는 초기 설정과 학습이 필요하며, 시스템의 복잡도를 관리하는 데 신경을 써야 합니다. 특히, 인덱스 설계와 쿼리 최적화가 중요합니다.
② 다시 시도한다면?
- Elasticsearch를 최적화하여 더 빠르고 효율적인 검색 엔진을 구축할 수 있을 것입니다. 또한, 머신러닝 기반의 추천 시스템을 후속 단계로 도입하여 추천 정확도를 향상시키는 방법을 고려할 수 있습니다.
- AI 기반의 검색을 사용하여 검색어와 사용자의 관심사를 기반으로 한 지능형 추천 시스템을 구현하는 방향을 시도할 수 있습니다.
</details>

<details> <summary>🧩 페이지 조회 속도 개선 및 데드락 문제 해결</summary> 

### 요약
채용 공고 페이지 조회 시 조회 속도를 개선하고 데드락 문제를 해결했습니다. 

### 문제 정의
채용공고 페이지 조회 시 조회 속도도 느렸습니다.  
동시에 다수가 접근할 시에는 데드락 문제가 발생하여 관리자조차 정보 업데이트가 불가능했습니다.

### 가설
조회와 동시에 업데이트가 진행되어서 느리다고 추측했습니다. 

### 해결 방안
- 채용공고 페이지 테이블과 별도로 집계 테이블을 생성하여 비관적 락을 집계 테이블에 사용했습니다.
- 정기적으로 집계 테이블의 조회 수를 채용공고 테이블의 조회 수에 업데이트 해주는 방식으로 해결했습니다.
- 조회할 때마다 채용공고가 락이 걸리는 문제가 해결되어 관리자가 정보를 갱신하기 쉬웠습니다.
사용자 측에서 조회할 때는 오로지 조회만 되기 때문에 조회 속도가 향상되었습니다. 

### 해결 완료
전후 조회 속도 데이터 비교
- 낙관적 락 : 1335ms
- 비관적 락 : 856ms
- 개선된 비관적 락 : 179ms

</details>

---

## 🤝 **팀원**

| 이름     | 직책            | 블로그                                     | 깃허브                                                   |
|----------|---------------|--------------------------------------------|---------------------------------------------------------|
| 이채영   | 👑 Leader     | [https://roqkfchqh.tistory.com/](https://roqkfchqh.tistory.com/) | [https://github.com/roqkfchqh](https://github.com/roqkfchqh) |
| 신지현   | 👑 Sub-Leader | [https://writingforever162.tistory.com/](https://writingforever162.tistory.com/) | [https://github.com/backswan0](https://github.com/backswan0) |
| 김리은   | 🧑‍💻 Member  | [https://creator0920.tistory.com/](https://creator0920.tistory.com) | [https://github.com/llRosell](https://github.com/llRosell) |
| 이승찬   | 🧑‍💻 Member  | [https://carrot0911.tistory.com](https://carrot0911.tistory.com) | [https://github.com/tmdcksdl](https://github.com/tmdcksdl) |
| 진주양   | 🧑‍💻 Member  | [https://velog.io/@wndid2008/posts](https://velog.io/@wndid2008/posts) | [https://github.com/juyangjin](https://github.com/juyangjin) |

---
