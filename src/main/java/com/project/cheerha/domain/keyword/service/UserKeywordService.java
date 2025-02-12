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
        List<Long> idList = requestDto.keywordIdList();

        List<Keyword> keywordList = findKeywordListByIdList(idList);

        createNewUserKeywordIfNotExist(keywordList, foundUser);

        List<String> keywordNameList = Keyword.extractNameListFromEntityList(keywordList);

        return CreateUserKeywordResponseDto.of(keywordNameList);
    }

    // 등록된 UserKeyword 객체가 없을 시 객체를 생성하고 저장하는 메서드
    private void createNewUserKeywordIfNotExist(
        List<Keyword> keywordList,
        User foundUser
    ) {
        keywordList.forEach(
            foundKeyword -> {
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
    }

    // 키워드 식별자 목록으로 해당 키워드 엔티티를 조회하는 메서드
    private List<Keyword> findKeywordListByIdList(List<Long> keywordIdList) {
        return keywordIdList.stream()
            .map(this::findKeywordById)
            .toList();
    }

    // 키워드 식별자로 키워드를 조회하는 메서드
    private Keyword findKeywordById(Long keywordId) {
        return keywordRepository.findById(keywordId)
            .orElseThrow(() -> new CustomException(ErrorCode.KEYWORD_NOT_FOUND));
    }

    // 사용자 식별자로 사용자를 조회하는 메서드
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}