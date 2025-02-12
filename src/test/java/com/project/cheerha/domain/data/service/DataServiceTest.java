package com.project.cheerha.domain.data.service;

import com.project.cheerha.domain.data.entity.Data;
import com.project.cheerha.domain.data.repository.DataRepository;
import com.project.cheerha.domain.keyword.entity.DataKeyword;
import com.project.cheerha.domain.keyword.entity.Keyword;
import com.project.cheerha.domain.keyword.repository.DataKeywordRepository;
import com.project.cheerha.domain.keyword.repository.KeywordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class DataServiceTest {

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private DataKeywordRepository dataKeywordRepository;

    @BeforeEach
    @Transactional
    public void setup() {
        // 데이터베이스 초기화
        dataRepository.deleteAll();
        dataKeywordRepository.deleteAll();
        keywordRepository.deleteAll();

        // 더미 데이터를 삽입
        System.out.println("더미 데이터 설정을 시작합니다...");
        addDummyData();
    }

    private void addDummyData() {
        // 반복문을 통해 더미 데이터 생성 및 저장
        for (int i = 1; i <= 5; i++) {
            List<String> requiredSkillList = List.of("java", "spring", "mysql");

            System.out.println("잡 " + i + "의 더미 데이터를 생성합니다...");
            // 더미 데이터를 생성하고, 데이터베이스에 저장
            createAndSaveDataWithKeywords(
                    "Job " + i,
                    "Company " + i,
                    "Location " + i,
                    5000000,
                    "Full-time",
                    "https://www.example.com/jobs/job" + i,
                    LocalDate.parse("2025-03-01"),
                    LocalDate.parse("2025-04-15"),
                    "Bachelor's Degree",
                    2,
                    "SW Developer",
                    requiredSkillList
            );
        }
    }

    private void createAndSaveDataWithKeywords(
            String title, String company, String location, int salary, String jobType, String url,
            LocalDate hiringStartPeriod, LocalDate hiringEndPeriod, String education, int career,
            String position, List<String> requiredSkills
    ) {
        System.out.println("타이틀이 " + title + "인 Data 엔티티를 생성합니다.");

        // 키워드 목록을 생성하고 연결
        List<DataKeyword> dataKeywords = new ArrayList<>();
        for (String skill : requiredSkills) {
            Keyword keyword = getOrCreateKeyword(skill);
            DataKeyword dataKeyword = DataKeyword.toEntity(null, keyword); // Data 연결을 위해 null 전달
            dataKeywords.add(dataKeyword);
            System.out.println("기술 스택으로 DataKeyword 생성: " + skill);
        }

        // Data 객체 생성
        Data data = new Data();
        ReflectionTestUtils.setField(data, "title", title);
        ReflectionTestUtils.setField(data, "company", company);
        ReflectionTestUtils.setField(data, "location", location);
        ReflectionTestUtils.setField(data, "salary", salary);
        ReflectionTestUtils.setField(data, "jobType", jobType);
        ReflectionTestUtils.setField(data, "url", url);
        ReflectionTestUtils.setField(data, "hiringStartPeriod", hiringStartPeriod);
        ReflectionTestUtils.setField(data, "hiringEndPeriod", hiringEndPeriod);
        ReflectionTestUtils.setField(data, "education", education);
        ReflectionTestUtils.setField(data, "position", position);

        // `dataKeywordList` 연결
        ReflectionTestUtils.setField(data, "dataKeywordList", dataKeywords); // 연결된 키워드 리스트 설정

        // Data 저장
        dataRepository.save(data);
        System.out.println("잡 " + title + "의 Data 엔티티를 저장했습니다.");

        // Data와 연결된 DataKeyword 저장 (Data 객체에 연결된 DataKeyword 객체도 저장)
        saveDataKeywordsWithData(data, dataKeywords);
    }

    private void saveDataKeywordsWithData(Data data, List<DataKeyword> dataKeywords) {
        System.out.println("Data 엔티티 ID: " + data.getId() + "와 연결된 DataKeywords를 저장 중...");
        for (DataKeyword dataKeyword : dataKeywords) {
            // 생성된 DataKeyword에 Data를 연결
            ReflectionTestUtils.setField(dataKeyword, "data", data); // 양방향 관계 설정
        }
        dataKeywordRepository.saveAll(dataKeywords);
        System.out.println(dataKeywords.size() + "개의 DataKeyword를 Data 엔티티에 저장했습니다.");
    }

    private Keyword getOrCreateKeyword(String keywordName) {
        Keyword keyword = keywordRepository.findByName(keywordName)
                .orElseGet(() -> keywordRepository.save(Keyword.toEntity(keywordName)));
        System.out.println("키워드 '" + keywordName + "'를 조회하거나 새로 생성했습니다.");
        return keyword;
    }

    @Test
    public void testAddDummyData() {
        System.out.println("더미 데이터 삽입 테스트를 시작합니다...");

        // 데이터베이스에서 Data 엔티티를 조회합니다.
        List<Data> dataList = dataRepository.findAll();
        System.out.println("데이터베이스에서 " + dataList.size() + "개의 Data 엔티티를 조회했습니다.");

        // 데이터가 존재하는지 확인합니다.
        assertThat(dataList).isNotEmpty();
        assertThat(dataList.size()).isGreaterThan(0);

        // 데이터베이스에서 Keyword 엔티티를 조회하여 적어도 1개의 키워드가 생성되었는지 확인합니다.
        List<Keyword> keywords = keywordRepository.findAll();
        System.out.println("데이터베이스에서 " + keywords.size() + "개의 Keyword 엔티티를 조회했습니다.");
        assertThat(keywords).isNotEmpty();
        assertThat(keywords.size()).isGreaterThan(0);

        // DataKeyword가 제대로 연결되어 저장되었는지 확인합니다.
        List<DataKeyword> dataKeywords = dataKeywordRepository.findAll();
        System.out.println("데이터베이스에서 " + dataKeywords.size() + "개의 DataKeyword 엔티티를 조회했습니다.");
        assertThat(dataKeywords).isNotEmpty();
        assertThat(dataKeywords.size()).isGreaterThan(0);

        // 첫 번째 Data에 연결된 키워드들을 확인합니다.
        Data firstData = dataList.get(0);
        List<DataKeyword> firstDataKeywords = dataKeywords.stream()
                .filter(dataKeyword -> dataKeyword.getData().getId().equals(firstData.getId())) // Data의 ID로 비교
                .toList();
        System.out.println("첫 번째 Data 엔티티 (ID: " + firstData.getId() + ")는 " + firstDataKeywords.size() + "개의 DataKeyword가 연결되어 있습니다.");
        assertThat(firstDataKeywords).isNotEmpty();
    }
}
