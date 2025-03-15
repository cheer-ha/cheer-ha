package com.project.cheerha.domain.notification.repository;

import static com.project.cheerha.domain.jobopening.entity.QJobOpening.jobOpening;
import static com.project.cheerha.domain.keyword.entity.QJobOpeningKeyword.jobOpeningKeyword;
import static com.project.cheerha.domain.user.entity.QUser.user;
import static com.project.cheerha.domain.userkeyword.entity.QUserKeyword.userKeyword;

import com.project.cheerha.domain.notification.dto.NotificationDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryQueryImpl implements NotificationRepositoryQuery{

    private final JPAQueryFactory queryFactory;

    /**
     * referenceTime 이후에 등록된 채용 공고와 사용자 조회
     *
     * @param referenceTime : 조회 시간
     * @return 알림을 보낼 사용자와 채용 공고 정보 목록
     */
    public List<NotificationDto> findTopMatchingJobOpeningsWithUsers(ZonedDateTime referenceTime) {
        // 모든 알림 허용 사용자에 대해 각각 쿼리 실행
        List<Long> userIdList = queryFactory
            .select(user.id)
            .from(user)
            .where(user.isNotificationEnabled.isTrue())
            .fetch();

        List<NotificationDto> notificationDtoList = new ArrayList<>();

        // 각 사용자별로 별도 쿼리 실행
        for (Long userId : userIdList) {
            List<NotificationDto> userResultList = queryFactory
                .select(Projections.constructor(NotificationDto.class,
                    user.email,
                    jobOpening.jobOpeningUrl))
                .from(jobOpening)
                .join(jobOpeningKeyword).on(jobOpeningKeyword.jobOpening.eq(jobOpening))
                .join(userKeyword).on(userKeyword.keyword.eq(jobOpeningKeyword.keyword))
                .join(user).on(user.eq(userKeyword.user))
                .where(
                    jobOpening.createdAt.after(referenceTime)
                        .and(user.id.eq(userId))
                )
                .groupBy(jobOpening.jobOpeningUrl, user.email)
                .orderBy(userKeyword.id.count().desc())
                .limit(20)
                .fetch();

            notificationDtoList.addAll(userResultList);
        }

        return notificationDtoList;
    }
}