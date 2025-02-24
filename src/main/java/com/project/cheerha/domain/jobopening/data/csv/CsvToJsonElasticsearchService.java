//package com.project.cheerha.domain.jobopening.data.csv;
//
//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import co.elastic.clients.elasticsearch.core.BulkRequest;
//import co.elastic.clients.elasticsearch.core.BulkResponse;
//import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
//import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVRecord;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.File;
//import java.util.*;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class CsvToJsonElasticsearchService {
//
//    private final ElasticsearchClient client;
//
//    @Value("${csv.file.path}") // application.properties에 정의된 CSV 파일 경로
//    private String csvFilePath;
//
//    @Value("${elasticsearch.index.name}") // application.properties에 정의된 Elasticsearch 인덱스 이름
//    private String indexName;
//
//    private Map<Long, List<String>> jobOpeningSkillsMap;
//    private Map<Long, String> keywordMap;
//
//    /**
//     * CSV 파일을 읽고 Elasticsearch에 삽입하는 메서드입니다.
//     * 이 메서드는 서비스가 시작될 때 자동으로 호출되며,
//     * CSV 파일에서 데이터를 읽고 Elasticsearch에 삽입합니다.
//     *
//     * 먼저 `jobOpeningSkillsMap`을 초기화한 후, CSV 파일에서 데이터를 삽입합니다.
//     */
//    @PostConstruct
//    public void loadCsvData() {
//        // 먼저 jobOpeningSkillsMap을 초기화하고, 그 후 CSV 데이터 삽입
//        loadJobOpeningSkills();
//        insertDataFromCsv(csvFilePath);
//    }
//
//    /**
//     * CSV 파일에서 데이터를 읽어 Elasticsearch에 삽입하는 메서드입니다.
//     *
//     * 이 메서드는 CSV 파일을 읽고, 각 레코드를 Elasticsearch에 삽입할 수 있는 형식으로 변환하여
//     * BulkRequest에 추가한 후, 배치로 Elasticsearch에 전송합니다.
//     *
//     * @param filePath CSV 파일 경로
//     */
//    public void insertDataFromCsv(String filePath) {
//        try {
//            File file = new File("src/main/resources/Job_Openings.csv");  // 로컬 경로 설정
//            FileReader reader = new FileReader(file);  // FileReader로 파일을 읽습니다
//            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(reader);
//            ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper 인스턴스 생성
//
//            // BulkRequest를 위한 요청 리스트를 초기화합니다.
//            List<BulkOperation> bulkOperations = new ArrayList<>();
//
//            // CSV 레코드를 순회하며 Elasticsearch에 삽입할 준비를 합니다.
//            for (CSVRecord record : records) {
//                Map<String, Object> recordMap = new HashMap<>();
//
//                // 기술 리스트 초기화
//                List<String> requiredSkills = new ArrayList<>();
//                Long jobOpeningId = Long.valueOf(record.get("id"));
//
//                // job_opening_keywords.csv에서 직무에 해당하는 키워드들을 가져옴
//                List<String> jobKeywords = jobOpeningSkillsMap.get(jobOpeningId);
//                if (jobKeywords != null) {
//                    requiredSkills.addAll(jobKeywords);  // 키워드를 리스트에 추가
//                }
//
//                log.info("Job Opening ID: " + jobOpeningId + ", Required Skills: " + requiredSkills);
//
//                // Add requiredSkills to the recordMap
//                if (requiredSkills.isEmpty()) {
//                    log.warn("No skills found for job opening ID: " + jobOpeningId);
//                } else {
//                    recordMap.put("requiredSkills", requiredSkills);
//                }
//
//                // recordMap에 필요한 데이터 넣기
//                for (String header : record.toMap().keySet()) {
//                    String value = record.get(header);
//
//                    // 필드를 카멜 케이스로 변환 후 Map에 추가
//                    String camelCaseHeader = convertToCamelCase(header);
//                    if ("requiredSkills".equals(camelCaseHeader)) {
//                        recordMap.put("requiredSkills", requiredSkills);
//                    } else {
//                        recordMap.put(camelCaseHeader, value);  // Add other fields as normal
//                    }
//                }
//
//                // `_id`를 CSV에서 가져온 `id` 필드를 사용하여 설정
//                String documentId = record.get("id");  // CSV에서 `id` 필드를 가져와서 사용
//                recordMap.put("id", documentId);  // `_source.id` 필드에 해당 값을 추가
//
//                // Elasticsearch에 데이터 삽입을 위한 IndexOperation 생성
//                IndexOperation<Map<String, Object>> indexOperation = new IndexOperation.Builder<Map<String, Object>>()
//                        .index(indexName)
//                        .id(documentId)
//                        .document(recordMap)
//                        .build();
//
//                BulkOperation bulkOperation = BulkOperation.of(op -> op.index(indexOperation));
//
//                bulkOperations.add(bulkOperation);
//
//                if (bulkOperations.size() >= 500) {
//                    executeBulkRequest(bulkOperations); // 배치 요청 전송
//                    bulkOperations.clear(); // 새 요청으로 초기화
//                }
//            }
//
//            if (!bulkOperations.isEmpty()) {
//                executeBulkRequest(bulkOperations); // 마지막 배치 전송
//            }
//
//            log.info("CSV 데이터 Elasticsearch에 삽입 완료");
//
//        } catch (IOException e) {
//            log.error("CSV 파일 읽기 오류", e);
//        }
//    }
//
//    /**
//     * BulkOperation 리스트를 Elasticsearch에 전송하여 데이터를 일괄 삽입합니다.
//     *
//     * 이 메서드는 준비된 BulkOperation들을 Elasticsearch에 전송하여 데이터를 일괄 처리합니다.
//     * 실패한 경우, 경고 로그를 남깁니다.
//     *
//     * @param bulkOperations Elasticsearch에 전송할 BulkOperation 리스트
//     */
//    private void executeBulkRequest(List<BulkOperation> bulkOperations) {
//        try {
//            BulkRequest bulkRequest = new BulkRequest.Builder().operations(bulkOperations).build();
//            BulkResponse bulkResponse = client.bulk(bulkRequest); // Elasticsearch에 배치 요청 전송
//
//            // 요청 결과에 따라 로그 출력
//            if (bulkResponse.errors()) {
//                log.warn("Bulk insert failed with errors");
//            } else {
//                log.info("Bulk insert completed successfully");
//            }
//        } catch (Exception e) {
//            log.error("Failed to execute bulk request", e); // 요청 실행 중 예외 발생 시 에러 로그 출력
//        }
//    }
//
//    /**
//     * job_opening_keywords.csv와 keywords.csv를 읽고,
//     * 각 직무(job opening)과 그에 해당하는 기술(keyword)을 매핑하여 jobOpeningSkillsMap에 저장합니다.
//     *
//     * 이 메서드는 두 개의 CSV 파일을 읽고, 직무별로 기술 키워드를 매핑하여 `jobOpeningSkillsMap`에 저장합니다.
//     * 이후 각 채용 공고에 필요한 기술 키워드를 Elasticsearch에 전송하는 데 사용됩니다.
//     */
//    @PostConstruct
//    private void loadJobOpeningSkills() {
//        jobOpeningSkillsMap = new HashMap<>();  // Map 초기화
//        keywordMap = loadKeywords();  // 키워드 맵 로드
//
//        try {
//            // 절대 경로로 job_opening_keywords.csv 파일을 읽기
//            try (FileReader reader = new FileReader("src/main/resources/job_opening_keywords.csv")) {
//                Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(reader);
//                for (CSVRecord record : records) {
//                    Long jobOpeningId = Long.valueOf(record.get("job_opening_id"));
//                    Long keywordId = Long.valueOf(record.get("keyword_id"));
//
//                    String keyword = keywordMap.get(keywordId);
//
//                    if (keyword != null) {
//                        jobOpeningSkillsMap.computeIfAbsent(jobOpeningId, k -> new ArrayList<>()).add(keyword);
//                    } else {
//                        log.warn("Keyword ID " + keywordId + " not found in keywords.csv");
//                    }
//                }
//            }
//        } catch (IOException e) {
//            log.error("Error loading job opening skills", e);
//        }
//    }
//
//    /**
//     * keywords.csv에서 기술 데이터를 로드하고,
//     * 이를 키워드 ID와 키워드 이름의 맵으로 반환합니다.
//     *
//     * 이 메서드는 `keywords.csv` 파일을 읽고, 각 키워드 ID와 그에 해당하는 키워드 이름을
//     * `Map<Long, String>` 형식으로 반환합니다.
//     *
//     * @return 키워드 ID와 키워드 이름이 매핑된 맵
//     */
//    private Map<Long, String> loadKeywords() {
//        Map<Long, String> keywordMap = new HashMap<>();
//
//        try (FileReader reader = new FileReader("src/main/resources/keywords.csv")) { // 절대 경로 사용
//            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(reader);
//            for (CSVRecord record : records) {
//                Long keywordId = Long.valueOf(record.get("id"));
//                String keywordName = record.get("name");
//                keywordMap.put(keywordId, keywordName);
//            }
//        } catch (IOException e) {
//            log.error("Error loading keywords", e);
//        }
//
//        return keywordMap;
//    }
//
//    /**
//     * 스네이크 케이스를 카멜 케이스로 변환하는 메서드
//     *
//     * 이 메서드는 스네이크 케이스 형식의 문자열을 카멜 케이스 형식으로 변환합니다.
//     * 예: "job_opening_title" -> "jobOpeningTitle"
//     *
//     * @param snakeCase 스네이크 케이스 형식의 문자열
//     * @return 카멜 케이스 형식으로 변환된 문자열
//     */
//    private String convertToCamelCase(String snakeCase) {
//        StringBuilder camelCase = new StringBuilder();
//        boolean nextUpperCase = false;
//
//        for (char c : snakeCase.toCharArray()) {
//            if (c == '_') {
//                nextUpperCase = true;
//            } else {
//                if (nextUpperCase) {
//                    camelCase.append(Character.toUpperCase(c));
//                    nextUpperCase = false;
//                } else {
//                    camelCase.append(Character.toLowerCase(c));
//                }
//            }
//        }
//
//        return camelCase.toString();
//    }
//}
