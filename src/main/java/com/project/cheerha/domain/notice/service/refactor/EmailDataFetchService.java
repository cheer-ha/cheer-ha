package com.project.cheerha.domain.notice.service.refactor;

import com.project.cheerha.domain.notice.dto.refactor.UserKeywordDto;
import com.project.cheerha.domain.notice.repository.refactor.EmailRepositoryQuery;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailDataFetchService {

    private final EmailRepositoryQuery repositoryQuery;

    public Map<Long, List<String>> findJobOpeningKeywordMap(ZonedDateTime referenceTime) {
        return repositoryQuery.findAllJobOpeningKeywords(referenceTime);
    }

    public List<UserKeywordDto> findUserKeywordList() {
        return repositoryQuery.findAllUserKeywords();
    }
}
