package com.project.cheerha.domain.userkeyword.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.cheerha.domain.keyword.dto.response.KeywordDto;
import com.project.cheerha.domain.keyword.entity.Keyword;
import com.project.cheerha.domain.keyword.service.KeywordFindByService;
import com.project.cheerha.domain.user.entity.User;
import com.project.cheerha.domain.user.service.UserFindByService;
import com.project.cheerha.domain.userkeyword.dto.request.CreateUserKeywordRequestDto;
import com.project.cheerha.domain.userkeyword.dto.response.CreateUserKeywordResponseDto;
import com.project.cheerha.domain.userkeyword.dto.response.ReadUserKeywordResponseDto;
import com.project.cheerha.domain.userkeyword.respository.UserKeywordRepository;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserKeywordServiceTest {

    @Mock
    private UserKeywordRepository userKeywordRepository;
    @Mock
    private KeywordFindByService keywordFindByService;
    @Mock
    private UserFindByService userFindByService;

    @InjectMocks
    private UserKeywordService userKeywordService;

    // 테스트에서 사용할 데이터를 미리 정의
    private final Long keywordOneId = 1L;
    private final Long keywordTwoId = 2L;
    private final String keywordOneName = "Java";
    private final String keywordTwoName = "Kafka";
    private final List<Long> userKeywordIdList = List.of(keywordOneId, keywordTwoId);

    private final User user = User.toEntity(
        "user1@example.com",
        "user1",
        30,
        1,
        "password123"
    );

    // Reflections을 활용해 Keyword 객체에 이름을 설정하는 공통 메서드
    private Keyword createKeywordWithName(Long keywordId, String keywordName) throws NoSuchFieldException, IllegalAccessException {
        Keyword keyword = Keyword.toEntity(keywordId);
        Field field = Keyword.class.getDeclaredField("name");
        field.setAccessible(true);
        field.set(keyword, keywordName);
        return keyword;
    }

    @DisplayName("성공 - 기존에 선택하지 않은 않는 기술 키워드 선택(등록)")
    @Test
    void succeedsToCreateNewUserKeyword() throws NoSuchFieldException, IllegalAccessException {
        // given
        // 키워드 객체 생성 및 설정
        Keyword javaKeyword = createKeywordWithName(keywordOneId, keywordOneName);
        Keyword kafkaKeyword = createKeywordWithName(keywordTwoId, keywordTwoName);

        // 키워드 ID로 조회하면 해당하는 키워드 객체를 반환하도록 Mock 설정
        when(keywordFindByService.findById(keywordOneId)).thenReturn(javaKeyword);
        when(keywordFindByService.findById(keywordTwoId)).thenReturn(kafkaKeyword);

        // 사용자 정보 반환
        when(userFindByService.findById(any())).thenReturn(user);

        // 기존에 사용자가 해당 키워드 두 개를 선택하지 않았다고 가정
        when(userKeywordRepository.existsByUserIdAndKeywordId(user.getId(), keywordOneId)).thenReturn(false);
        when(userKeywordRepository.existsByUserIdAndKeywordId(user.getId(), keywordTwoId)).thenReturn(false);

        // when
        CreateUserKeywordRequestDto requestDto = new CreateUserKeywordRequestDto(List.of(keywordOneId, keywordTwoId));
        CreateUserKeywordResponseDto responseDto = userKeywordService.createUserKeyword(user.getId(), requestDto);

        // then
        // 응답에 사용자가 선택한 키워드가 포함되었는지 검증
        assertTrue(responseDto.keywordList().contains(keywordOneName));
        assertTrue(responseDto.keywordList().contains(keywordTwoName));
        assertEquals(List.of(keywordOneName, keywordTwoName), responseDto.keywordList());
    }

    @DisplayName("성공 - 사용자가 선택한 기술 키워드 목록 조회")
    @Test
    void succeedsToReadAllUserKeywords() throws NoSuchFieldException, IllegalAccessException {
        // given
        // 사용자가 등록한 키워드 ID 목록 반환
        when(userKeywordRepository.findKeywordIdsByUserId(user.getId())).thenReturn(userKeywordIdList);

        // 키워드 객체 생성 및 설정
        Keyword javaKeyword = createKeywordWithName(keywordOneId, keywordOneName);
        Keyword kafkaKeyword = createKeywordWithName(keywordTwoId, keywordTwoName);

        // ID로 조회 시 각 ID에 해당하는 키워드 객체 반환
        when(keywordFindByService.findById(keywordOneId)).thenReturn(javaKeyword);
        when(keywordFindByService.findById(keywordTwoId)).thenReturn(kafkaKeyword);

        // when
        ReadUserKeywordResponseDto responseDto = userKeywordService.readAllUserKeywords(user.getId());

        // then
        List<KeywordDto> keywordDtoList = responseDto.keywordDtoList();
        List<String> expectedKeywordNameList = List.of(keywordOneName, keywordTwoName);

        // 응답된 키워드 목록의 개수가 예상 개수와 일치하는지 검증
        assertThat(expectedKeywordNameList.size()).isEqualTo(keywordDtoList.size());

        // 키워드 목록의 순서가 같은지 검증
        for (int i = 0; i < keywordDtoList.size(); i++) {
            assertEquals(expectedKeywordNameList.get(i), keywordDtoList.get(i).name());
        }
    }

    @DisplayName("성공 - 사용자가 선택한 기술 키워드 목록 삭제")
    @Test
    void succeedsToDeleteUserKeywords() {
        // given
        // 사용자가 삭제하려는 키워드가 존재한다고 가정
        when(userKeywordRepository.existsByUserIdAndId(user.getId(), keywordOneId)).thenReturn(true);
        when(userKeywordRepository.existsByUserIdAndId(user.getId(), keywordTwoId)).thenReturn(true);

        // when
        userKeywordService.deleteUserKeyword(user.getId(), userKeywordIdList);

        // then
        // 각 메서드가 한 번만 호출되었는지 검증
        verify(userKeywordRepository, times(1)).deleteById(keywordOneId);
        verify(userKeywordRepository, times(1)).deleteById(keywordTwoId);
        verify(userKeywordRepository, times(1)).existsByUserIdAndId(user.getId(), keywordOneId);
        verify(userKeywordRepository, times(1)).existsByUserIdAndId(user.getId(), keywordTwoId);
    }
}
