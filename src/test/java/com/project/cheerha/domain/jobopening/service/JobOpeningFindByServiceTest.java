package com.project.cheerha.domain.jobopening.service;

import com.project.cheerha.common.exception.data.DataErrorCode;
import com.project.cheerha.common.exception.data.NotFoundException;
import com.project.cheerha.domain.jobopening.entity.JobOpening;
import com.project.cheerha.domain.jobopening.repository.JobOpeningRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobOpeningFindByServiceTest {

    @Mock
    private JobOpeningRepository jobOpeningRepository;

    @InjectMocks
    private JobOpeningFindByService jobOpeningFindByService;

    /**
     * 조회 성공 테스트만 있고 실패테스트 없어서 추가해야함, null 여부 등도 추가 가능하면 할 예정
     */
    @Test
    void 채용공고_단건_조회성공() {
        // given
        Long jobId = 1L;
        JobOpening mockJobOpening = mock(JobOpening.class);
        when(mockJobOpening.getTitle()).thenReturn("S전자 신입공채 모집");
        when(jobOpeningRepository.findById(jobId)).thenReturn(Optional.of(mockJobOpening));

        // when
        JobOpening result = jobOpeningFindByService.findById(jobId);

        // then
        assertNotNull(result);
        assertEquals("S전자 신입공채 모집", result.getTitle());
        verify(jobOpeningRepository, times(1)).findById(jobId);
    }

    @Test
    @DisplayName("예외 발생: 존재하지 않는 jobOpeningId를 조회할 경우 NotFoundException 발생")
    void 채용공고_단건_조회_실패() {
        // given
        Long jobOpeningId = 999L;

        when(jobOpeningRepository.findById(jobOpeningId)).thenReturn(Optional.empty());

        // when & then
        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> jobOpeningFindByService.findById(jobOpeningId)
        );

        assertThat(exception.getMessage()).contains(DataErrorCode.JOB_OPENING_NOT_FOUND.getMessage());

        verify(jobOpeningRepository, times(1)).findById(jobOpeningId);
    }
}
