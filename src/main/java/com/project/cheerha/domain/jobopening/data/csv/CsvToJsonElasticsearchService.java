package com.project.cheerha.domain.jobopening.data.csv;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvToJsonElasticsearchService {

    private final ElasticsearchClient client;

    @Value("${csv.file.path}") // application.properties에 정의된 CSV 파일 경로
    private String csvFilePath;

    @Value("${elasticsearch.index.name}") // application.properties에 정의된 Elasticsearch 인덱스 이름
    private String indexName;

    /**
     * 애플리케이션 시작 시 호출되는 메서드입니다.
     * CSV 파일에서 데이터를 로드하여 Elasticsearch에 삽입합니다.
     */
    @PostConstruct
    public void loadCsvData() {
        insertDataFromCsv(csvFilePath);
    }

    /**
     * 주어진 파일 경로의 CSV 데이터를 Elasticsearch에 삽입합니다.
     *
     * @param filePath CSV 파일의 경로
     */
    public void insertDataFromCsv(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            // CSV 파일을 파싱하여 헤더가 포함된 레코드를 가져옵니다.
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(reader);
            ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper 인스턴스 생성

            // BulkRequest를 위한 요청 리스트를 초기화합니다.
            List<BulkOperation> bulkOperations = new ArrayList<>();

            // CSV 레코드를 순회하며 Elasticsearch에 삽입할 준비를 합니다.
            for (CSVRecord record : records) {
                // CSV 각 레코드를 Map 형태로 변환
                Map<String, Object> recordMap = new HashMap<>();
                for (String header : record.toMap().keySet()) {
                    recordMap.put(header, record.get(header)); // 각 헤더와 값을 Map에 추가
                }

                // `_id`를 CSV에서 가져온 `id` 필드를 사용하여 설정
                String documentId = record.get("id");  // CSV에서 `id` 필드를 가져와서 사용

                // `_source.id`로 설정
                recordMap.put("id", documentId);  // `_source.id` 필드에 해당 값을 추가

                // Elasticsearch에 데이터 삽입을 위한 IndexOperation 생성
                IndexOperation<Map<String, Object>> indexOperation = new IndexOperation.Builder<Map<String, Object>>()
                        .index(indexName) // 사용할 Elasticsearch 인덱스 설정
                        .id(documentId)  // 문서의 `_id`로 설정
                        .document(recordMap) // 데이터를 document로 설정
                        .build();

                // BulkOperation에 IndexOperation 추가
                BulkOperation bulkOperation = BulkOperation.of(op -> op.index(indexOperation));

                // 요청 리스트에 추가
                bulkOperations.add(bulkOperation);

                // 배치 크기가 일정 이상이면 Elasticsearch에 전송
                if (bulkOperations.size() >= 500) {
                    executeBulkRequest(bulkOperations); // 배치 요청 전송
                    bulkOperations.clear(); // 새 요청으로 초기화
                }
            }

            // 남아있는 데이터 삽입
            if (!bulkOperations.isEmpty()) {
                executeBulkRequest(bulkOperations); // 마지막 배치 전송
            }

            log.info("CSV 데이터 Elasticsearch에 삽입 완료");

        } catch (IOException e) {
            log.error("CSV 파일 읽기 오류", e);
        }
    }

    /**
     * 주어진 BulkOperation 리스트를 Elasticsearch에 전송하여 데이터를 일괄 삽입합니다.
     *
     * @param bulkOperations Elasticsearch에 전송할 BulkOperation 리스트
     */
    private void executeBulkRequest(List<BulkOperation> bulkOperations) {
        try {
            // BulkRequest 요청 실행
            BulkRequest bulkRequest = new BulkRequest.Builder().operations(bulkOperations).build();
            BulkResponse bulkResponse = client.bulk(bulkRequest); // Elasticsearch에 배치 요청 전송

            // 요청 결과에 따라 로그 출력
            if (bulkResponse.errors()) {
                log.warn("Bulk insert failed with errors"); // 에러가 발생한 경우 경고 로그 출력
            } else {
                log.info("Bulk insert completed successfully"); // 성공적으로 완료된 경우 정보 로그 출력
            }
        } catch (Exception e) {
            log.error("Failed to execute bulk request", e); // 요청 실행 중 예외 발생 시 에러 로그 출력
        }
    }
}
