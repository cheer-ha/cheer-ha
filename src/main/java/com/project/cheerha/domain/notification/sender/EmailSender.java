package com.project.cheerha.domain.notification.sender;

import com.project.cheerha.domain.notification.entity.Notification;
import com.project.cheerha.domain.notification.repository.NotificationRepository;
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

    // Notification을 이메일로 비동기 전송
    @Async
    public void sendEmails() {
        // key: 알림 받을 이메일, value: Notification Set
        Map<String, Set<Notification>> emailToNotificationSet = new HashMap<>();

        // (1) 이메일로 전송되지 않은 Notification 조회
        // (2) 이메일 주소별로 해당 Notification Set 그룹화하여 저장
        notificationRepository.findByIsEmailSentFalse().forEach(
            notification -> {
                emailToNotificationSet.computeIfAbsent(
                    notification.getEmail(),
                    emailAsKey -> new HashSet<>()
                ).add(notification);
            });

        // 묶은 알림을 각 이메일로 전송
        emailToNotificationSet.forEach(this::sendMail);
    }

    /**
     * 이메일 인증 코드 전송
     *
     * @param recipientEmail 이메일 수신자
     * @param code           인증 코드
     */
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

    /**
     * SendGrid로 이메일 전송
     *
     * @param from         발신자 이메일
     * @param subject      이메일 제목
     * @param to           수신자 이메일
     * @param emailContent 이메일 내용
     * @throws IOException 이메일 전송 시 발생할 수 있는 예외
     */
    private void sendSendGridEmail(Email from, String subject, Email to, Content emailContent)
        throws IOException {
        Mail mail = new Mail(from, subject, to, emailContent);

        SendGrid sendGrid = new SendGrid(sendGridApiKey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        sendGrid.api(request);
    }

    /**
     * 수신자에게 맞춤형 채용 공고 알림을 이메일로 전송
     *
     * @param recipientEmail  수신자 이메일
     * @param notificationSet 해당 수신자에게 보낼 알림 목록
     */
    private void sendMail(String recipientEmail, Set<Notification> notificationSet) {
        try {
            // 이메일 설정
            Email from = new Email(senderEmail);
            Email to = new Email(recipientEmail);
            String subject = "📢 새로운 맞춤 채용 공고가 도착했어요!";
            StringBuilder content = new StringBuilder();

            content.append("<h1>🚀 새로운 채용 공고가 준비됐어요! 🎉</h1>");
            content.append("<p>맞춤형 채용 공고가 도착했답니다! 💼</p>");
            content.append("<p>아래 링크에서 확인해보세요! ⬇️</p>");
            content.append("<ul>");

            // 알림 목록을 이메일 내용에 추가
            for (Notification notification : notificationSet) {
                content.append("<li>👉 <a href=\"")
                    .append(notification.getJobOpeningUrl())
                    .append("\" target=\"_blank\">")
                    .append("채용 공고 자세히 보기</a></li>");
            }

            content.append("</ul>");
            content.append("<p>행운을 빕니다! 🙌</p>");

            // 이메일 전송
            Content emailContent = new Content("text/html", content.toString());
            sendSendGridEmail(from, subject, to, emailContent);

            log.info("이메일 전송 완료: {}", recipientEmail);

            notificationSet.forEach(notification -> {
                notification.markEmailAsSent();
                notificationRepository.save(notification);
            });

        } catch (IOException e) {
            log.error("이메일 전송 실패: {}", recipientEmail, e);
        }
    }
}