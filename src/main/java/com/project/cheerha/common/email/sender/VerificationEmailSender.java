package com.project.cheerha.common.email.sender;

import com.project.cheerha.common.email.format.VerificationFormat;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationEmailSender {

    private final VerificationFormat verificationFormat;
    private final EmailSender sendGridEmailSender;

    public void sendVerificationEmail(String recipientEmail, String code) {
        try {
            String[] emailData = verificationFormat.createVerification(code);
            String subject = emailData[0];
            String content = emailData[1];

            sendGridEmailSender.sendEmailBySendGrid(recipientEmail, subject, content);

        } catch (IOException e) {
            log.error("인증 코드 이메일 전송 실패: {}", recipientEmail, e);
        }
    }
}