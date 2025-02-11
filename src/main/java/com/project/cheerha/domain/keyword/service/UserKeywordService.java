package com.project.cheerha.domain.keyword.service;

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
        Long userId,
        CreateUserKeywordRequestDto requestDto
    ) {

        User foundUser = findUserById(userId);

        // 요청 DTO에서 키워드 ID 목록을 가져옴
        List<Long> keywordIdList = requestDto.keywordIdList();


        keywordIdList.forEach(
            keywordId -> {
                // 키워드 ID로 키워드 엔티티 조회
                Keyword foundKeyword = findKeywordById(keywordId);

                // 사용자가 선택한 키워드와 사용자 간의 관계를 나타내는 UserKeyword 엔티티 생성
                UserKeyword newUserKeyword = UserKeyword.of(foundUser, foundKeyword);

                // 생성된 UserKeyword 엔티티를 데이터베이스에 저장
                userKeywordRepository.save(newUserKeyword);
            }
        );

        return CreateUserKeywordResponseDto.of(keywordIdList);
    }

    // todo git pull 이후 예외 처리 메시지 추가 예정
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow();
    }

    // todo git pull 이후 예외 처리 메시지 추가 예정
    private Keyword findKeywordById(Long keywordId) {
        return keywordRepository.findById(keywordId)
            .orElseThrow();
    }
}