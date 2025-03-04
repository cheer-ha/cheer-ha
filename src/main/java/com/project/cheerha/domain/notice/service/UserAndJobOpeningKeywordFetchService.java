//package com.project.cheerha.domain.notice.service;
//
//import com.project.cheerha.domain.notice.dto.UserDto;
//import com.project.cheerha.domain.notice.repository.NotificationDataRepositoryQuery;
//import java.time.ZonedDateTime;
//import java.util.List;
//import java.util.Map;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class UserAndJobOpeningKeywordFetchService {
//
//    private final NotificationDataRepositoryQuery repositoryQuery;
//
//    public Map<Long, List<String>> findKeywordIdToUrlList(ZonedDateTime referenceTime) {
//        return repositoryQuery.findAllJobOpeningKeywords(referenceTime);
//    }
//
//    public List<UserDto> findUserDtoList() {
//        return repositoryQuery.findAllUserKeywords();
//    }
//}