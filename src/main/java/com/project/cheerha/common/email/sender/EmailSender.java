package com.project.cheerha.common.email.sender;

import java.io.IOException;

public interface EmailSender {
    void send(String recipientEmail, String subject, String content) throws IOException;
}
