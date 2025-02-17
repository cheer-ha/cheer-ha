package com.project.cheerha.domain.notice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendTestEmail() {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String senderEmail = "cheerha35@gmail.com";
            String recipientEmail = "rnrwprh3434@gmail.com";

            helper.setFrom(senderEmail);
            helper.setTo(recipientEmail);
            helper.setSubject("Hello World");

            String content = "<h1>Hello World!</h1><p>This is a test email.</p>";

            helper.setText(content, true);

            javaMailSender.send(message);
            log.info("이메일 전송 완료: {}", recipientEmail);
        } catch (MessagingException e) {
            log.error("이메일 전송 실패: {}", e.getMessage(), e);
        }
    }
}