package com.project.cheerha.common.email.format;

import com.project.cheerha.domain.notification.entity.Notification;
import java.util.List;

public class NotificationFormat {

    public static String[] createEmailNotification(List<Notification> notificationList) {
        String subject = "📢 새로운 맞춤 채용 공고가 도착했어요!";

        StringBuilder content = new StringBuilder();

        content.append("<h1>🚀 새로운 채용 공고가 준비됐어요! 🎉</h1>");
        content.append("<p>맞춤형 채용 공고가 도착했답니다! 💼</p>");
        content.append("<p>아래 링크에서 확인해보세요! ⬇️</p>");
        content.append("<ul>");

        for (Notification notification : notificationList) {
            content.append("<li>👉 <a href=\"")
                .append(notification.getJobOpeningUrl())
                .append("\" target=\"_blank\">")
                .append("채용 공고 자세히 보기")
                .append("</a></li>");
        }

        content.append("</ul>");
        content.append("<p>행운을 빕니다! 🙌</p>");

        return new String[]{subject, content.toString()};
    }
}