package com.project.cheerha.domain.notification.sender;

import java.io.IOException;

public interface EmailSender {
    void sendEmailBySendGrid(String recipientEmail, String subject, String content) throws IOException;
}
