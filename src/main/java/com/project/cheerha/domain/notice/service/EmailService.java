package com.project.cheerha.domain.notice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
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

    private static final String SENDER_EMAIL = "cheerha35@gmail.com";

    public void sendMail(String recipientEmail, List<String> jobOpeningUrlList) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(SENDER_EMAIL);
            helper.setTo(recipientEmail);
            helper.setSubject("📢 새로운 맞춤 채용 공고가 도착했어요!");

            StringBuilder content = new StringBuilder();
            content.append("<h1>✨ 안녕하세요! 새로운 채용 공고가 도착했어요! ✨</h1>");
            content.append("<p>혹시 아래 공고를 확인해 볼까요? 🧐</p>");
            content.append("<ul>");

            for (String url : jobOpeningUrlList) {
                content.append("<li>👉 <a href=\"")
                    .append(url)
                    .append("\" target=\"_blank\">")
                    .append("채용 공고 확인하기!</a></li>");
            }

            content.append("</ul>");
            content.append("<p>🎉 좋은 기회를 놓치지 마세요! 화이팅! 💪</p>");

            helper.setText(content.toString(), true);

            javaMailSender.send(message);

            log.info("이메일 전송 완료: {}", recipientEmail);
        } catch (MessagingException e) {
            log.error("이메일 전송 실패: {}", recipientEmail, e);
        }
    }
}