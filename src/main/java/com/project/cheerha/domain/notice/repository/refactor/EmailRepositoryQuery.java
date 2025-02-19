package com.project.cheerha.domain.notice.repository.refactor;

import com.project.cheerha.domain.notice.dto.refactor.UserKeywordDto;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public interface EmailRepositoryQuery {

    List<UserKeywordDto> findAllUserKeywords();

    Map<Long, List<String>> findAllJobOpeningKeywords(ZonedDateTime referenceTime);
}