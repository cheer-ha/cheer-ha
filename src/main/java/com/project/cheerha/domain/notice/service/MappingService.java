package com.project.cheerha.domain.notice.service;

import com.project.cheerha.domain.notice.UserDto;
import com.project.cheerha.domain.notice.entity.Mapping;
import com.project.cheerha.domain.notice.repository.MappingRepository;
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

    private final MappingRepository mappingRepository;

    @Transactional
    public void saveEmailJobOpeningMappings(
        List<UserDto> userDtoList,
        Map<Long, List<String>> jobOpeningKeywordMap
    ) {
        Map<String, Set<String>> emailToUrl = new HashMap<>();

        for (UserDto dto : userDtoList) {
            List<String> matchingUrlList = jobOpeningKeywordMap
                .getOrDefault(
                    dto.keywordId(),
                    List.of()
                );

            if (!matchingUrlList.isEmpty()) {
                emailToUrl.computeIfAbsent(
                    dto.email(),
                    email -> new HashSet<>()
                ).addAll(matchingUrlList);
            }
        }

        emailToUrl.forEach((email, urlSet) -> {
            urlSet.forEach(jobOpeningUrl -> {
                Mapping mapping = Mapping.toEntity(
                    email,
                    jobOpeningUrl
                );

                mappingRepository.save(mapping);
            });
        });
    }
}