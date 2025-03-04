package com.project.cheerha.domain.elasticsearch.data.mysql;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MysqlToElasticsearchSyncController {

    private final MysqlToElasticsearchSyncService mysqlToElasticsearchService;

    /**
     * 사용자가 원할 때 데이터를 동기화할 수 있는 엔드포인트입니다.
     * 이 엔드포인트는 POST 요청을 받아 MySQL에서 Elasticsearch로 데이터를 동기화하는 작업을 시작합니다.
     */
    @PostMapping("/syncData")
    public ResponseEntity<String> startSync() {
        mysqlToElasticsearchService.syncDataToElasticsearch();
        return ResponseEntity.ok("데이터 동기화가 시작되었습니다.");
    }
}
