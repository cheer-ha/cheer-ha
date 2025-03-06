package com.project.cheerha.common.email.format;

public class VerificationFormat {

    public static String[] createVerification (String code) {
        String subject = "이메일 인증 코드";
        String content = "<p>인증 코드: <strong>" + code + "</strong></p>";

        return new String[] {subject, content};
    }
}