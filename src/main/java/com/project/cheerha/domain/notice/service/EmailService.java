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

    // JavaMailSender 객체로 이메일을 보낼 수 있음
    private final JavaMailSender javaMailSender;

    // 이메일 발신자 주소 설정
    private static final String SENDER_EMAIL = "cheerha35@gmail.com";

    /**
     * 이메일을 보내는 메서드
     * @param recipientEmail 수신자 이메일 주소
     * @param jobOpeningUrlList 채용 공고 목록
     */
    public void sendMail(String recipientEmail, List<String> jobOpeningUrlList) {
        try {
            // 새로운 이메일 메시지 객체 생성
            MimeMessage message = javaMailSender.createMimeMessage();

            // 메시지에 다양한 정보를 설정할 수 있도록 돕는 객체
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(SENDER_EMAIL); // 발신자 설정
            helper.setTo(recipientEmail); // 수신자 설정
            helper.setSubject("📢 새로운 맞춤 채용 공고가 도착했어요!"); // 이메일 제목 설정

            // 이메일 본문 내용 구성
            StringBuilder content = new StringBuilder();
            content.append("<h1>🚀 새로운 채용 공고가 준비됐어요! 🎉</h1>");
            content.append("<p>맞춤형 채용 공고가 도착했답니다! 💼</p>");
            content.append("<p>아래 링크에서 확인해보세요! ⬇️</p>");
            content.append("<ul>");

            // 채용 공고 URL 목록을 리스트 형식으로 출력
            for (String url : jobOpeningUrlList) {
                content.append("<li>👉 <a href=\"")
                    .append(url)
                    .append("\" target=\"_blank\">")
                    .append("채용 공고 자세히 보기</a></li>");
            }

            content.append("</ul>");
            content.append("<p>행운을 빕니다! 🙌</p>");

            // HTML 형식으로 본문 내용 설정
            helper.setText(content.toString(), true);

            // 이메일 발송
            javaMailSender.send(message);

            // todo 수신자의 이메일 검증 로직 추가 필요
            log.info("이메일 전송 완료: {}", recipientEmail);
        } catch (MessagingException e) {
            log.error("이메일 전송 실패: {}", recipientEmail, e);
        }
    }
}