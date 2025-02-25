package com.project.cheerha.domain.notice.service;

import com.project.cheerha.domain.notice.entity.Mapping;
import com.project.cheerha.domain.notice.repository.MappingRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSender {

    private final MappingRepository mappingRepository;
    private final JavaMailSender javaMailSender;
    private static final String SENDER_EMAIL = "cheerha35@gmail.com";

    @Async
    public void sendEmails() {
        Map<String, Set<Mapping>> emailToMappings = new HashMap<>();

        mappingRepository.findByIsEmailSentFalse()
            .forEach(mapping -> {
                emailToMappings.computeIfAbsent(
                    mapping.getEmail(),
                    emailAsKey -> new HashSet<>()
                ).add(mapping);
            });

        emailToMappings.forEach(this::sendMail);
    }

    private void sendMail(
        String recipientEmail,
        Set<Mapping> mappings
    ) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                message,
                true,
                "UTF-8"
            );

            helper.setFrom(SENDER_EMAIL);
            helper.setTo(recipientEmail);
            helper.setSubject("📢 새로운 맞춤 채용 공고가 도착했어요!");

            StringBuilder content = new StringBuilder();
            content.append("<h1>🚀 새로운 채용 공고가 준비됐어요! 🎉</h1>");
            content.append("<p>맞춤형 채용 공고가 도착했답니다! 💼</p>");
            content.append("<p>아래 링크에서 확인해보세요! ⬇️</p>");
            content.append("<ul>");

            for (Mapping mapping : mappings) {
                content.append("<li>👉 <a href=\"")
                    .append(mapping.getJobOpeningUrl())
                    .append("\" target=\"_blank\">")
                    .append("채용 공고 자세히 보기</a></li>");
            }

            content.append("</ul>");
            content.append("<p>행운을 빕니다! 🙌</p>");

            helper.setText(content.toString(), true);

            javaMailSender.send(message);

            log.info("이메일 전송 완료: {}", recipientEmail);

            mappings.forEach(mapping -> {
                mapping.markEmailAsSent();
                mappingRepository.save(mapping);
            });

        } catch (MessagingException e) {
            log.error("이메일 전송 실패: {}", recipientEmail, e);
        }
    }
}