package com.project.cheerha.domain.notice.service;

import com.project.cheerha.domain.notice.entity.Mapping;
import com.project.cheerha.domain.notice.repository.MappingRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushSender {

    private final MappingRepository mappingRepository;

    public void sendPushMessage() {
        List<Mapping> mappingList = mappingRepository.findAll();

        mappingList.forEach(mapping -> {
            log.info("이메일: {}, URL: {}",
                mapping.getEmail(),
                mapping.getJobOpeningUrl()
            );
        });
    }
}