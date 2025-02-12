package com.project.cheerha.domain.data.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DataRepositoryQueryImpl implements DataRepositoryQuery{

    private final JPAQueryFactory queryFactory;


}
