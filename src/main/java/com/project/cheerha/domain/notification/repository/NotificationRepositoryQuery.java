package com.project.cheerha.domain.notification.repository;

import static com.project.cheerha.domain.jobopening.entity.QJobOpening.jobOpening;
import static com.project.cheerha.domain.keyword.entity.QJobOpeningKeyword.jobOpeningKeyword;
import static com.project.cheerha.domain.user.entity.QUser.user;
import static com.project.cheerha.domain.userkeyword.entity.QUserKeyword.userKeyword;

import com.project.cheerha.domain.notification.dto.NotificationDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    /**
     * referenceTime 이후에 등록된 채용 공고와 사용자 조회
     *
     * @param referenceTime : 조회 시간
     * @return 알림을 보낼 사용자와 채용 공고 정보 목록
     */
    public List<NotificationDto> findTopMatchingJobOpeningsWithUsers(ZonedDateTime referenceTime) {

        // 첫 번째 쿼리: 알림 허용 사용자만 고려해서 상위 20개 채용공고 ID 조회
        List<Long> top20JobOpeningIdList = queryFactory
            .select(jobOpeningKeyword.jobOpening.id)
            .from(jobOpeningKeyword)
            .join(jobOpeningKeyword.jobOpening, jobOpening)
            .join(userKeyword)
            .on(userKeyword.keyword.eq(jobOpeningKeyword.keyword))
            // 여기서 user 엔티티도 조인하고 알림 허용 조건 추가
            .join(userKeyword.user, user)
            .where(
                jobOpening.createdAt.after(referenceTime)
                    .and(user.isNotificationEnabled.isTrue())
            )
            .groupBy(jobOpeningKeyword.jobOpening.id)
            .orderBy(userKeyword.id.count().desc())
            .limit(20)
            .fetch();

        // 두 번째 쿼리: 조회된 채용공고에 대해 관련 사용자(알림 허용 사용자)와 URL 정보를 조회
        return queryFactory
            .select(Projections.constructor(NotificationDto.class,
                user.email,
                jobOpening.jobOpeningUrl))
            .from(jobOpening)
            .join(jobOpeningKeyword)
            .on(jobOpeningKeyword.jobOpening.eq(jobOpening)).fetchJoin()
            .join(userKeyword)
            .on(userKeyword.keyword.eq(jobOpeningKeyword.keyword)).fetchJoin()
            .join(user)
            .on(user.eq(userKeyword.user)).fetchJoin()
            .where(jobOpening.id.in(top20JobOpeningIdList))
            // 혹시 모를 누락 방지를 위해 두 번째 쿼리에서도 알림 허용 조건 추가
            .where(
                jobOpening.createdAt.after(referenceTime)
                    .and(user.isNotificationEnabled.isTrue())
            )
            .distinct()
            .fetch();
    }
}