package com.project.cheerha.domain.jobopening.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.project.cheerha.domain.jobopening.entity.JobOpening;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class JobOpeningServiceTest {
    @Mock
    private JobOpeningFindByService jobOpeningFindByService;

    @InjectMocks
    private JobOpeningService jobOpeningService;

    @Test
    @DisplayName("페이지 조회 시, 리다이렉트 페이지가 정상일 때")
    void URL_정상_조회 () {
        //given
        Long id = 1L;
        JobOpening mockJobOpening = mock(JobOpening.class);
        when(mockJobOpening.getJobOpeningUrl()).thenReturn("naver.com");
        when(jobOpeningFindByService.findById(id)).thenReturn(mockJobOpening);

        // when
        String result = jobOpeningService.getJobOpeningUrl(id);

        // then
        assertNotNull(result);
        assertEquals("https://naver.com", result);
    }

}
