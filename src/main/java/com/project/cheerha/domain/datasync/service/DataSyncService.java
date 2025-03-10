package com.project.cheerha.domain.datasync.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
