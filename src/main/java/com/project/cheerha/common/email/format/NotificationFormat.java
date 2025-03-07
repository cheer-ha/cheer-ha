package com.project.cheerha.common.email.format;

import com.project.cheerha.domain.notification.entity.Notification;
import java.util.List;

public class NotificationFormat {

    public static String[] createEmailNotification(List<Notification> notificationList) {
        // 이메일 제목
        String subject = "📢 새로운 맞춤 채용 공고가 도착했어요!";

        // 이메일 본문 생성
        StringBuilder content = new StringBuilder();

        content.append("<h1>🚀 새로운 채용 공고가 준비됐어요! 🎉</h1>");
        content.append("<p>맞춤형 채용 공고가 도착했답니다! 💼</p>");
        content.append("<p>아래 링크에서 확인해보세요! ⬇️</p>");
        content.append("<ul>");

        int count = 0;

        // 알림(Notification) 목록을 이메일 내용에 추가 (상위 20개까지만)
        for (Notification notification : notificationList) {
            if (count < 20) {
                content.append("<li>👉 <a href=\"")
                    .append(notification.getJobOpeningUrl())
                    .append("\" target=\"_blank\">")
                    .append("채용 공고 자세히 보기")
                    .append("</a></li>");
            }
            count++;
        }

        content.append("</ul>");
        content.append("<p>행운을 빕니다! 🙌</p>");

        return new String[]{subject, content.toString()};
    }
}