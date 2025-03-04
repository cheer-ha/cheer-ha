package com.project.cheerha.domain.notice.scheduler;

import com.project.cheerha.domain.notice.sender.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SenderScheduler {

    private final EmailSender emailSender;

    @Scheduled(fixedDelay = 60_000)
    public void sendEmails() {
        log.info("이메일 알림 전송 시작");

        emailSender.sendEmails();
    }
}