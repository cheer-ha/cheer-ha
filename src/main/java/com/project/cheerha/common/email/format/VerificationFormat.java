package com.project.cheerha.common.email.format;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VerificationFormat {

    public String[] createVerification (String code) {
        String subject = "이메일 인증 코드";
        String content = "<p>인증 코드: <strong>" + code + "</strong></p>";

        return new String[] {subject, content};
    }
}