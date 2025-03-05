package com.project.cheerha.domain.viewcount;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.cheerha.domain.jobopening.repository.JobOpeningRepository;
import com.project.cheerha.domain.viewcount.repository.JobOpeningViewCountRepository;
import com.project.cheerha.domain.viewcount.scheduler.JobOpeningViewCountScheduler;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JobOpeningViewCountSchedulerTest {

    @Mock
    private JobOpeningRepository jobOpeningRepository;

    @Mock
    private JobOpeningViewCountRepository jobOpeningViewCountRepository;

    @InjectMocks
    private JobOpeningViewCountScheduler jobOpeningViewCountScheduler;

    @Test
    void 집계테이블_조회수를_채용공고에_동기화 () {
        //given
        Long id = 1L;
        Long viewCount = 10L;

        when(jobOpeningViewCountRepository.findDistinctViewedJobOpeningIdList()).thenReturn(List.of(id));
        when(jobOpeningViewCountRepository.getViewCountByJobOpeningId(id)).thenReturn(viewCount);

        //when
        jobOpeningViewCountScheduler.syncViewCounts();

        //then
        verify(jobOpeningRepository, times(1))
            .updateViewCount(id, viewCount);

        verify(jobOpeningViewCountRepository, times(1))
            .resetViewCountByJobOpeningId(id);
    }

}
