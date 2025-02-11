package com.project.cheerha.domain.bookmark.repository;

import com.project.cheerha.domain.bookmark.entity.Bookmark;
import com.project.cheerha.domain.bookmark.entity.QBookmark;
import com.project.cheerha.domain.data.entity.Data;
import com.project.cheerha.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookmarkRepositoryQueryImpl implements BookmarkRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Bookmark> findByUserAndData(User user, Data data) {
        QBookmark bookmark = QBookmark.bookmark;
        return Optional.ofNullable(queryFactory
                .selectFrom(bookmark)
                .where(bookmark.user.eq(user)
                        .and(bookmark.data.eq(data)))
                .fetchOne());
    }

    @Override
    public List<Bookmark> findAllByUser(User user) {
        QBookmark bookmark = QBookmark.bookmark;
        return queryFactory
                .selectFrom(bookmark)
                .where(bookmark.user.eq(user))
                .fetch();
    }
}
