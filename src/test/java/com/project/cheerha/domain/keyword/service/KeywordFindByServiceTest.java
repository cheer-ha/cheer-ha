package com.project.cheerha.domain.keyword.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.cheerha.common.exception.data.DataErrorCode;
import com.project.cheerha.common.exception.data.NotFoundException;
import com.project.cheerha.domain.keyword.repository.KeywordRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KeywordFindByServiceTest {

    @Mock
    private KeywordRepository keywordRepository;

    @InjectMocks
    private KeywordFindByService keywordFindByService;

    @DisplayName("실패 - 존재하지 않는 keywordId로 키워드 조회 시 NotFoundException 발생")
    @Test
    void failsToFindKeywordById() {
        // given
        // 존재하지 않는 키워드 ID 설정
        Long notExistingKeywordId = 999L;

        // 존재하지 않는 ID로 조회 시 빈 결과 반환
        when(keywordRepository.findById(notExistingKeywordId))
            .thenReturn(Optional.empty());

        // when & then
        // 존재하지 않는 ID로 조회 시 NotFoundException 발생하는지 검증
        NotFoundException expectedException = assertThrows(
            NotFoundException.class,
            () -> keywordFindByService.findById(notExistingKeywordId)
        );

        // 예외 메시지가 예상한 메시지와 같은지 확인
        String actualMessage = expectedException.getMessage();
        String expectedMessage = DataErrorCode.KEYWORD_NOT_FOUND.getMessage();
        assertThat(actualMessage).isEqualTo(expectedMessage);

        // findById가 정확히 한 번 호출되었는지 검증
        verify(keywordRepository, times(1))
            .findById(notExistingKeywordId);
    }
}
