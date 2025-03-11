package com.project.cheerha.email;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.project.cheerha.common.email.sender.EmailSender;
import com.project.cheerha.common.email.sender.VerificationEmailSender;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VerificationEmailSenderTest {
    private VerificationEmailSender verificationEmailSender;

    @Mock
    private EmailSender emailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        verificationEmailSender = new VerificationEmailSender(emailSender);
    }

    @Test
    @DisplayName("성공 - 이메일 인증 전송")
    void sendVerificationEmail() throws IOException {
        // given
        String recipientEmail = "test@example.com";
        String verificationCode = "123456";

        // when
        // 이메일 인증 전송 메서드 실행
        verificationEmailSender.sendVerificationEmail(recipientEmail, verificationCode);

        // then
        // emailSender의 send() 메서드가 1번 호출되었는지 확인
        // eq(recipientEmail): 첫 번째 파라미터는 recipientEmail이 전달되었는지 검증
        // anyString(): 두 번째 및 세 번째 파라미터는 어떤 문자열이라도 전달될 수 있으므로 anyString() 사용
        verify(emailSender, times(1))
            .send(eq(recipientEmail), anyString(), anyString());
    }
}