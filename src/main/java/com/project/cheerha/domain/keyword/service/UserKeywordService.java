package com.project.cheerha.domain.keyword.service;

import com.project.cheerha.common.dto.AuthUser;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserKeywordService {

    private final KeywordRepository keywordRepository;
    private final UserKeywordRepository userKeywordRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateUserKeywordResponseDto createUserKeyword(
        AuthUser authUser,
        CreateUserKeywordRequestDto requestDto
    ) {
        Long userId = authUser.id();

        User foundUser = findUserById(userId);

        List<Long> keywordIdList = requestDto.keywordIdList();

        keywordIdList.forEach(
            keywordId -> {
                Keyword foundKeyword = findKeywordById(keywordId);

                boolean isKeywordAlreadyChosen = userKeywordRepository.existsByUserAndKeyword(
                    foundUser,
                    foundKeyword
                );

                if (!isKeywordAlreadyChosen) {
                    UserKeyword newUserKeyword = UserKeyword.of(
                        foundUser,
                        foundKeyword
                    );

                    userKeywordRepository.save(newUserKeyword);
                }
            }
        );

        return CreateUserKeywordResponseDto.of(keywordIdList);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Keyword findKeywordById(Long keywordId) {
        return keywordRepository.findById(keywordId)
            .orElseThrow(() -> new CustomException(ErrorCode.KEYWORD_NOT_FOUND));
    }
}