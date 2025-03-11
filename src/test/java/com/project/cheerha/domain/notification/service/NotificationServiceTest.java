package com.project.cheerha.domain.notification.service;

import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.cheerha.domain.notification.dto.NotificationRecipientDto;
import com.project.cheerha.domain.notification.repository.NotificationRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private List<NotificationRecipientDto> notificationRecipientDtoList;
    private Map<Long, List<String>> keywordIdToUrlList;

    @BeforeEach
    void setUp() {
        // 테스트에 사용할 데이터 초기화
        notificationRecipientDtoList = Arrays.asList(
            new NotificationRecipientDto(1L, "user1@example.com"),
            new NotificationRecipientDto(2L, "user1@example.com"),
            new NotificationRecipientDto(2L, "user2@example.com")
        );

        keywordIdToUrlList = new HashMap<>();
        keywordIdToUrlList.put(1L, Arrays.asList("https://job.com/1", "https://job.com/2"));
        keywordIdToUrlList.put(2L, List.of("https://job.com/3"));
    }

    @Test
    void succeedsToCreateNotification() {
        // given
        String emailOne = notificationRecipientDtoList.get(0).email();
        String emailTwo = notificationRecipientDtoList.get(1).email();
        HashSet<String> urlSetOne = new HashSet<>(keywordIdToUrlList.get(1L));
        HashSet<String> urlSetTwo = new HashSet<>(keywordIdToUrlList.get(2L));

        // 이메일 주소와 해당 키워드에 맞는 채용 공고 URL을 매칭
        Map<String, Set<String>> emailToUrlSet = new HashMap<>();
        emailToUrlSet.put(emailOne, urlSetOne);
        emailToUrlSet.put(emailTwo, urlSetTwo);

        // findAllByEmailAndJobOpeningUrlIn() 메서드 호출 시, 빈 리스트를 반환하도록 설정
        when(notificationRepository.findAllByEmailAndJobOpeningUrlIn(anyString(), anyList()))
            .thenReturn(Collections.emptyList());

        // when
        // 실제로 알림 생성 메서드 실행
        notificationService.createNotification(notificationRecipientDtoList, keywordIdToUrlList);

        // then
        // findAllByEmailAndJobOpeningUrlIn 메서드가 2번 호출되었는지 확인
        // 고유한 이메일 주소가 2개이므로 2번 호출되는지 검증
        verify(notificationRepository, times(2))
            .findAllByEmailAndJobOpeningUrlIn(anyString(), anyList());

        // saveAll 메서드가 2번 호출되었는지 확인
        // 각 이메일마다 알림을 저장하는데, 고유한 이메일 주소가 2개이므로 2번 호출되는지 검증
        verify(notificationRepository, times(2))
            .saveAll(anyList());
    }
}