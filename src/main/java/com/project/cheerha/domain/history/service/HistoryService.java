package com.project.cheerha.domain.history.service;

import com.project.cheerha.domain.history.dto.response.ReadHistoryResponseDto;
import com.project.cheerha.domain.history.entity.History;
import com.project.cheerha.domain.history.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;

    public List<ReadHistoryResponseDto> readAllHistories(Long userId) {
        List<History> historyList = historyRepository.findAllByUserIdOrderByCreatedAtDesc(userId);

        return historyList.stream()
            .map(ReadHistoryResponseDto::toDto)
            .toList();
    }

}
