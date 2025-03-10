package com.project.cheerha.domain.userkeyword.service;

import com.project.cheerha.domain.keyword.service.KeywordFindByService;
import com.project.cheerha.domain.userkeyword.dto.request.CreateUserKeywordRequestDto;
import com.project.cheerha.domain.userkeyword.dto.response.CreateUserKeywordResponseDto;
import com.project.cheerha.domain.keyword.dto.response.KeywordDto;
import com.project.cheerha.domain.userkeyword.dto.response.ReadUserKeywordResponseDto;
import com.project.cheerha.domain.keyword.entity.Keyword;
import com.project.cheerha.domain.userkeyword.entity.UserKeyword;
import com.project.cheerha.domain.userkeyword.respository.UserKeywordRepository;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.service.UserFindByService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserKeywordService {

    private final UserKeywordRepository userKeywordRepository;
    private final UserFindByService userFindByIdService;
    private final KeywordFindByService keywordFindByService;

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
                Keyword keyword = keywordFindByService.findById(keywordId);

                if (!isKeywordAlreadyChosen(userId, keywordId)) {
                    User user = userFindByIdService.findById(userId);

                    UserKeyword newUserKeyword = UserKeyword.toEntity(
                        user,
                        keyword
                    );

                    userKeywordRepository.save(newUserKeyword);
                }
                keywordList.add(keyword);
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
        List<Long> userKeywordIdList
    ) {
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

    @Transactional(readOnly = true)
    public ReadUserKeywordResponseDto readAllUserKeywords(Long userId) {

        List<Long> keywordIdList = userKeywordRepository.findKeywordIdsByUserId(userId);

        List<KeywordDto> keywordDtoList = keywordIdList.stream()
            .map(keywordId -> {
                    Keyword keyword = keywordFindByService.findById(keywordId);

                    return KeywordDto.toKeywordDto(keyword.getId(), keyword.getName());
                }
            ).toList();

        return ReadUserKeywordResponseDto.toDto(keywordDtoList);
    }
}