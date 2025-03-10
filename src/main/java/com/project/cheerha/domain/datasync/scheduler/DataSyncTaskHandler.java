package com.project.cheerha.domain.datasync.scheduler;

import com.project.cheerha.common.scheduler.core.TaskHandler;
import com.project.cheerha.domain.datasync.service.DataSync;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

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
