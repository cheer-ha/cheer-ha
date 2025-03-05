package com.project.cheerha.domain.notification.sender;

import com.project.cheerha.domain.notification.entity.Notification;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationFormat {

    public String[] createEmailNotification(Set<Notification> notificationSet) {
        // 이메일 제목
        String subject = "📢 새로운 맞춤 채용 공고가 도착했어요!";

        // 이메일 본문 생성
        StringBuilder content = new StringBuilder();

        content.append("<h1>🚀 새로운 채용 공고가 준비됐어요! 🎉</h1>");
        content.append("<p>맞춤형 채용 공고가 도착했답니다! 💼</p>");
        content.append("<p>아래 링크에서 확인해보세요! ⬇️</p>");
        content.append("<ul>");

        // 알림(Notification) 목록을 이메일 내용에 추가
        for (Notification notification : notificationSet) {
            content.append("<li>👉 <a href=\"")
                .append(notification.getJobOpeningUrl())
                .append("\" target=\"_blank\">")
                .append("채용 공고 자세히 보기</a></li>");
        }

        content.append("</ul>");
        content.append("<p>행운을 빕니다! 🙌</p>");

        return new String[]{subject, content.toString()};
    }
}