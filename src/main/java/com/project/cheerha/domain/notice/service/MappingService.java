package com.project.cheerha.domain.notice.service;

import com.project.cheerha.domain.notice.UserDto;
import com.project.cheerha.domain.notice.entity.EmailJobOpeningMapping;
import com.project.cheerha.domain.notice.repository.EmailJobOpeningMappingRepository;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MappingService {

    private final EmailJobOpeningMappingRepository mappingRepository;

    /**
     * 이메일과 채용 공고 URL을 매핑하여 저장
     *
     * @param userDtoList 사용자 정보 리스트
     * @param jobOpeningKeywordMap 채용 공고 URL 목록을 키워드 ID로 매핑한 맵
     */
    @Transactional
    public void saveEmailJobOpeningMappings(
        List<UserDto> userDtoList,
        Map<Long, List<String>> jobOpeningKeywordMap
    ) {
        // 이메일과 채용 공고 URL을 매핑할 맵
        // key: 사용자 이메일
        // value: 채용 공고 URL Set
        Map<String, Set<String>> emailUrlMap = new HashMap<>();

        // (1) 사용자 정보 리스트 순회
        // (2) 사용자 이메일과 매칭된 채용 공고 URL 수집
        for (UserDto dto : userDtoList) {
            List<String> matchingUrlList = jobOpeningKeywordMap.get(dto.keywordId());

            // 매칭된 URL이 존재하면 해당 이메일에 URL 추가
            if (matchingUrlList != null && !matchingUrlList.isEmpty()) {
                emailUrlMap.computeIfAbsent(
                    dto.email(), // 이메일을 key로 사용
                    email -> new HashSet<>()
                ).addAll(matchingUrlList); // URL 목록을 Set에 추가
            }
        }

        // 이메일-채용 공고 URL 매핑 저장
        emailUrlMap.forEach((email, urlSet) -> {
            for (String jobOpeningUrl : urlSet) {
                // EmailJobOpeningMapping 객체 생성
                EmailJobOpeningMapping mapping = EmailJobOpeningMapping.toEntity(email, jobOpeningUrl);
                // 데이터베이스에 저장
                mappingRepository.save(mapping);
            }
        });
    }
}