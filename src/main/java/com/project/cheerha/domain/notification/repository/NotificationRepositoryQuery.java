package com.project.cheerha.domain.notification.repository;

import static com.project.cheerha.domain.jobopening.entity.QJobOpening.jobOpening;
import static com.project.cheerha.domain.keyword.entity.QJobOpeningKeyword.jobOpeningKeyword;
import static com.project.cheerha.domain.user.entity.QUser.user;
import static com.project.cheerha.domain.userkeyword.entity.QUserKeyword.userKeyword;

import com.project.cheerha.domain.notification.dto.NotificationDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.Projections;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryQuery {

    private final JPAQueryFactory queryFactory;  // JPAQueryFactory는 QueryDSL을 이용하여 쿼리를 작성하는 데 사용됨

    /**
     * 주어진 시간(referenceTime) 이후에 활동이 있는 채용 공고와 이를 본 사용자들을 조회
     * @param referenceTime : 기준 시간이 되는 시간 (주로 1시간 전과 같은 시간)
     * @return 알림을 보낼 사용자와 해당 채용 공고 정보 목록
     */
    public List<NotificationDto> findTopMatchingJobOpeningsWithUsers(ZonedDateTime referenceTime) {

        // 1. 해당 시간 이후에 활동이 있는 채용 공고와 관련된 상위 20개의 채용 공고 ID를 조회
        List<Long> top20JobOpeningIdList = queryFactory
            .select(jobOpeningKeyword.jobOpening.id)  // 채용 공고 ID를 선택
            .from(jobOpeningKeyword)  // jobOpeningKeyword 테이블에서 조회
            .join(jobOpeningKeyword.jobOpening, jobOpening)  // jobOpening과 조인
            .join(jobOpeningKeyword.keyword, userKeyword.keyword)  // jobOpeningKeyword와 userKeyword 키워드로 조인
            .where(jobOpening.createdAt.after(referenceTime))  // 채용 공고 생성 시간이 referenceTime 이후인 것만 필터링
            .groupBy(jobOpeningKeyword.jobOpening.id)  // jobOpening별로 그룹화
            .orderBy(userKeyword.id.count().desc())  // 사용자 키워드의 개수를 기준으로 내림차순 정렬
            .limit(20)  // 상위 20개만 선택
            .fetch();  // 실행하여 결과 리스트 반환

        // 2. 상위 20개 채용 공고에 해당하는 사용자와 채용 공고 URL을 조회 (fetchJoin으로 n + 1 문제 해결)
        return queryFactory
            .select(Projections.constructor(NotificationDto.class,  // NotificationDto를 결과로 반환
                user.email,  // 사용자 이메일
                jobOpening.jobOpeningUrl))  // 채용 공고 URL
            .from(jobOpening)  // jobOpening 테이블에서 조회
            .join(jobOpeningKeyword).fetchJoin()  // fetchJoin으로 jobOpeningKeyword와 관련된 엔티티들을 한 번에 조회
            .on(jobOpeningKeyword.jobOpening.id.eq(jobOpening.id))  // jobOpening과 일치하는 jobOpeningKeyword 찾기
            .join(userKeyword).fetchJoin()  // fetchJoin으로 userKeyword와 관련된 엔티티들을 한 번에 조회
            .on(userKeyword.keyword.id.eq(jobOpeningKeyword.keyword.id))  // jobOpeningKeyword와 일치하는 userKeyword 찾기
            .join(user).fetchJoin()  // fetchJoin으로 user와 관련된 엔티티들을 한 번에 조회
            .on(user.id.eq(userKeyword.user.id))  // userKeyword와 일치하는 user 찾기
            .where(jobOpening.id.in(top20JobOpeningIdList))  // 상위 20개 채용 공고에 해당하는 사용자들만 조회
            .where(user.isNotificationEnabled.isTrue())  // 알림을 허용한 사용자만 필터링
            .distinct()  // 중복을 제거
            .fetch();  // 실행하여 결과 리스트 반환
    }
}
