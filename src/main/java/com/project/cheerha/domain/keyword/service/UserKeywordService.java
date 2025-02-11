package com.project.cheerha.domain.keyword.service;

import com.project.cheerha.common.exception.CustomException;
import com.project.cheerha.common.exception.ErrorCode;
import com.project.cheerha.domain.keyword.dto.request.CreateUserKeywordRequestDto;
import com.project.cheerha.domain.keyword.dto.response.CreateUserKeywordResponseDto;
import com.project.cheerha.domain.keyword.entity.Keyword;
import com.project.cheerha.domain.keyword.entity.UserKeyword;
import com.project.cheerha.domain.keyword.repository.KeywordRepository;
import com.project.cheerha.domain.keyword.repository.UserKeywordRepository;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserKeywordService {

    private final KeywordRepository keywordRepository;
    private final UserKeywordRepository userKeywordRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<CreateUserKeywordResponseDto> createUserKeyword(
        Long userId,
        CreateUserKeywordRequestDto requestDto
    ) {
        // todo 유저 정보 불러오는 로직 완성되면 수정 에정
        userId = 1L;

        log.info("userId: {}", userId);

        User foundUser = findUserById(userId);

        List<Long> keywordIdList = requestDto.keywordIdList();

        return keywordIdList.stream()
            .map(keywordId -> {
                    Keyword foundKeyword = findKeywordById(keywordId);
                    UserKeyword newUserKeyword = UserKeyword.of(foundUser, foundKeyword);
                    userKeywordRepository.save(newUserKeyword);

                    return CreateUserKeywordResponseDto.of(newUserKeyword);
                }
            ).toList();
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow();
    }

    private Keyword findKeywordById(Long keywordId) {
        return keywordRepository.findById(keywordId)
            .orElseThrow();
    }
}