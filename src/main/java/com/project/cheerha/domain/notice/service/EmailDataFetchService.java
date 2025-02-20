package com.project.cheerha.domain.notice.service;

import com.project.cheerha.domain.notice.UserDto;
import com.project.cheerha.domain.notice.repository.EmailRepositoryQuery;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailDataFetchService {

    private final EmailRepositoryQuery repositoryQuery;

    // 주어진 기준 시간(referenceTime) 이후에 생성된 채용 공고 키워드와 URL 목록을 조회하는 메서드
    public Map<Long, List<String>> findJobOpeningKeywordMap(ZonedDateTime referenceTime) {
        return repositoryQuery.findAllJobOpeningKeywords(referenceTime);
    }

    // 모든 사용자의 이메일과 각 사용자가 선택한 키워드를 조회하는 메서드
    public List<UserDto> findUserKeywordList() {
        return repositoryQuery.findAllUserKeywords();
    }
}
