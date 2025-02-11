package com.project.cheerha.domain.keyword.service;

import com.project.cheerha.domain.keyword.dto.request.CreateUserKeywordRequestDto;
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

    @Transactional
    public List<CreateUserKeywordResponseDto> createUserKeyword(
        List<CreateUserKeywordRequestDto> requestDtoList
    ) {
       // todo dev git pull한 다음 수정 예정
        long userId = 1L;

        User foundUser = userRepository.findById(userId).orElseThrow();

        List<CreateUserKeywordResponseDto> responseDtoList = new ArrayList<>();

        responseDtoList = requestDtoList.stream()
            .map(dto ->
                {
                    Keyword foundKeyword = keywordRepository.findById(dto.keywordId()).orElseThrow();

                    UserKeyword userKeyword = UserKeyword.of(foundUser, foundKeyword);

                    userKeywordRepository.save(userKeyword);

                    return CreateUserKeywordResponseDto.of(userKeyword);

                }
            ).toList();

        return responseDtoList;
    }
}