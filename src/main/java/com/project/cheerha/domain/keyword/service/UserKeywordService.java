package com.project.cheerha.domain.keyword.service;

import com.project.cheerha.common.exception.CustomException;
import com.project.cheerha.common.exception.ErrorCode;
import com.project.cheerha.domain.keyword.dto.request.CreateUserKeywordRequestDto;
import com.project.cheerha.domain.keyword.dto.request.DeleteUserKeywordRequestDto;
import com.project.cheerha.domain.keyword.dto.response.CreateUserKeywordResponseDto;
import com.project.cheerha.domain.keyword.entity.Keyword;
import com.project.cheerha.domain.keyword.entity.UserKeyword;
import com.project.cheerha.domain.keyword.repository.KeywordRepository;
import com.project.cheerha.domain.keyword.repository.UserKeywordRepository;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.repository.UserRepository;
import java.util.ArrayList;
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

    // todo 테스트 코드 작성 필요
    @Transactional
    public CreateUserKeywordResponseDto createUserKeyword(
        Long userId,
        CreateUserKeywordRequestDto requestDto
    ) {
        List<Long> idList = requestDto.keywordIdList();

        List<Keyword> keywordList = createNewUserKeywordIfNotExist(userId, idList);

        List<String> keywordNameList = Keyword.extractNameFromEntity(keywordList);

        return CreateUserKeywordResponseDto.toDto(keywordNameList);
    }

    // 등록된 UserKeyword 객체가 없을 시 객체를 생성하고 저장하는 메서드
    private List<Keyword> createNewUserKeywordIfNotExist(
        Long userId, List<Long> keywordIdList
    ) {
        List<Keyword> keywordList = new ArrayList<>();

        keywordIdList.forEach(keywordId -> {
                Keyword foundKeyword = keywordRepository.findById(keywordId)
                    .orElseThrow(() -> new CustomException(ErrorCode.KEYWORD_NOT_FOUND));

                if (!isKeywordAlreadyChosen(userId, keywordId)) {
                    User foundUser = userRepository.findById(userId)
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

                    UserKeyword newUserKeyword = UserKeyword.of(
                        foundUser,
                        foundKeyword
                    );

                    userKeywordRepository.save(newUserKeyword);
                }

                keywordList.add(foundKeyword);
            }
        );

        return keywordList;
    }

    // 키워드가 이미 선택되었는지 확인하는 메서드
    private boolean isKeywordAlreadyChosen(Long userId, Long keywordId) {
        return userKeywordRepository.existsByUserIdAndKeywordId(userId, keywordId);
    }

    @Transactional
    public void deleteUserKeyword(
        Long userId,
        DeleteUserKeywordRequestDto requestDto
    ) {
        List<Long> userKeywordIdList = requestDto.userKeywordIdList();

        userKeywordIdList.forEach(userKeywordId -> {

                boolean isUserKeywordExist = userKeywordRepository.existsByUserIdAndId(
                    userId,
                    userKeywordId
                );

                if (!isUserKeywordExist) {
                    return;
                }

                userKeywordRepository.deleteById(userKeywordId);
            }
        );
    }
}