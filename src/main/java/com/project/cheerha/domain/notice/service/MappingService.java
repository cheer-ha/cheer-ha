package com.project.cheerha.domain.notice.service;

import com.project.cheerha.domain.notice.dto.UserDto;
import com.project.cheerha.domain.notice.entity.Mapping;
import com.project.cheerha.domain.notice.repository.MappingRepository;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 사용자와 채용 공고 URL을 연결하는 Service
@Service
@RequiredArgsConstructor
public class MappingService {

    private final MappingRepository mappingRepository;

    @Transactional
    public void saveMappings(
        List<UserDto> userDtoList,
        Map<Long, List<String>> keywordIdToUrlList
    ) {
        // key: 사용자 이메일
        // value: 해당 이메일에 연결되는 채용 공고 URL 목록
        Map<String, Set<String>> emailToUrl = new HashMap<>();

        // 사용자 정보를 순회하며 이메일별로 URL 목록 구성
        for (UserDto dto : userDtoList) {
            List<String> matchingUrlList = keywordIdToUrlList
                .getOrDefault(
                    dto.keywordId(),
                    List.of()
                );

            // 기존에 존재하지 않으면 새로운 HashSet 생성 후 추가
            if (!matchingUrlList.isEmpty()) {
                emailToUrl.computeIfAbsent(
                    dto.email(),
                    email -> new HashSet<>()
                ).addAll(matchingUrlList);
            }
        }

        // 이메일별로 연결된 채용 공고 URL 목록 처리
        emailToUrl.forEach((email, urlSet) -> {

            // 기존에 저장된 Mapping 목록 조회
            List<Mapping> foundMappingList = mappingRepository.findAllByEmailAndJobOpeningUrlIn(
                email,
                urlSet.stream().toList()
            );

            // 이미 존재하는 Mapping의 URL 목록을 Set으로 변환
            // 중복을 확인할 때 사용
            Set<String> existingUrlSet = foundMappingList.stream()
                .map(Mapping::getJobOpeningUrl)
                .collect(Collectors.toSet());

            // 중복 확인 후 기존에 없는 Mapping 객체만 생성
            // .filter : 중복 제거
            // .map: Mapping 객체 생성
            List<Mapping> mappingList = urlSet.stream()
                .filter(jobOpeningUrl ->
                    !existingUrlSet.contains(jobOpeningUrl)
                ).map(jobOpeningUrl ->
                    Mapping.toEntity(email, jobOpeningUrl)
                ).toList();

            // 새로운 Mapping 객체를 한꺼번에 데이터베이스에 저장
            mappingRepository.saveAll(mappingList);
        });
    }
}