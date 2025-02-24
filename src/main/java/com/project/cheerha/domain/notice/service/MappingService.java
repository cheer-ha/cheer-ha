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

    @Transactional
    public void saveEmailJobOpeningMappings(
        List<UserDto> userDtoList,
        Map<Long, List<String>> jobOpeningKeywordMap
    ) {
        Map<String, Set<String>> emailUrlMap = new HashMap<>();

        for (UserDto dto : userDtoList) {
            List<String> matchingUrlList = jobOpeningKeywordMap.get(dto.keywordId());

            if (matchingUrlList != null && !matchingUrlList.isEmpty()) {
                emailUrlMap.computeIfAbsent(dto.email(), email -> new HashSet<>()).addAll(matchingUrlList);
            }
        }

        emailUrlMap.forEach((email, urlSet) -> {
            for (String jobOpeningUrl : urlSet) {
                EmailJobOpeningMapping mapping = EmailJobOpeningMapping.toEntity(email, jobOpeningUrl);
                mappingRepository.save(mapping);
            }
        });
    }
}