package com.project.cheerha.domain.notice.scheduler;

import com.project.cheerha.domain.notice.service.EmailSender;
import com.project.cheerha.domain.notice.service.RealTimeSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SenderScheduler {

    private final RealTimeSender realTimeSender;
    private final EmailSender emailSender;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void sendRealTime() {
        log.info("실시간 알림 전송 시작");
        realTimeSender.sendRealTime();
    }

    @Scheduled(cron = "0 0/1 * * * ?")
//    @Scheduled(cron = "0 0 9,18 * * ?")
    public void sendEmails() {
        log.info("이메일 알림 전송 시작");

        emailSender.sendEmails();
    }
}