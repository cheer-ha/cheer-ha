package com.project.cheerha.domain.jobopening.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.cheerha.domain.jobopening.entity.JobOpening;
import com.project.cheerha.domain.jobopening.repository.JobOpeningRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JobOpeningFindByServiceTest {

    @Mock
    private JobOpening jobOpening;

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
}
