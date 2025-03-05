package com.project.cheerha.common.email.sender;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendGridEmailSender implements EmailSender {

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    @Value("${SENDGRID_FROM_EMAIL}")
    private String senderEmail;

    /**
     * 이메일을 SendGrid로 전송
     *
     * @param recipientEmail 수신자 이메일
     * @param subject        이메일 제목
     * @param content        이메일 본문 (HTML)
     * @throws IOException 이메일 전송 시 발생할 수 있는 예외
     */
    @Override
    public void send(String recipientEmail, String subject, String content) throws IOException {
        Email from = new Email(senderEmail);
        Email to = new Email(recipientEmail);
        Content emailContent = new Content("text/html", content);

        Mail mail = new Mail(from, subject, to, emailContent);
        SendGrid sendGrid = new SendGrid(sendGridApiKey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        sendGrid.api(request);

        log.info("이메일 전송 완료: {}", recipientEmail);
    }
}
