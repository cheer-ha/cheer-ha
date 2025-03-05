package com.project.cheerha.domain.notification.sender;

import com.project.cheerha.domain.notification.entity.Notification;
import com.project.cheerha.domain.notification.repository.NotificationRepository;
import com.project.cheerha.domain.notification.service.NotificationService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSender {

    private final NotificationRepository notificationRepository;

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    @Value("${SENDGRID_FROM_EMAIL}")
    private String senderEmail;

    @Async
    public void sendEmails() {
        // 이메일별로 해당하는 Mapping들을 묶는 Map
        Map<String, Set<Notification>> emailToMappings = new HashMap<>();

        // (1) 이메일로 발송되지 않은 Mapping 목록 조회
        // (2) 이메일별로 emailToMappings에 Mapping을 묶음
        notificationRepository.findByIsEmailSentFalse()
            .forEach(mapping -> {
                emailToMappings.computeIfAbsent(
                    mapping.getEmail(),
                    emailAsKey -> new HashSet<>()
                ).add(mapping);
                // 해당 이메일에 맞는 Mapping을 Set<Mapping>에 추가
            });

        // 이메일 주소별로 이메일 발송
        emailToMappings.forEach(this::sendMail);
    }

    public void sendVerificationEmail(String recipientEmail, String code) {
        try {
            Email from = new Email(senderEmail);
            Email to = new Email(recipientEmail);
            String subject = "이메일 인증 코드";
            String content = "<p>인증 코드: <strong>" + code + "</strong></p>";
            Content emailContent = new Content("text/html", content);
            sendSendGridEmail(from, subject, to, emailContent);

            log.info("인증 코드 이메일 전송 완료: {}", recipientEmail);
        } catch (IOException e) {
            log.error("인증 코드 이메일 전송 실패: {}", recipientEmail, e);
        }
    }

    private void sendSendGridEmail(Email from, String subject, Email to, Content emailContent) throws IOException {
        Mail mail = new Mail(from, subject, to, emailContent);

        SendGrid sendGrid = new SendGrid(sendGridApiKey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        sendGrid.api(request);
    }

    // 이메일 발송
    private void sendMail(
        String recipientEmail,
        Set<Notification> notifications
    ) {
        try {
            // 이메일 발송에 필요한 정보 설정
            // from: 보내는 사람 이메일 주소
            // to: 받는 사람 이메일 주소
            Email from = new Email(senderEmail);
            Email to = new Email(recipientEmail);
            String subject = "📢 새로운 맞춤 채용 공고가 도착했어요!";
            StringBuilder content = new StringBuilder();

            // 내용 추가
            content.append("<h1>🚀 새로운 채용 공고가 준비됐어요! 🎉</h1>");
            content.append("<p>맞춤형 채용 공고가 도착했답니다! 💼</p>");
            content.append("<p>아래 링크에서 확인해보세요! ⬇️</p>");
            content.append("<ul>");

            // Mapping에 저장된 채용 공고 URL 목록을 내용에 추가
            for (Notification notification : notifications) {
                content.append("<li>👉 <a href=\"")
                    .append(notification.getJobOpeningUrl())
                    .append("\" target=\"_blank\">")
                    .append("채용 공고 자세히 보기</a></li>");
            }

            content.append("</ul>");
            content.append("<p>행운을 빕니다! 🙌</p>");

            // 내용 설정
            Content emailContent = new Content(
                "text/html", // HTML 형식의 이메일
                content.toString() // 작성된 내용
            );

            // SendGrid Mail 객체 생성
            sendSendGridEmail(from, subject, to, emailContent);

            log.info("이메일 전송 완료: {}", recipientEmail);

            notifications.forEach(notification -> {
                notification.markEmailAsSent(); // 발송 완료 상태로 변경
                notificationRepository.save(notification); // 변경된 상태 저장
            });

        } catch (IOException e) {
            log.error("이메일 전송 실패: {}", recipientEmail, e);
        }
    }
}